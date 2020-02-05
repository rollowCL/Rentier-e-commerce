package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import pl.coderslab.rentier.repository.OrderNumberRepository;

import java.util.Optional;

@Service
public class OrderNumberServiceImpl implements OrderNumberService {

    private final OrderNumberRepository orderNumberRepository;

    public OrderNumberServiceImpl(OrderNumberRepository orderNumberRepository) {
        this.orderNumberRepository = orderNumberRepository;
    }


    @Override
    public Integer checkLastOrderNumber(int year) {
        Optional<Integer> orderNumber = orderNumberRepository.findMaxNumberByYear(year);

        if (orderNumber.isPresent()) {
            return orderNumber.get();
        }
        return 0;
    }

    @Override
    public void registerOrderNumber(int year, int orderNumber) {
        orderNumberRepository.insertNewOrderNumber(year, orderNumber);
    }
}
