package pl.coderslab.rentier.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.exception.InvalidFileException;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    private final ImageServiceImpl imageService;

    public BrandServiceImpl(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }


    @Override
    public Optional<File> saveBrandImage(Part filePart, Brand brand, String uploadPath, String uploadPathForView) throws IOException, FileNotFoundException, InvalidFileException {
        File file = null;
        String fileName = imageService.getFileName(filePart);

        if (imageService.isValidFile(filePart)) {

            String suffix = "." + FilenameUtils.getExtension(fileName);
            String prefix = "brand-";
            File uploads = new File(uploadPath);
            file = File.createTempFile(prefix, suffix, uploads);
            String imageFileName = uploadPathForView + file.getName();

            InputStream input = filePart.getInputStream();
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            brand.setLogoFileName(imageFileName);


        } else {

            throw new InvalidFileException("Błędny plik");
        }


        return Optional.of(file);
    }
}
