package pl.coderslab.rentier.controller.user;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.service.LoginRegisterServiceImpl;
import pl.coderslab.rentier.utils.BCrypt;
import pl.coderslab.rentier.pojo.Login;
import pl.coderslab.rentier.entity.OrderType;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.OrderTypeRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.repository.UserRoleRepository;
import pl.coderslab.rentier.validation.UserBasicValidation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class RegisterController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private final OrderTypeRepository orderTypeRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final LoginRegisterServiceImpl registerService;


    public RegisterController(OrderTypeRepository orderTypeRepository,
                              UserRoleRepository userRoleRepository, UserRepository userRepository,
                              ProductCategoryRepository productCategoryRepository, LoginRegisterServiceImpl registerService) {
        this.orderTypeRepository = orderTypeRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public String registerStepOne(@ModelAttribute @Validated({UserBasicValidation.class}) User user, BindingResult resultUser,
                                  @ModelAttribute(binding = false) Login login, BindingResult resultLogin) {

        OrderType orderType = orderTypeRepository.findExternalOrderTypeIdByOrderTypeNameEquals("external");
        UserRole userRole = userRoleRepository.findByOrderTypeId(orderType.getId());
        user.setUserRole(userRole);

        user.setActive(true);
        user.setVerified(false);
        user.setRegisterDate(LocalDateTime.now());

        if (userRepository.existsByEmail(user.getEmail())) {
            resultUser.rejectValue("email", "error.emailExists", "Użytkownik o takim email już istnieje");

        }


        if (!user.getPassword().equals(user.getPassword2())) {
            resultUser.rejectValue("password", "error.passwordMatch", "Wprowadzono różne hasła");
        }

        if (resultUser.hasErrors()) {

            return "/login/login";

        } else {

            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userRepository.save(user);
            registerService.sendActivationEmail(user);
            return "/login/registerSuccess";
        }


    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
