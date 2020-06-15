package pl.coderslab.rentier.observer;

import pl.coderslab.rentier.entity.Order;

public interface Observer {

    void update(Order order);
}
