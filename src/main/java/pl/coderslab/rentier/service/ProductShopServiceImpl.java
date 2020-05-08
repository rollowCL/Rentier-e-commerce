package pl.coderslab.rentier.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
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

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Service
public class ProductShopServiceImpl implements ProductShopService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductShopServiceImpl.class);
    private final ProductShopRepository productShopRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ShopRepository shopRepository;
    private final List<String> acceptedActions = Arrays.asList("D", "U", "N");

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
    public boolean validateFile(MultipartFile file) throws InvalidFileException {
        return false;
    }

    @Override
    public int readFile(MultipartFile file) throws IOException {

        InputStream fileInputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        FileOutputStream fileStream;
        OutputStreamWriter writer;
        String logFileName = file.getOriginalFilename().concat(".txt");
        int errors = 0;
        logger.info("Creating log file: " + logFileName);
        try {
            fileStream = new FileOutputStream(new File(logFileName));
            writer = new OutputStreamWriter(fileStream);

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

            if (errors == 0) {
                for (Row row : sheet) {
                    logger.info("Procesowanie wiersza: " + row.getRowNum());
                    processRow(row);
                }
            } else {
                logger.info("Walidacja pliku zakończyła się błędami: " + errors);
            }

        } catch (IOException e) {
            e.printStackTrace();
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

}
