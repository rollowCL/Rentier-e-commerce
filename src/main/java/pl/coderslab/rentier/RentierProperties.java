package pl.coderslab.rentier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RentierProperties {

    private String uploadPathBrands = "/home/osint/Documents/Repos/CodersLab/Rentier/src/main/resources/static/images/brands/";
    private String uploadPathBrandsForView = "/images/brands/";
    private String uploadPathProducts = "/home/osint/Documents/Repos/CodersLab/Rentier/src/main/resources/static/images/products/";
    private String uploadPathProdutsForView = "/images/products/";

    public String getUploadPathBrands() {
        return uploadPathBrands;
    }

    public String getUploadPathBrandsForView() {
        return uploadPathBrandsForView;
    }

    public String getUploadPathProducts() {
        return uploadPathProducts;
    }

    public String getUploadPathProdutsForView() {
        return uploadPathProdutsForView;
    }

}
