package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.DeliveryMethod;
import pl.coderslab.rentier.repository.DeliveryMethodRepository;

import java.util.Optional;

public class DeliveryMethodConverter implements Converter<String, DeliveryMethod> {

    @Autowired
    private DeliveryMethodRepository deliveryMethodRepository;

    @Override
    public DeliveryMethod convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<DeliveryMethod> group = deliveryMethodRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
