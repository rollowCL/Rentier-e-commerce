package pl.coderslab.rentier.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.rentier.entity.PaymentMethod;
import pl.coderslab.rentier.repository.PaymentMethodRepository;

import java.util.Optional;

public class PaymentMethodConverter implements Converter<String, PaymentMethod> {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public PaymentMethod convert(String source) {

        if (source.trim().equals("0") || source.trim().equals("")) {
            return null;
        }

        Optional<PaymentMethod> group = paymentMethodRepository.findById(Long.parseLong(source));

        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }
}
