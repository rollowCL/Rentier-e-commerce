package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.entity.ProductShop;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.ProductShopRepository;

import java.util.List;
import java.util.ListIterator;

@Service
public class ProductShopServiceImpl implements ProductShopService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductShopServiceImpl.class);
    private final ProductShopRepository productShopRepository;

    public ProductShopServiceImpl(ProductShopRepository productShopRepository) {
        this.productShopRepository = productShopRepository;
    }

    @Override
    public void removeFromStock(CartItem cartItem) {

        List<ProductShop> productShopList =
                productShopRepository.findByProductAndProductSizeOrderByShopId(cartItem.getProduct(), cartItem.getProductSize());

        int cartItemQuantityToRemove = cartItem.getQuantity();

        ListIterator<ProductShop> productShopListIterator = productShopList.listIterator();

        while (productShopListIterator.hasNext()) {
            ProductShop currProductShop = productShopListIterator.next();
            int currProductShopQuantity = currProductShop.getQuantity();

            if (currProductShopQuantity <= cartItemQuantityToRemove) {

                cartItemQuantityToRemove -= currProductShopQuantity;
                productShopRepository.delete(currProductShop);

            } else {

                currProductShop.setQuantity(currProductShopQuantity - cartItemQuantityToRemove);
                cartItemQuantityToRemove = 0;
                productShopRepository.save(currProductShop);
                break;
            }

        }

        if (cartItemQuantityToRemove > 0) {
            logger.error("To much quantity sold: " + cartItem + ", left: " + cartItemQuantityToRemove);
        }

    }
}
