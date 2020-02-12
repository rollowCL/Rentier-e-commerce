package pl.coderslab.rentier.service;

import javax.servlet.http.Part;
import java.io.IOException;

public interface ImageService {

    String getFileName(Part part);
    boolean isValidFile(Part filePart) throws IOException;
}
