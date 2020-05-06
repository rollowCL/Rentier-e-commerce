package pl.coderslab.rentier.service;

import com.querydsl.core.BooleanBuilder;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.beans.ProductSearch;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.QProduct;
import pl.coderslab.rentier.exception.InvalidFileException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface ProductService {

    String saveProductImage(MultipartFile file, Product product) throws IOException, InvalidFileException;
    void deleteProductImage(String fileName);

    Iterable<Product> searchProductsForShop(ProductSearch productSearch);

    Iterable<Product> searchProductsForAdmin(ProductSearch productSearch);

    Iterable<Product> performProductSearch(QProduct product, BooleanBuilder where, ProductSearch productSearch);

    void restDeleteImage(Long imageId);

    void restMakeMainImage(Long imageId);

}
