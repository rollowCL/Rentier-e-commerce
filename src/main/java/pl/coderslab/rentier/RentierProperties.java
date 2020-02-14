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

    private final String mailHostName = "poczta.o2.pl";
    private final String mailPort = "465";
    private final String mailFrom = "rentier.test@o2.pl";
    private final String mailSMTP = "poczta.o2.pl";
    private final String mailPassword = "";
    private final String mailPersonal = "Rentier | Sklep";


    public String getOrderStartStatus() {
        return orderStartStatus;
    }

    public String getUploadPathBrands() {
        return uploadPathBrands;
    }

    public String getUploadPathBrandsForDelete() {
        return uploadPathBrandsForDelete;
    }

    public String getUploadPathBrandsForView() {
        return uploadPathBrandsForView;
    }

    public String getUploadPathProducts() {
        return uploadPathProducts;
    }

    public String getUploadPathProductsForDelete() {
        return uploadPathProductsForDelete;
    }

    public String getUploadPathProdutsForView() {
        return uploadPathProdutsForView;
    }

    public String getMailHostName() {
        return mailHostName;
    }

    public String getMailPort() {
        return mailPort;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailSMTP() {
        return mailSMTP;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getMailPersonal() {
        return mailPersonal;
    }
}
