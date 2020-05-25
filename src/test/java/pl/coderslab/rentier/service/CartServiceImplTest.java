package pl.coderslab.rentier.service;

import static org.junit.Assert.*;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.rentier.InMemoryTestConfig;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { InMemoryTestConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class CartServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(CartServiceImplTest.class);

    @Resource
    ProductRepository productRepository;

    @Resource
    AddressRepository addressRepository;

    @Resource
    ShopRepository shopRepository;

    @Resource
    ProductSizeRepository productSizeRepository;

    @Resource
    ProductShopRepository productShopRepository;

    @Resource
    ProductCategoryRepository productCategoryRepository;

    @Resource
    BrandRepository brandRepository;

    @Test
    public void cartAdd() {
    }

    @Test
    public void cartRemove() {
        CartServiceImpl service = new CartServiceImpl(productRepository, productSizeRepository, productShopRepository);
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        Product product = new Product();
        ProductSize productSize = new ProductSize();

        product.setId(1L);
        product.setPriceGross(BigDecimal.valueOf(15));
        productSize.setId(1L);

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(1);

        cart.addToCart(cartItem);

        assertEquals(0, service.checkCartProductIndex(product.getId(), productSize.getId(), cart));
        service.cartRemove(product.getId(), productSize.getId(), cart);
        assertEquals(-1, service.checkCartProductIndex(product.getId(), productSize.getId(), cart));

    }

    @Test
    public void checkCartProductIndex() {
        CartServiceImpl service = new CartServiceImpl(productRepository, productSizeRepository, productShopRepository);
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        Product product = new Product();
        ProductSize productSize = new ProductSize();

        product.setId(1L);
        product.setPriceGross(BigDecimal.valueOf(15));
        productSize.setId(1L);

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(1);
        cart.addToCart(cartItem);

        assertEquals(0, service.checkCartProductIndex(product.getId(), productSize.getId(), cart));

    }

    @Test
    public void checkCartProductQuantity() {
        int expectedQuantity = 10;

        CartServiceImpl service = new CartServiceImpl(productRepository, productSizeRepository, productShopRepository);
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        Product product = new Product();
        ProductSize productSize = new ProductSize();

        product.setId(1L);
        product.setPriceGross(BigDecimal.valueOf(15));
        productSize.setId(1L);

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(expectedQuantity);
        cart.addToCart(cartItem);

        assertEquals(expectedQuantity, service.checkCartProductQuantity(product.getId(), productSize.getId(), cart));

    }


    @Test
    public void givenProductShop_checkStockProductAvailableQuantityForCart() {
        CartServiceImpl service = new CartServiceImpl(productRepository, productSizeRepository, productShopRepository);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("Testowa");
        productCategory.setActive(true);
        productCategory.setCategoryOrder(1);
        productCategoryRepository.save(productCategory);
        logger.info("Category saved");

        Brand brand = new Brand();
        brand.setName("Testowa");
        brand.setEmail("test@o2.pl");
        brandRepository.save(brand);
        logger.info("Brand saved");

        Product product = new Product();
        product.setProductCategory(productCategory);
        product.setBrand(brand);
        product.setPriceGross(BigDecimal.TEN);
        product.setProductName("Testowy");
        product.setProductDesc("Opis");
        product.setProductCode("121345");
        productRepository.save(product);
        logger.info("Product saved");

        ProductSize productSize = new ProductSize();
        productSize.setActive(true);
        productSize.setProductCategory(productCategory);
        productSize.setSizeName("111");
        productSizeRepository.save(productSize);
        logger.info("Product saved");

        Address address =  new Address();
        address.setCity("Test");
        address.setStreet("Test");
        address.setStreetNumber("1");
        address.setZipCode("00-000");
        addressRepository.save(address);
        logger.info("Address saved");

        Shop shop = new Shop();
        shop.setShopName("test");
        shop.setActive(true);
        shop.setEmail("test@o2.pl");
        shop.setPhone("123456789");
        shop.setShopCode("123");
        shop.setAddress(address);
        shopRepository.save(shop);
        logger.info("Shop saved");

        ProductShop productShop = new ProductShop();
        productShop.setShop(shop);
        productShop.setProduct(product);
        productShop.setProductSize(productSize);
        productShop.setQuantity(15);
        productShopRepository.save(productShop);
        assertEquals("Should be equal 15", 15,service.checkStockProductAvailableQuantity(product.getId(), productSize.getId()));
    }


}
