package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.OrderType;
import pl.coderslab.rentier.repository.OrderTypeRepository;

import java.util.Optional;

public class OrderTypeConverter implements Converter<String, OrderType> {

    @Autowired
    private OrderTypeRepository orderTypeRepository;

    @Override
    public OrderType convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<OrderType> group = orderTypeRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
