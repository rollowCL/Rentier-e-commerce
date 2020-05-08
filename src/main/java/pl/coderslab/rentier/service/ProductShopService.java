package pl.coderslab.rentier.service;

import com.microsoft.azure.storage.file.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.pojo.CartItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public interface ProductShopService {

    void removeFromStock(CartItem cartItem);
    int validateFile(MultipartFile file, Sheet sheet) throws IOException;
    int readFile(MultipartFile file) throws IOException;
    String validateRow(Row row);
    void processRow(Row row);
    boolean isValidAction(String column);
    String getCellData(Cell cell);
    String getDateString();
    FileOutputStream createAzureLogFile(String fileName, Long size);
    java.io.FileOutputStream createLocalLogFile(String fileName) throws FileNotFoundException;
}
