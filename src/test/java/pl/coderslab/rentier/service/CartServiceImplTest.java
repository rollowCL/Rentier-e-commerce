package pl.coderslab.rentier.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.rentier.InMemoryTestConfig;
import pl.coderslab.rentier.beans.Cart;
import pl.coderslab.rentier.entity.*;
import pl.coderslab.rentier.exception.ProductNotInCartException;
import pl.coderslab.rentier.exception.ProductQuantityExceededException;
import pl.coderslab.rentier.pojo.CartItem;
import pl.coderslab.rentier.repository.*;

import javax.annotation.Resource;
import java.math.BigDecimal;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {InMemoryTestConfig.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class CartServiceImplTest {

    CartServiceImpl service;
    Cart cart;
    CartItem cartItem;
    Shop shop;
    Address address;
    ProductCategory productCategory;
    Brand brand;
    Product product;
    Product product1;
    ProductSize productSize;
    ProductShop productShop;

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

    @Before
    public void setUpService() {
        service = new CartServiceImpl(productRepository, productSizeRepository, productShopRepository);
        cart = new Cart();
        cartItem = new CartItem();

        productCategory = new ProductCategory();
        productCategory.setCategoryName("Testowa");
        productCategory.setActive(true);
        productCategory.setCategoryOrder(1);
        productCategoryRepository.save(productCategory);
        logger.info("Category saved");

        brand = new Brand();
        brand.setName("Testowa");
        brand.setEmail("test@o2.pl");
        brandRepository.save(brand);
        logger.info("Brand saved");

        product = new Product();
        product.setProductCategory(productCategory);
        product.setBrand(brand);
        product.setPriceGross(BigDecimal.TEN);
        product.setProductName("Testowy");
        product.setProductDesc("Opis");
        product.setProductCode("121345");
        productRepository.save(product);
        logger.info("Product saved");

        product1 = new Product();
        product1.setProductCategory(productCategory);
        product1.setBrand(brand);
        product1.setPriceGross(BigDecimal.TEN);
        product1.setProductName("Testowy 1");
        product1.setProductDesc("Opis 1");
        product1.setProductCode("54321");
        productRepository.save(product1);
        logger.info("Product 1 saved");

        productSize = new ProductSize();
        productSize.setActive(true);
        productSize.setProductCategory(productCategory);
        productSize.setSizeName("111");
        productSizeRepository.save(productSize);
        logger.info("Product saved");

        address = new Address();
        address.setCity("Test");
        address.setStreet("Test");
        address.setStreetNumber("1");
        address.setZipCode("00-000");
        addressRepository.save(address);
        logger.info("Address saved");

        shop = new Shop();
        shop.setShopName("test");
        shop.setActive(true);
        shop.setEmail("test@o2.pl");
        shop.setPhone("123456789");
        shop.setShopCode("123");
        shop.setAddress(address);
        shopRepository.save(shop);
        logger.info("Shop saved");

        productShop = new ProductShop();
        productShop.setShop(shop);
        productShop.setProduct(product);
        productShop.setProductSize(productSize);
    }


    @Test(expected = ProductQuantityExceededException.class)
    public void should_addToCart_ThrowException_ItemNotInCart() throws ProductQuantityExceededException {

        productShop.setQuantity(1);
        productShopRepository.save(productShop);

        service.cartAdd(product.getId(), productSize.getId(), 6, cart);
    }

    @Test(expected = ProductQuantityExceededException.class)
    public void should_addToCart_ThrowException_ItemInCart() throws ProductQuantityExceededException {

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(5);
        cart.addToCart(cartItem);

        productShop.setQuantity(10);
        productShopRepository.save(productShop);

        service.cartAdd(product.getId(), productSize.getId(), 7, cart);
    }

    @Test
    public void should_addToCart_ResultOK_ItemNotInCart() throws ProductQuantityExceededException {
        int expectedQuantity = 4;

        productShop.setQuantity(5);
        productShopRepository.save(productShop);

        service.cartAdd(product.getId(), productSize.getId(), expectedQuantity, cart);
        assertNotEquals(-1, service.checkCartProductIndex(product.getId(), productSize.getId(), cart));
        assertEquals(expectedQuantity, service.checkCartProductQuantity(product.getId(), productSize.getId(), cart));
    }

    @Test
    public void should_addToCart_ResultOK_ItemNotInCart_MaxQuantity() throws ProductQuantityExceededException {
        int expectedQuantity = 5;

        productShop.setQuantity(expectedQuantity);
        productShopRepository.save(productShop);

        service.cartAdd(product.getId(), productSize.getId(), expectedQuantity, cart);
        assertEquals(expectedQuantity, service.checkCartProductQuantity(product.getId(), productSize.getId(), cart));
    }

    @Test
    public void should_addToCart_Increase_ItemQuantity_InCart() throws ProductQuantityExceededException {
        int inCartQuantity = 2;

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(inCartQuantity);
        cart.addToCart(cartItem);

        productShop.setQuantity(10);
        productShopRepository.save(productShop);

        int additionalQuantity = 3;
        service.cartAdd(product.getId(), productSize.getId(), additionalQuantity, cart);
        assertThat(service.checkCartProductQuantity(product.getId(), productSize.getId(), cart), is(inCartQuantity + additionalQuantity));
    }

    @Test
    public void should_cartRemove_ResultOK() throws ProductNotInCartException {
        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(5);
        cart.addToCart(cartItem);

        assertEquals(0, service.checkCartProductIndex(product.getId(), productSize.getId(), cart));
        service.cartRemove(product.getId(), productSize.getId(), cart);
        assertEquals(-1, service.checkCartProductIndex(product.getId(), productSize.getId(), cart));
    }

    @Test(expected = ProductNotInCartException.class)
    public void should_cartRemove_ThrowException() throws ProductNotInCartException {
        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(5);
        cart.addToCart(cartItem);

        service.cartRemove(product1.getId(), productSize.getId(), cart);

    }

    @Test
    public void should_checkCartProductIndex_Return_Not_Negative() {

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(1);
        cart.addToCart(cartItem);

        assertNotEquals(-1, service.checkCartProductIndex(product.getId(), productSize.getId(), cart));

    }

    @Test
    public void should_checkCartProductQuantity_Return10() {

        int expectedQuantity = 10;

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(expectedQuantity);
        cart.addToCart(cartItem);

        assertEquals("should be equal 10", expectedQuantity, service.checkCartProductQuantity(product.getId(), productSize.getId(), cart));

    }


    @Test
    public void should_checkStockProductAvailableQuantity_Return15() {
        int expectedQuantity = 15;

        productShop.setQuantity(expectedQuantity);
        productShopRepository.save(productShop);

        assertEquals("Should be equal " + expectedQuantity, expectedQuantity, service.checkStockProductAvailableQuantity(product.getId(), productSize.getId()));
    }

    @Test(expected = NullPointerException.class)
    public void should_checkStockProductAvailableQuantity_ThrowException() {

        assertEquals("Should be equal " + 0, 0, service.checkStockProductAvailableQuantity(product.getId(), productSize.getId()));
    }

    @Test
    public void should_cartItems_have_2items() {

        cartItem.setProduct(product);
        cartItem.setProductSize(productSize);
        cartItem.setQuantity(1);
        cart.addToCart(cartItem);

        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(product1);
        cartItem1.setProductSize(productSize);
        cartItem1.setQuantity(1);
        cart.addToCart(cartItem1);

        CartItem[] arrayOfItems = {cartItem, cartItem1};

        assertArrayEquals(arrayOfItems, cart.getCartItems().toArray());

    }


}
