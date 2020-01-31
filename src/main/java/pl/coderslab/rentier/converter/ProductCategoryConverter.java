package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;

import java.util.Optional;

public class ProductCategoryConverter implements Converter<String, ProductCategory> {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<ProductCategory> group = productCategoryRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
