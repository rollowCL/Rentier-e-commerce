package pl.coderslab.rentier.controller.user;

import com.google.common.net.HttpHeaders;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;
import pl.coderslab.rentier.utils.BCrypt;
import pl.coderslab.rentier.pojo.Login;
import pl.coderslab.rentier.entity.OrderType;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.OrderTypeRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.repository.UserRoleRepository;
import pl.coderslab.rentier.utils.EmailUtil;
import pl.coderslab.rentier.validation.UserBasicValidation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SessionAttributes({"loggedAdmin", "loggedUser", "loggedId", "loggedFirstName", "loggedLastName", "referer"})
@Controller
public class LoginRegisterController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginRegisterController.class);
    private final OrderTypeRepository orderTypeRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public LoginRegisterController(OrderTypeRepository orderTypeRepository,
                                   UserRoleRepository userRoleRepository, UserRepository userRepository,
                                   ProductCategoryRepository productCategoryRepository) {
        this.orderTypeRepository = orderTypeRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
    }


    @GetMapping("/login")
    public String showLoginRegister(Model model,
                                    HttpServletRequest request) {

        Login login = new Login();
        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("login", login);

        String referer = request.getHeader("Referer");
        model.addAttribute("referer", referer);
        logger.info("set: " + referer);
        return "/login/login";
    }

    @PostMapping("/login")
    public String logInUser(Model model, @ModelAttribute(binding = false) User user, BindingResult resultUser,
                            @ModelAttribute @Valid Login login, BindingResult resultLogin,
                            @SessionAttribute(value = "referer", required = false) String referer) {
        logger.info("read: " + referer);
        if (resultLogin.hasErrors()) {

            return "/login/login";

        } else {

            Optional<User> checkedUser = userRepository.findByEmailAndActiveTrue(login.getEmail());

            if (checkedUser.isPresent() && BCrypt.checkpw(login.getPassword(), checkedUser.get().getPassword())) {

                model.addAttribute("loggedId", checkedUser.get().getId());
                model.addAttribute("loggedFirstName", checkedUser.get().getFirstName());
                model.addAttribute("loggedLastName", checkedUser.get().getLastName());

                if (checkedUser.get().getUserRole().getRoleCode().equals("admin")) {

                    model.addAttribute("loggedAdmin", 1);
                    return "redirect:/admin/config";

                } else {

                    model.addAttribute("loggedUser", 1);

                    if (referer != null) {
                        return "redirect:" + referer;
                    } else {
                        return "/";
                    }

                }


            } else {

                model.addAttribute("message", "Nieprawidłowe dane logowania");
                return "/login/login";

            }

        }

    }


    @PostMapping("/register")
    public String registerStepOne(@ModelAttribute @Validated({UserBasicValidation.class}) User user, BindingResult resultUser,
                                  @ModelAttribute(binding = false) Login login, BindingResult resultLogin) {

        OrderType orderType = orderTypeRepository.findExternalOrderTypeIdByOrderTypeNameEquals("external");
        UserRole userRole = userRoleRepository.findByOrderTypeId(orderType.getId());
        user.setUserRole(userRole);

        user.setActive(true);
        user.setVerified(true);
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

            return "/login/registerSuccess";
        }


    }

    @GetMapping("/loginSuccess")
    public String showLoginSuccess(@SessionAttribute("loggedId") Long id) {

        return "/login/loginSuccess";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
