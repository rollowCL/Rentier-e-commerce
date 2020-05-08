package pl.coderslab.rentier.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductShop;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.entity.Shop;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.ProductRepository;
import pl.coderslab.rentier.repository.ProductShopRepository;
import pl.coderslab.rentier.repository.ProductSizeRepository;
import pl.coderslab.rentier.repository.ShopRepository;

import javax.management.openmbean.InvalidKeyException;
import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductShopServiceImpl implements ProductShopService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductShopServiceImpl.class);
    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ShopRepository shopRepository;
    private final List<String> acceptedActions = Arrays.asList("D", "U", "N");

    @Value("${rentier.dataSource}")
    private String dataSource;

    @Value("${rentier.storageConnectionString}")
    private String storageConnectionString;

    public ProductShopServiceImpl(ProductShopRepository productShopRepository, ProductRepository productRepository, ProductSizeRepository productSizeRepository, ShopRepository shopRepository) {
        this.productShopRepository = productShopRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.shopRepository = shopRepository;
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
    public String validateRow(Row row) {

        String columnAString = getCellData(row.getCell(0));
        String columnBString = getCellData(row.getCell(1));
        String columnCString = getCellData(row.getCell(2));
        String columnDString = getCellData(row.getCell(3));
        String columnEString = getCellData(row.getCell(4));

        Optional<Product> product = productRepository.findFirstByProductCode(columnAString);
        if (!product.isPresent()) {
            return "Brak produktu o kodzie " + columnAString;
        }

        Optional<Shop> shop = shopRepository.findFirstByShopCode(columnBString);
        if (!shop.isPresent()) {
            return "Brak sklepu o kodzie " + columnBString;
        }

        Optional<ProductSize> productSize = productSizeRepository.findFirstBySizeNameAndProductCategory(
                columnCString, product.get().getProductCategory());
        if (!productSize.isPresent()) {
            return "Brak rozmiaru o nazwie " + columnCString + " dla kategorii " + product.get().getProductCategory().getCategoryName();
        }

        try {
            Integer.parseInt(columnDString);
        } catch (NumberFormatException e) {
            return "Kolumna D ma błędny format - powinna być liczba";
        }

        if (!row.getCell(4).getCellType().equals(CellType.STRING)) {
            return "Kolumna E ma błędny format";
        }

        if (columnEString.length() != 1) {
            return "Kolumna E powinna mieć jeden znak a ma " + columnEString.length();
        }

        if (!isValidAction(columnEString)) {
            return "Nie rozpoznana akcja";
        }

        return "OK";
    }

    @Override
    public void processRow(Row row) {
        String columnAString = getCellData(row.getCell(0));
        String columnBString = getCellData(row.getCell(1));
        String columnCString = getCellData(row.getCell(2));
        String columnDString = getCellData(row.getCell(3));
        String columnEString = getCellData(row.getCell(4));

        Optional<Product> product = productRepository.findFirstByProductCode(columnAString);
        Optional<Shop> shop = shopRepository.findFirstByShopCode(columnBString);
        Optional<ProductSize> productSize = productSizeRepository.findFirstBySizeNameAndProductCategory(
                columnCString, product.get().getProductCategory());

        ProductShop productShop = new ProductShop();
        int currentQuantity = 0;
        Optional<ProductShop> existingProductShop = productShopRepository.findFirstByProductAndProductSizeAndShop(
                product.get(), productSize.get(), shop.get());

        if (existingProductShop.isPresent()) {
            productShop = existingProductShop.get();
            currentQuantity = productShop.getQuantity();
        } else {
            productShop.setProduct(product.get());
            productShop.setProductSize(productSize.get());
            productShop.setShop(shop.get());
            productShop.setQuantity(currentQuantity);
        }
        int quantityFromFile = Integer.parseInt(columnDString);

        logger.info("Akcja " + columnEString);
        switch (columnEString) {
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
        LocalDateTime localDateTime = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(localDateTime.getYear())
                .append(localDateTime.getMonth())
                .append(localDateTime.getDayOfMonth())
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

        } catch (InvalidKeyException invalidKey) {
            // Handle the exception
        } catch (java.security.InvalidKeyException | URISyntaxException | StorageException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FileOutputStream createLocalLogFile(String fileName) throws FileNotFoundException {
        return new FileOutputStream(new File(fileName));
    }

}
