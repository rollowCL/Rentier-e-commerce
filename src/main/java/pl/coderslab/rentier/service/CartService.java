package pl.coderslab.rentier.service;

import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.rentier.beans.Cart;

public interface CartService {

    int checkCartIndex(Long productId, Long productSizeId, Cart cart);
    void cartAdd(Long productId, Long productSizeId, Integer quantity, Cart cart);
    void cartRemove(Long productId, Long productSizeId, Cart cart);

}
