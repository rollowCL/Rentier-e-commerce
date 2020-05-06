package pl.coderslab.rentier.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.rentier.beans.ProductSearch;
import pl.coderslab.rentier.entity.Product;
import pl.coderslab.rentier.entity.ProductImage;
import pl.coderslab.rentier.entity.QProduct;
import pl.coderslab.rentier.exception.InvalidFileException;
import pl.coderslab.rentier.repository.ProductImageRepository;
import pl.coderslab.rentier.repository.ProductRepository;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ImageServiceImpl imageService;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final EntityManager entityManager;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Value("${rentier.uploadPathProducts}")
    private String uploadPathProducts;

    @Value("${rentier.dataSource}")
    private String dataSource;

    public ProductServiceImpl(ImageServiceImpl imageService, ProductRepository productRepository,
                              ProductImageRepository productImageRepository, EntityManager entityManager) {
        this.imageService = imageService;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.entityManager = entityManager;
    }


    @Override
    public String saveProductImage(MultipartFile file, Product product) throws IOException, InvalidFileException {
        String fileName = null;

        if (dataSource.equals("AZURE")) {
            fileName = imageService.saveImageToPath(file, "product-", uploadPathProducts);
        } else if (dataSource.equals("LOCAL")) {
            fileName = imageService.saveImageToPathLocal(file, "product-", uploadPathProducts, "/products/");
        }

        return fileName;
    }

    @Override
    public void deleteProductImage(String fileName) {

        if (dataSource.equals("AZURE")) {
            imageService.deleteBlob(fileName);
        } else if (dataSource.equals("LOCAL")) {
            logger.info(uploadPathProducts + fileName.substring(fileName.lastIndexOf('/')));
            imageService.deleteLocalFile(uploadPathProducts + fileName.substring(fileName.lastIndexOf('/')));
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

    @Override
    public void restDeleteImage(Long imageId) {

        Optional<ProductImage> productImage = productImageRepository.findById(imageId);
        if (productImage.isPresent()) {

            deleteProductImage(productImage.get().getImageFileName());
            productImageRepository.delete(productImage.get());

        }

    }

    @Override
    public void restMakeMainImage(Long imageId) {

        Optional<ProductImage> productImage = productImageRepository.findById(imageId);
        if (productImage.isPresent()) {

            productImageRepository.setAllProductImagesNotMain(productImage.get().getProduct().getId());
            productImageRepository.setImageMain(productImage.get().getId());

        }

    }

}
