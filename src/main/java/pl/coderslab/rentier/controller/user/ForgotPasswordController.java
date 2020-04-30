package pl.coderslab.rentier.controller.user;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.service.ForgotPasswordServiceImpl;
import pl.coderslab.rentier.service.TokenServiceImpl;
import pl.coderslab.rentier.service.UserServiceImpl;
import pl.coderslab.rentier.validation.UserPasswordValidation;

import java.util.List;

@Controller
public class ForgotPasswordController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ForgotPasswordServiceImpl forgotPasswordService;
    private final TokenServiceImpl tokenService;
    private final UserServiceImpl userService;

    @Value("${rentier.tokenTypePasswordReset}")
    private int tokenTypePasswordReset;

    public ForgotPasswordController(UserRepository userRepository,
                                    ProductCategoryRepository productCategoryRepository,
                                    ForgotPasswordServiceImpl forgotPasswordService,
                                    TokenServiceImpl tokenService, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.forgotPasswordService = forgotPasswordService;
        this.tokenService = tokenService;
        this.userService = userService;
    }


    @GetMapping("/forgotpassword")
    public String showEmailForm() {

        return "/login/forgot";
    }


    @PostMapping("/forgotpassword")
    public String remindPassword(@RequestParam String email) {

        if (userRepository.existsByEmail(email)) {

            forgotPasswordService.resetPasswordProcess(email);


        }

        return "/login/forgotSuccess";
    }

    @GetMapping("/resetpassword")
    public String showResetForm(@RequestParam String token, Model model) {

        if (tokenService.validateToken(token, tokenTypePasswordReset)) {

            tokenService.invalidateToken(token);

            User user = new User();
            user.setId(tokenService.getUserForToken(token).getId());
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

            userService.updateUserPassword(user);
            return "/login/resetSuccess";
        }
    }


    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
