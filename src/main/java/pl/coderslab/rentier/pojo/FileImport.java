package pl.coderslab.rentier.pojo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileImport {

    CommonsMultipartFile importFile;

    public CommonsMultipartFile getImportFile() {
        return importFile;
    }

    public void setImportFile(CommonsMultipartFile importFile) {
        this.importFile = importFile;
    }
}
