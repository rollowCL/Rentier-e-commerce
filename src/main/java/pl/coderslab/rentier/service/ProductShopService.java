package pl.coderslab.rentier.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.pojo.CartItem;

import java.io.FileOutputStream;
import java.io.IOException;

public interface ProductShopService {

    void removeFromStock(CartItem cartItem);
    boolean validateFile(MultipartFile file) throws InvalidFileException;
    int readFile(MultipartFile file) throws IOException;
    String validateRow(Row row);
    void processRow(Row row);
    boolean isValidAction(String column);
    String getCellData(Cell cell);
}
