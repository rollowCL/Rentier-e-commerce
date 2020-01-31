package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.OrderStatus;
import pl.coderslab.rentier.repository.OrderStatusRepository;

import java.util.Optional;

public class OrderStatusConverter implements Converter<String, OrderStatus> {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public OrderStatus convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<OrderStatus> group = orderStatusRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
