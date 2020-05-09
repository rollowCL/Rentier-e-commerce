package pl.coderslab.rentier.service;

import com.microsoft.azure.storage.file.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.pojo.CartItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ProductShopService {

    void removeFromStock(CartItem cartItem);
    int validateFile(MultipartFile file, Sheet sheet) throws IOException;
    int readFile(MultipartFile file) throws IOException;
    List<String> readRow(Row row);
    String validateRow(Row row);
    void processRow(Row row);
    boolean isValidAction(String column);
    String getCellData(Cell cell);
    String getDateString();
    FileOutputStream createAzureLogFile(String fileName, Long size);
    java.io.FileOutputStream createLocalLogFile(String fileName) throws FileNotFoundException;
    Optional<Product> getProductFromRowData(List<String> rowData);
    Optional<Shop> getShopFromRowData(List<String> rowData);
    Optional<ProductSize> getProductSizeFromRowData(List<String> rowData, ProductCategory productCategory);
    Optional<ProductShop> getExistingProductShopForRowData(Product product, ProductSize productSize, Shop shop);
    URL copyCloudFileToBlob(String fileName);
}
