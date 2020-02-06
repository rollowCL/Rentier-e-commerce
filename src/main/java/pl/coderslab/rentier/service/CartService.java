package pl.coderslab.rentier.service;

import pl.coderslab.rentier.exception.ProductQuantityExceededException;
import pl.coderslab.rentier.beans.Cart;

public interface CartService {

    int checkCartProductIndex(Long productId, Long productSizeId, Cart cart);
    void cartAdd(Long productId, Long productSizeId, Integer quantity, Cart cart) throws ProductQuantityExceededException;
    void cartRemove(Long productId, Long productSizeId, Cart cart);
    int checkCartProductQuantity(Long productId, Long productSizeId, Cart cart);
    int checkStockProductAvailableQuantity(Long productId, Long productSizeId);

}
