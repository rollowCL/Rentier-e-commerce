package pl.coderslab.rentier.service;

import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.rentier.ProductQuantityExceededException;
import pl.coderslab.rentier.beans.Cart;

public interface CartService {

    int checkCartProductIndex(Long productId, Long productSizeId, Cart cart);
    void cartAdd(Long productId, Long productSizeId, Integer quantity, Cart cart) throws ProductQuantityExceededException;
    void cartRemove(Long productId, Long productSizeId, Cart cart);
    int checkCartProductQuantity(Long productId, Long productSizeId, Cart cart);
    int checkStockProductAvailableQuantity(Long productId, Long productSizeId);

}
