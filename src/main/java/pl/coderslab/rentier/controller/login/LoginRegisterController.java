package pl.coderslab.rentier.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.utils.BCrypt;
import pl.coderslab.rentier.pojo.Login;
import pl.coderslab.rentier.entity.OrderType;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.OrderTypeRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.repository.UserRoleRepository;
import pl.coderslab.rentier.validation.UserBasicValidation;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@SessionAttributes({"loggedAdmin", "loggedUser", "loggedId", "loggedFirstName", "loggedLastName"})
@Controller
public class LoginRegisterController {

    private final BCrypt bCrypt;
    private final OrderTypeRepository orderTypeRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    public LoginRegisterController(BCrypt bCrypt, OrderTypeRepository orderTypeRepository,
                                   UserRoleRepository userRoleRepository, UserRepository userRepository) {
        this.bCrypt = bCrypt;
        this.orderTypeRepository = orderTypeRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/login")
    public String showLoginRegister(Model model) {

        Login login = new Login();
        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("login", login);

        return "/login/login";
    }

    @PostMapping("/register")
    public String registerStepOne(@ModelAttribute @Validated({UserBasicValidation.class}) User user, BindingResult resultUser,
                                  @ModelAttribute (binding = false) Login login, BindingResult resultLogin) {

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

    @PostMapping("/login")
    public String logInUser(Model model, @ModelAttribute (binding = false) User user, BindingResult resultUser,
                            @ModelAttribute @Valid Login login, BindingResult resultLogin) {

        if (resultLogin.hasErrors()) {

            return "/login/login";

        } else {

            Optional<User> checkedUser = userRepository.findByEmailAndActiveTrue(login.getEmail());

            if (checkedUser.isPresent() && bCrypt.checkpw(login.getPassword(), checkedUser.get().getPassword())) {

                model.addAttribute("loggedId", checkedUser.get().getId());
                model.addAttribute("loggedFirstName", checkedUser.get().getFirstName());
                model.addAttribute("loggedFirstName", checkedUser.get().getLastName());

                if (checkedUser.get().getUserRole().getRoleCode().equals("admin")) {

                    model.addAttribute("loggedAdmin", 1);
                    return "redirect:/admin/config";

                } else {

                    model.addAttribute("loggedUser", 1);

                    return "redirect:/loginSuccess";
                }


            } else {

                model.addAttribute("message", "Nieprawidłowe dane logowania");
                return "/login/login";

            }

        }

    }

    @GetMapping("/loginSuccess")
    public String showLoginSuccess(@SessionAttribute("loggedId") Long id) {

        return "/login/loginSuccess";
    }

}
