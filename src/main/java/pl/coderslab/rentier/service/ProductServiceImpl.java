package pl.coderslab.rentier.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return null;
    }

    @Override
    public boolean isValidFile(Part filePart) throws IOException {

        if (filePart.getSize() > 1024 * 1024) {
            return false;
        }

        String regexp = ".*(jpe?g|png|bmp)$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(filePart.getSubmittedFileName());

        if (!matcher.matches()) {
            return false;
        }


        return true;
    }

    @Override
    public Optional<File> saveProductImage(Part filePart, Product product, String uploadPath, String uploadPathForView)
            throws IOException, FileNotFoundException, InvalidFileException {

        File file = null;
        String fileName = getFileName(filePart);

        if (isValidFile(filePart)) {

            String suffix = "." + FilenameUtils.getExtension(fileName);
            String prefix = "product-";
            File uploads = new File(uploadPath);
            file = File.createTempFile(prefix, suffix, uploads);
            String imageFileName = uploadPathForView + file.getName();

            InputStream input = filePart.getInputStream();
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            product.setImageFileName(imageFileName);


        } else {

            throw new InvalidFileException("Błędny plik");
        }


        return Optional.of(file);
    }


}
