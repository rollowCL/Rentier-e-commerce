package pl.coderslab.rentier.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.beans.ProductSearch;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.QProduct;
import pl.coderslab.rentier.entity.QUser;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.repository.ProductRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ImageServiceImpl imageService;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductServiceImpl(ImageServiceImpl imageService, ProductRepository productRepository, EntityManager entityManager) {
        this.imageService = imageService;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }


    @Override
    public Optional<File> saveProductImage(Part filePart, Product product, String uploadPath, String uploadPathForView)
            throws IOException, FileNotFoundException, InvalidFileException {

        File file = null;
        String fileName = imageService.getFileName(filePart);

        if (imageService.isValidFile(filePart)) {

            String suffix = "." + FilenameUtils.getExtension(fileName);
            String prefix = "product-";
            File uploads = new File(uploadPath);
            file = File.createTempFile(prefix, suffix, uploads);
            String imageFileName = uploadPathForView + file.getName();

            InputStream input = filePart.getInputStream();
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            product.setImageFileName(imageFileName);


        } else {

            throw new InvalidFileException("Błędny plik");
        }


        return Optional.of(file);
    }

    @Override
    public void deleteProductImage(Optional<File> file) {

        file.ifPresent(File::delete);

    }

    @Override
    public Iterable<Product> searchProductsForAdmin(ProductSearch productSearch) {

        QProduct product = QProduct.product;
        BooleanBuilder where = new BooleanBuilder();

        return performProductSearch(product, where, productSearch);
    }


    @Override
    public Iterable<Product> searchProductsForShop(ProductSearch productSearch) {

        QProduct product = QProduct.product;
        BooleanBuilder where = new BooleanBuilder();
        where.and(product.brand.active.eq(true));
        where.and(product.productCategory.active.eq(true));

        return performProductSearch(product, where, productSearch);
    }

    @Override
    public Iterable<Product> performProductSearch(QProduct product, BooleanBuilder where, ProductSearch productSearch) {

        if (productSearch.getProductName() != null) {
            where.and(product.productName.containsIgnoreCase(productSearch.getProductName()));
        }

        if (productSearch.getBrand() != null) {
            where.and(product.brand.id.eq(productSearch.getBrand().getId()));
        }

        if (productSearch.getProductCategory() != null) {
            where.and(product.productCategory.id.eq(productSearch.getProductCategory().getId()));
        }
        if (productSearch.isActive()) {
            where.and(product.active.eq(productSearch.isActive()));
        }
        if (productSearch.isAvailableOnline()) {
            where.and(product.availableOnline.eq(productSearch.isAvailableOnline()));
        }
        if (productSearch.getPriceGrossFrom() != null) {
            where.and(product.priceGross.goe(productSearch.getPriceGrossFrom()));
        }
        if (productSearch.getPriceGrossTo() != null) {
            where.and(product.priceGross.loe(productSearch.getPriceGrossTo()));
        }

        Iterable<Product> products = productRepository.findAll(where);

        return products;
    }


}
