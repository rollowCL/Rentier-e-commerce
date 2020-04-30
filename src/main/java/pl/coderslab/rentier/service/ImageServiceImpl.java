package pl.coderslab.rentier.service;

import com.microsoft.azure.storage.blob.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.exception.InvalidFileException;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.file.*;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageServiceImpl implements ImageService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Value("${rentier.storageConnectionString}")
    private String storageConnectionString;
//    =
//            "DefaultEndpointsProtocol=http;" +
//                    "AccountName=csb10032000b93e64dd;" +
//                    "AccountKey=VjctOgvBg2JigVuV2RcEIvyAK1tCRw1N8NhEq2ox1za1odfJmnkamjxZ1bg8Hh7T/aM9VWp+l4mafWB/WsnN+Q==";

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
    public String saveImageToPath(MultipartFile filePart, String filePrefix, String uploadPath)
            throws InvalidFileException, IOException {
        CloudBlockBlob blob = null;

        if (isValidFile(filePart)) {

            try {
                CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                CloudBlobContainer container = blobClient.getContainerReference("rentier");
                blob = container.getBlockBlobReference(uploadPath + filePart.getOriginalFilename());
                blob.upload(filePart.getInputStream(), filePart.getSize());
                logger.info(String.valueOf(blob.getUri()));

            } catch (StorageException | URISyntaxException | InvalidKeyException e) {
                e.printStackTrace();
            }

        } else {

            throw new InvalidFileException("Błędny plik");
        }


        return String.valueOf(blob.getUri());
    }

    @Override
    public String saveImageToPathLocal(MultipartFile filePart, String filePrefix, String uploadPath, String resourcePath) throws InvalidFileException, IOException {

        File newFile = null;
        String imageFileName = null;

        if (isValidFile(filePart)) {

            String fileSuffix = "." + FilenameUtils.getExtension(filePart.getOriginalFilename());
            File uploads = new File(uploadPath);
            newFile = File.createTempFile(filePrefix, fileSuffix, uploads);
            imageFileName = resourcePath + newFile.getName();

            InputStream input = filePart.getInputStream();
            Files.copy(input, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } else {

            throw new InvalidFileException("Błędny plik");
        }

        return imageFileName;
    }

    @Override
    public void deleteBlob(String fileName) {

        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference("rentier");
            CloudBlockBlob blob = container.getBlockBlobReference(getBlobName(fileName));
            logger.info("deleting blob: " + getBlobName(fileName));
            blob.deleteIfExists();
        } catch (StorageException | URISyntaxException | InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBlobName(String fileName) {
        return fileName.substring(fileName.substring(0, (fileName.lastIndexOf('/'))).lastIndexOf('/')+1);
    }

    @Override
    public void deleteLocalFile(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

    }

}
