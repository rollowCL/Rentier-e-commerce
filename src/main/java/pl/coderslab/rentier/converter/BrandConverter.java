package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.repository.BrandRepository;

import java.util.Optional;

public class BrandConverter implements Converter<String, Brand> {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<Brand> group = brandRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
