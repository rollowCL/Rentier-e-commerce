package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.exception.InvalidFileException;

import java.io.IOException;

@Service
public class BrandServiceImpl implements BrandService {

    @Value("${rentier.uploadPathBrands}")
    private String uploadPathBrands;

    @Value("${rentier.dataSource}")
    private String dataSource;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);
    private final ImageServiceImpl imageService;

    public BrandServiceImpl(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }


    @Override
    public String saveBrandLogo(MultipartFile file, Brand brand) throws IOException, InvalidFileException {
        String fileName = null;

        if (dataSource.equals("AZURE")) {
            fileName = imageService.saveImageToPath(file, "brand-", uploadPathBrands);
        } else if (dataSource.equals("LOCAL")) {
            fileName = imageService.saveImageToPathLocal(file, "brand-", uploadPathBrands, "/brands/");
        }

        brand.setLogoFileName(fileName);

        return fileName;
    }

    @Override
    public void deleteBrandLogo(String fileName) {

        if (dataSource.equals("AZURE")) {
            imageService.deleteBlob(fileName);
        } else if (dataSource.equals("LOCAL")) {
            logger.info(uploadPathBrands + fileName.substring(fileName.lastIndexOf('/')));
            imageService.deleteLocalFile(uploadPathBrands + fileName.substring(fileName.lastIndexOf('/')));
        }

    }
}
