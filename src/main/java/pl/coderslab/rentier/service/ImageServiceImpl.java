package pl.coderslab.rentier.service;

import com.microsoft.azure.storage.blob.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductImage;
import pl.coderslab.rentier.exception.InvalidFileException;
import com.microsoft.azure.storage.*;
import pl.coderslab.rentier.repository.ProductImageRepository;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageServiceImpl implements ImageService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ProductImageRepository productImageRepository;

    @Value("${rentier.storageConnectionString}")
    private String storageConnectionString;

    public ImageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    public String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return null;
    }

    @Override
    public void isValidFile(MultipartFile filePart) throws InvalidFileException {

        if (filePart.getSize() > 1024 * 1024) {
            throw new InvalidFileException("Błędny rozmiar pliku ");
        }

        String regexp = ".*(jpe?g|png|bmp)$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(Objects.requireNonNull(filePart.getOriginalFilename()));

        if (!matcher.matches()) {
            throw new InvalidFileException("Błędny format pliku ");
        }

    }

    @Override
    public String saveImageToPath(MultipartFile filePart, String filePrefix, String uploadPath)
            throws IOException {
        CloudBlockBlob blob = null;

        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference("rentier");
            String fileSuffix = "." + FilenameUtils.getExtension(filePart.getOriginalFilename());
            String blobName = generateRandomName(20) + fileSuffix;
            blob = container.getBlockBlobReference(uploadPath + blobName);
            blob.upload(filePart.getInputStream(), filePart.getSize());

        } catch (StorageException | URISyntaxException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return String.valueOf(blob.getUri());
    }

    @Override
    public String saveImageToPathLocal(MultipartFile filePart, String filePrefix, String uploadPath, String resourcePath) throws IOException {

        File newFile = null;
        String imageFileName = null;

        String fileSuffix = "." + FilenameUtils.getExtension(filePart.getOriginalFilename());
        File uploads = new File(uploadPath);
        newFile = File.createTempFile(filePrefix, fileSuffix, uploads);
        imageFileName = resourcePath + newFile.getName();

        InputStream input = filePart.getInputStream();
        Files.copy(input, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


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
        return fileName.substring(fileName.substring(0, (fileName.lastIndexOf('/'))).lastIndexOf('/') + 1);
    }

    @Override
    public void deleteLocalFile(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

    }

    @Override
    public String generateRandomName(int len) {

        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }

        return sb.toString();


    }


}
