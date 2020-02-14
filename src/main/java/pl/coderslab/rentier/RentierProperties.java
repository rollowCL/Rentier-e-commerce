package pl.coderslab.rentier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class RentierProperties {

    private final String uploadPathBrands = "/home/osint/Documents/Repos/CodersLab/Rentier/src/main/resources/static/images/brands/";
    private final String uploadPathBrandsForDelete = "/home/osint/Documents/Repos/CodersLab/Rentier/src/main/resources/static";
    private final String uploadPathBrandsForView = "/images/brands/";

    private final String uploadPathProducts = "/home/osint/Documents/Repos/CodersLab/Rentier/src/main/resources/static/images/products/";
    private final String uploadPathProductsForDelete = "/home/osint/Documents/Repos/CodersLab/Rentier/src/main/resources/static";
    private final String uploadPathProdutsForView = "/images/products/";

    private final String orderStartStatus = "Nowy";

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

    public String getUploadPathBrandsForDelete() {
        return uploadPathBrandsForDelete;
    }

    public String getUploadPathProductsForDelete() {
        return uploadPathProductsForDelete;
    }

    public String getOrderStartStatus() {
        return orderStartStatus;
    }
}
