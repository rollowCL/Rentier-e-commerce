package pl.coderslab.rentier.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.beans.ProductSearch;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.QProduct;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.repository.ProductRepository;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;

@Service
public class ProductServiceImpl implements ProductService {
    private final ImageServiceImpl imageService;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final RentierProperties rentierProperties;

    public ProductServiceImpl(ImageServiceImpl imageService, ProductRepository productRepository, EntityManager entityManager, RentierProperties rentierProperties) {
        this.imageService = imageService;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
        this.rentierProperties = rentierProperties;
    }


    @Override
    public String saveProductImage(MultipartFile file, Product product) throws IOException, InvalidFileException {
        String uploadPath = rentierProperties.getUploadPathProducts();
        String uploadPathForView = rentierProperties.getUploadPathProdutsForView();

        String fileName = imageService.saveImageToPath(file, "product-", uploadPath, uploadPathForView);

        product.setImageFileName(fileName);

        return fileName;
    }

    @Override
    public void deleteProductImage(String fileName) {
        String deletePath = rentierProperties.getUploadPathProductsForDelete();
        File file = new File(deletePath + fileName);

        if (file.exists()) {
            file.delete();
        }

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


        OrderSpecifier<?> orderSpecifier = null;

        if (productSearch.getSorting() != null) {

            String sortingVariant = productSearch.getSorting();

            switch (sortingVariant) {

                case "p":
                    orderSpecifier = product.priceGross.asc();
                    break;
                case "pd":
                    orderSpecifier = product.priceGross.desc();
                    break;
                case "n":
                    orderSpecifier = product.productName.asc();
                    break;
                case "nd":
                    orderSpecifier = product.productName.desc();
                    break;

            }

        }

        Iterable<Product> products;

        if (orderSpecifier == null) {
            products = productRepository.findAll(where);
        } else {
            products = productRepository.findAll(where, orderSpecifier);
        }


        return products;
    }


}
