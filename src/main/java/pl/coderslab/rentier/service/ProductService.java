package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.exception.InvalidFileException;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface ProductService {

    String getFileName(Part part);
    boolean isValidFile(Part filePart) throws IOException;
    Optional<File> saveProductImage(Part filePart, Product product, String uploadPath, String uploadPathForView)
            throws IOException, FileNotFoundException, InvalidFileException;



}
