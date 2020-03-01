package pl.coderslab.rentier.controller.user;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.RentierProperties;
import pl.coderslab.rentier.entity.OrderType;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.service.RegisterServiceImpl;
import pl.coderslab.rentier.validation.UserPasswordValidation;

import java.time.LocalDateTime;
import java.util.List;

//@SessionAttributes({"loggedAdmin", "loggedUser", "loggedId", "loggedFirstName", "loggedLastName", "referer"})
@Controller
public class ForgotPasswordController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final RegisterServiceImpl registerService;
    private final RentierProperties rentierProperties;


    public ForgotPasswordController(UserRepository userRepository,
                                    ProductCategoryRepository productCategoryRepository, RegisterServiceImpl registerService, RentierProperties rentierProperties) {
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.registerService = registerService;
        this.rentierProperties = rentierProperties;
    }


    @GetMapping("/forgotpassword")
    public String showEmailForm() {

        return "/login/forgot";
    }


    @PostMapping("/forgotpassword")
    public String remindPassword(@RequestParam String email) {

        if (userRepository.existsByEmail(email)) {

            registerService.resetPasswordProcess(email);


        }

        return "/login/forgotSuccess";
    }

    @GetMapping("/resetpassword")
    public String showResetForm(@RequestParam String token, Model model) {

        if (registerService.validateToken(token, rentierProperties.getTokenTypePasswordReset())) {

            registerService.invalidateToken(token);

            User user = new User();
            user.setId(registerService.getUserForToken(token).getId());
            model.addAttribute("user", user);
            return "/login/resetPassword";
        }

        return "/login/resetError";
    }


    @PostMapping("/resetpassword")
    public String processReset(@ModelAttribute @Validated({UserPasswordValidation.class}) User user, BindingResult resultUser) {

        if (!user.getPassword().equals(user.getPassword2())) {
            resultUser.rejectValue("password", "error.passwordMatch", "Wprowadzono różne hasła");
        }

        if (resultUser.hasErrors()) {

            return "/login/resetPassword";

        } else {

            registerService.updateUserPassword(user);
            return "/login/resetSuccess";
        }
    }


    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
