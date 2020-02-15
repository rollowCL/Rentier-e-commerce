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
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.repository.UserRoleRepository;
import pl.coderslab.rentier.service.RegisterServiceImpl;
import pl.coderslab.rentier.utils.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@SessionAttributes({"loggedAdmin", "loggedUser", "loggedId", "loggedFirstName", "loggedLastName", "referer"})
@Controller
public class LoginController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final OrderTypeRepository orderTypeRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final RegisterServiceImpl registerService;


    public LoginController(OrderTypeRepository orderTypeRepository,
                           UserRoleRepository userRoleRepository, UserRepository userRepository,
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

        String referer = request.getHeader("Referer");
        model.addAttribute("referer", referer);
        logger.info("set: " + referer);
        return "/login/login";
    }

    @PostMapping("/login")
    public String logInUser(Model model, @ModelAttribute(binding = false) User user, BindingResult resultUser,
                            @ModelAttribute @Valid Login login, BindingResult resultLogin,
                            @SessionAttribute(value = "referer", required = false) String referer) {

        if (resultLogin.hasErrors()) {

            return "/login/login";

        } else {

            Optional<User> checkedUser = userRepository.findByEmailAndActiveTrueAndVerifiedTrue(login.getEmailLogin());

            if (checkedUser.isPresent() && BCrypt.checkpw(login.getPasswordLogin(), checkedUser.get().getPassword())) {

                model.addAttribute("loggedId", checkedUser.get().getId());
                model.addAttribute("loggedFirstName", checkedUser.get().getFirstName());
                model.addAttribute("loggedLastName", checkedUser.get().getLastName());

                if (checkedUser.get().getUserRole().getRoleCode().equals("admin")) {

                    model.addAttribute("loggedAdmin", 1);
                    return "redirect:/admin/config";

                } else {

                    model.addAttribute("loggedUser", 1);

                    if (referer.contains("register")) {
                        referer = null;
                    }

                    if (referer != null) {

                        return "redirect:" + referer;

                    } else {

                        return "redirect:/";
                    }

                }


            } else {

                model.addAttribute("message", "Nieprawidłowe dane logowania.");
                return "/login/login";

            }

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
