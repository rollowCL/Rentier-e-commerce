package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.repository.ProductSizeRepository;

import java.util.Optional;

public class ProductSizeConverter implements Converter<String, ProductSize> {

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Override
    public ProductSize convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<ProductSize> group = productSizeRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
