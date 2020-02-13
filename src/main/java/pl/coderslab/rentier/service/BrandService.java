package pl.coderslab.rentier.service;

import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.exception.InvalidFileException;
import java.io.IOException;

public interface BrandService {

    String saveBrandLogo(MultipartFile file, Brand brand) throws IOException, InvalidFileException;
    void deleteBrandLogo(String fileName);
}
