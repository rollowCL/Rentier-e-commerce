package pl.coderslab.rentier.controller.user;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.pojo.Login;
import pl.coderslab.rentier.repository.OrderTypeRepository;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.RoleRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.service.RegisterServiceImpl;
import pl.coderslab.rentier.utils.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
public class LoginController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final OrderTypeRepository orderTypeRepository;
    private final RoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final RegisterServiceImpl registerService;


    public LoginController(OrderTypeRepository orderTypeRepository,
                           RoleRepository userRoleRepository, UserRepository userRepository,
                           ProductCategoryRepository productCategoryRepository, RegisterServiceImpl registerService) {
        this.orderTypeRepository = orderTypeRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.registerService = registerService;
    }


    @GetMapping("/login")
    public String showLoginRegister(Model model,
                                    HttpServletRequest request) {

        Login login = new Login();
        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("login", login);

        return "login/login";
    }


    @GetMapping("/loginError")
    public String showLoginError() {

        return "login/loginError";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
