package pl.coderslab.rentier.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.controller.shop.CartController;
import pl.coderslab.rentier.entity.Order;
import pl.coderslab.rentier.entity.OrderDetail;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.OrderDetailRepository;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CartController.class);
    private final OrderDetailRepository orderDetailRepository;
    private final ProductShopServiceImpl productShopService;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, ProductShopServiceImpl productShopService) {
        this.orderDetailRepository = orderDetailRepository;
        this.productShopService = productShopService;
    }


    @Override
    public void placeOrderDetails(Cart cart, Order order) {

        for (CartItem cartItem : cart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrder(order);
            orderDetail.setProductName(cartItem.getProduct().getProductName());
            orderDetail.setProductDesc(cartItem.getProduct().getProductDesc());
            orderDetail.setProductCategoryName(cartItem.getProduct().getProductCategory().getCategoryName());
            orderDetail.setProductSizeName(cartItem.getProductSize().getSizeName());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPriceGross(cartItem.getProduct().getPriceGross());
            orderDetail.setValueGross(cartItem.getCartItemValue());
            orderDetailRepository.save(orderDetail);

            productShopService.removeFromStock(cartItem);



        }

    }
}
