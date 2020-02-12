package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.exception.InvalidFileException;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface BrandService {
    public Optional<File> saveBrandImage(Part filePart, Brand brand, String uploadPath, String uploadPathForView)
            throws IOException, FileNotFoundException, InvalidFileException;

    void deleteBrandLogo(Optional<File> file);
}
