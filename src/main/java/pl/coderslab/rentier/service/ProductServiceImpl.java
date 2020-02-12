package pl.coderslab.rentier.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
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

        if (file.isPresent()) {

            file.get().delete();

        }

    }

    public List<Product> FindOne() {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct product = QProduct.product;

        List <Product> products = queryFactory.selectFrom(product)
                .where(product.productName.toLowerCase().contains("k"))
                .fetch();

        return products;

    }






}
