package pl.coderslab.rentier.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;
import pl.coderslab.rentier.repository.ProductSizeRepository;
import pl.coderslab.rentier.repository.ShopRepository;

import javax.management.openmbean.InvalidKeyException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ProductShopServiceImpl implements ProductShopService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductShopServiceImpl.class);
    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ShopRepository shopRepository;
    private final EmailServiceImpl emailService;
    private final List<String> acceptedActions = Arrays.asList("D", "U", "N");
    private final UserServiceImpl userService;

    @Value("${rentier.dataSource}")
    private String dataSource;

    @Value("${rentier.storageConnectionString}")
    private String storageConnectionString;

    public ProductShopServiceImpl(ProductShopRepository productShopRepository, ProductRepository productRepository, ProductSizeRepository productSizeRepository, ShopRepository shopRepository, EmailServiceImpl emailService, UserServiceImpl userService) {
        this.productShopRepository = productShopRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.shopRepository = shopRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public void removeFromStock(CartItem cartItem) {

        List<ProductShop> productShopList =
                productShopRepository.findByProductAndProductSizeOrderByShopId(cartItem.getProduct(), cartItem.getProductSize());

        int cartItemQuantityToRemove = cartItem.getQuantity();

        ListIterator<ProductShop> productShopListIterator = productShopList.listIterator();

        while (productShopListIterator.hasNext()) {
            ProductShop currProductShop = productShopListIterator.next();
            int currProductShopQuantity = currProductShop.getQuantity();

            if (currProductShopQuantity <= cartItemQuantityToRemove) {

                cartItemQuantityToRemove -= currProductShopQuantity;
                productShopRepository.delete(currProductShop);

            } else {

                currProductShop.setQuantity(currProductShopQuantity - cartItemQuantityToRemove);
                cartItemQuantityToRemove = 0;
                productShopRepository.save(currProductShop);
                break;
            }

        }

        if (cartItemQuantityToRemove > 0) {
            logger.error("To much quantity sold: " + cartItem + ", left: " + cartItemQuantityToRemove);
        }

    }

    @Override
    public int validateFile(MultipartFile file, Sheet sheet) throws IOException {

        String logFileName = getDateString() + "_" + file.getOriginalFilename().concat(".txt");
        logger.info("Creating log file: " + logFileName);

        OutputStreamWriter writer = null;
        if (dataSource.equals("LOCAL")) {
            FileOutputStream fileOutputStream = createLocalLogFile(logFileName);
            writer = new OutputStreamWriter(fileOutputStream);
        } else if (dataSource.equals("AZURE")) {
            com.microsoft.azure.storage.file.FileOutputStream fileOutputStream = createAzureLogFile(logFileName, file.getSize());
            writer = new OutputStreamWriter(fileOutputStream);
        }

        int errors = 0;
        for (Row row : sheet) {
            Cell firstCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (!(firstCell == null)) {
                logger.info("Walidacja wiersza row: " + row.getRowNum());
                String rowValid = validateRow(row);
                if (!rowValid.equals("OK")) {
                    errors++;
                }
                logger.info(rowValid);
                writer.write("Wiersz: " + (row.getRowNum() + 1) + " - " + rowValid + "\n");
            }

        }
        writer.close();
        emailService.sendEmailWithAttachment(userService.getLoggedUser(), copyCloudFileToBlob(logFileName), logFileName);

        if (errors > 0) {
            logger.info("Walidacja pliku zakończyła się błędami: " + errors);
        } else {
            logger.info("Walidacja pliku zakończyła się brakiem błędów");
        }

        return errors;
    }

    @Override
    public int readFile(MultipartFile file) throws IOException {

        InputStream fileInputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        int errors = validateFile(file, sheet);

        if (errors == 0) {
            for (Row row : sheet) {
                logger.info("Procesowanie wiersza: " + row.getRowNum());
                processRow(row);
            }
        }

        return errors;
    }

    @Override
    public List<String> readRow(Row row) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            result.add(getCellData(row.getCell(i)));
        }
       return result;
    }

    @Override
    public String validateRow(Row row) {

        List<String> rowData = readRow(row);

        Optional<Product> product = getProductFromRowData(rowData);
        if (!product.isPresent()) {
            return "Brak produktu o kodzie " + rowData.get(0);
        }

        Optional<Shop> shop = getShopFromRowData(rowData);
        if (!shop.isPresent()) {
            return "Brak sklepu o kodzie " + rowData.get(1);
        }

        Optional<ProductSize> productSize = getProductSizeFromRowData(rowData, product.get().getProductCategory());
        if (!productSize.isPresent()) {
            return "Brak rozmiaru o nazwie " + rowData.get(2) + " dla kategorii " + product.get().getProductCategory().getCategoryName();
        }

        try {
            Integer.parseInt(rowData.get(3));
        } catch (NumberFormatException e) {
            return "Kolumna D ma błędny format - powinna być liczba";
        }

        if (!row.getCell(4).getCellType().equals(CellType.STRING)) {
            return "Kolumna E ma błędny format";
        }

        if (rowData.get(4).length() != 1) {
            return "Kolumna E powinna mieć jeden znak a ma " + rowData.get(4).length();
        }

        if (!isValidAction(rowData.get(4))) {
            return "Nie rozpoznana akcja";
        }

        return "OK";
    }

    @Override
    public void processRow(Row row) {
        List<String> rowData = readRow(row);

        Optional<Product> product = getProductFromRowData(rowData);
        Optional<Shop> shop = getShopFromRowData(rowData);
        Optional<ProductSize> productSize = getProductSizeFromRowData(rowData, product.get().getProductCategory());

        ProductShop productShop = new ProductShop();
        int currentQuantity = 0;
        Optional<ProductShop> existingProductShop = getExistingProductShopForRowData(product.get(), productSize.get(), shop.get());

        if (existingProductShop.isPresent()) {
            productShop = existingProductShop.get();
            currentQuantity = productShop.getQuantity();
        } else {
            productShop.setProduct(product.get());
            productShop.setProductSize(productSize.get());
            productShop.setShop(shop.get());
            productShop.setQuantity(currentQuantity);
        }
        int quantityFromFile = Integer.parseInt(rowData.get(3));

        logger.info("Akcja " + rowData.get(4));
        switch (rowData.get(4)) {
            case "D":
                logger.info("current " + currentQuantity + " from file " + quantityFromFile);
                productShop.setQuantity(currentQuantity + quantityFromFile);
                productShopRepository.save(productShop);
                break;
            case "N":
                logger.info("new quantity " + quantityFromFile);
                productShop.setQuantity(quantityFromFile);
                productShopRepository.save(productShop);
                break;
            case "U":
                logger.info("deleting productShop");
                productShopRepository.delete(productShop);
                break;
        }
    }

    @Override
    public boolean isValidAction(String column) {
        for (String action : acceptedActions) {
            if (action.equals(column)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getCellData(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    @Override
    public String getDateString() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        StringBuilder sb = new StringBuilder();
        sb.append(localDateTime.getYear()).append("_")
                .append(localDateTime.getMonthValue()).append("_")
                .append(localDateTime.getDayOfMonth()).append("_")
                .append(localDateTime.getHour())
                .append(localDateTime.getMinute())
                .append(localDateTime.getSecond());

        return sb.toString();

    }

    @Override
    public com.microsoft.azure.storage.file.FileOutputStream createAzureLogFile(String fileName, Long size) {
        try {

            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudFileClient fileClient = storageAccount.createCloudFileClient();
            CloudFileShare share = fileClient.getShareReference("rentier");

            if (share.createIfNotExists()) {
                System.out.println("New share created");
            }

            CloudFileDirectory rootDir = share.getRootDirectoryReference();
            CloudFileDirectory logsDir = rootDir.getDirectoryReference("logs");

            if (logsDir.createIfNotExists()) {
                System.out.println("logsDir created");
            } else {
                System.out.println("logsDir already exists");
            }

            CloudFile cloudFile = logsDir.getFileReference(fileName);
            return cloudFile.openWriteNew(size);

        } catch (InvalidKeyException | java.security.InvalidKeyException | URISyntaxException | StorageException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FileOutputStream createLocalLogFile(String fileName) throws FileNotFoundException {
        return new FileOutputStream(new File(fileName));
    }

    @Override
    public Optional<Product> getProductFromRowData(List<String> rowData) {
        return productRepository.findFirstByProductCode(rowData.get(0));
    }

    @Override
    public Optional<Shop> getShopFromRowData(List<String> rowData) {
        return shopRepository.findFirstByShopCode(rowData.get(1));
    }

    @Override
    public Optional<ProductSize> getProductSizeFromRowData(List<String> rowData, ProductCategory productCategory) {
        return productSizeRepository.findFirstBySizeNameAndProductCategory(
                rowData.get(2), productCategory);
    }

    @Override
    public Optional<ProductShop> getExistingProductShopForRowData(Product product, ProductSize productSize, Shop shop) {
        return productShopRepository.findFirstByProductAndProductSizeAndShop(
                product, productSize, shop);
    }


    @Override
    public URL copyCloudFileToBlob(String fileName) {
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudFileClient fileClient = storageAccount.createCloudFileClient();
            CloudFileShare share = fileClient.getShareReference("rentier");
            CloudFileDirectory rootDir = share.getRootDirectoryReference();
            CloudFileDirectory logsDir = rootDir.getDirectoryReference("logs");
            CloudFile cloudFile = logsDir.getFileReference(fileName);
            InputStream cloudFileInputStream = cloudFile.openRead();

            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference("rentier");
            CloudBlockBlob blob = container.getBlockBlobReference("/logs/" + fileName);
            BlobOutputStream blobOutputStream = blob.openOutputStream();
            int next = -1;
            while((next = cloudFileInputStream.read()) != -1) {
                blobOutputStream.write(next);
            }
            blobOutputStream.close();
            cloudFileInputStream.close();
            return blob.getUri().toURL();

        } catch (InvalidKeyException | java.security.InvalidKeyException | URISyntaxException | StorageException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
