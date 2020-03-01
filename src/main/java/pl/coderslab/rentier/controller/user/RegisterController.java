package pl.coderslab.rentier.controller.user;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.service.RegisterServiceImpl;
import pl.coderslab.rentier.pojo.Login;
import pl.coderslab.rentier.entity.OrderType;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.OrderTypeRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.repository.UserRoleRepository;
import pl.coderslab.rentier.validation.UserBasicValidation;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class RegisterController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private final OrderTypeRepository orderTypeRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final RegisterServiceImpl registerService;
    private final RentierProperties rentierProperties;


    public RegisterController(OrderTypeRepository orderTypeRepository,
                              UserRoleRepository userRoleRepository, UserRepository userRepository,
                              ProductCategoryRepository productCategoryRepository, RegisterServiceImpl registerService, RentierProperties rentierProperties) {
        this.orderTypeRepository = orderTypeRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.registerService = registerService;
        this.rentierProperties = rentierProperties;
    }

    @PostMapping("/register")
    public String registerStepOne(@ModelAttribute @Validated({UserBasicValidation.class}) User user, BindingResult resultUser,
                                  @ModelAttribute(binding = false) Login login, BindingResult resultLogin) {


        if (userRepository.existsByEmail(user.getEmail())) {
            resultUser.rejectValue("email", "error.emailExists", "Użytkownik o takim email już istnieje");

        }


        if (!user.getPassword().equals(user.getPassword2())) {
            resultUser.rejectValue("password", "error.passwordMatch", "Wprowadzono różne hasła");
        }

        if (resultUser.hasErrors()) {

            return "/login/login";

        } else {

            OrderType orderType = orderTypeRepository.findExternalOrderTypeIdByOrderTypeNameEquals("external");
            UserRole userRole = userRoleRepository.findByOrderTypeId(orderType.getId());
            user.setUserRole(userRole);
            user.setActive(true);
            user.setVerified(false);
            user.setRegisterDate(LocalDateTime.now());
            registerService.registerUser(user);
            return "/login/registerSuccess";
        }


    }

    @GetMapping("/activate")
    public String activate(@RequestParam String token) {

        if (registerService.validateToken(token, rentierProperties.getTokenTypeActivation())) {

            registerService.invalidateToken(token);
            registerService.makeUserVerified(token);
            return "/login/activationSuccess";
        }

        return "/login/activationError";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}