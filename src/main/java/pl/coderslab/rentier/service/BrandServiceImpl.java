package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.exception.InvalidFileException;
import java.io.File;
import java.io.IOException;

@Service
public class BrandServiceImpl implements BrandService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);
    private final ImageServiceImpl imageService;
    private final RentierProperties rentierProperties;

    public BrandServiceImpl(ImageServiceImpl imageService, RentierProperties rentierProperties) {
        this.imageService = imageService;
        this.rentierProperties = rentierProperties;
    }


    @Override
    public String saveBrandLogo(MultipartFile file, Brand brand) throws IOException, InvalidFileException {
        String uploadPath = rentierProperties.getUploadPathBrands();
        String uploadPathForView = rentierProperties.getUploadPathBrandsForView();

        String fileName = imageService.saveImageToPath(file, "brand-", uploadPath, uploadPathForView);

        brand.setLogoFileName(fileName);

        return fileName;
    }

    @Override
    public void deleteBrandLogo(String fileName) {
        String deletePath = rentierProperties.getUploadPathBrandsForDelete();
        File file = new File(deletePath + fileName);

        if (file.exists()) {
            file.delete();
        }

    }
}
