package pl.coderslab.rentier.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.exception.InvalidFileException;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return null;
    }

    @Override
    public boolean isValidFile(MultipartFile filePart) throws IOException {

        if (filePart.getSize() > 1024 * 1024) {
            return false;
        }

        String regexp = ".*(jpe?g|png|bmp)$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(Objects.requireNonNull(filePart.getOriginalFilename()));

        return matcher.matches();
    }

    @Override
    public String saveImageToPath(MultipartFile filePart, String filePrefix, String uploadPath, String uploadPathForView)
            throws InvalidFileException, IOException {

        File newFile = null;
        String imageFileName = null;

        if (isValidFile(filePart)) {

            String fileSuffix = "." + FilenameUtils.getExtension(filePart.getOriginalFilename());
            File uploads = new File(uploadPath);
            newFile = File.createTempFile(filePrefix, fileSuffix, uploads);
            imageFileName = uploadPathForView + newFile.getName();

            InputStream input = filePart.getInputStream();
            Files.copy(input, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } else {

            throw new InvalidFileException("Błędny plik");
        }


        return imageFileName;
    }

}
