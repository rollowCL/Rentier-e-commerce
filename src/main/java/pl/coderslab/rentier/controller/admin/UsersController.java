package pl.coderslab.rentier.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.ProductSize;
import pl.coderslab.rentier.entity.Shop;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.ShopRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.repository.UserRoleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/users")
public class UsersController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ShopRepository shopRepository;

    public UsersController(UserRepository userRepository, UserRoleRepository userRoleRepository, ShopRepository shopRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.shopRepository = shopRepository;
    }


    @GetMapping("")
    public String showUsers(Model model) {

        UserRole userRoleFilter = new UserRole();

        model.addAttribute("userRoleFilter", userRoleFilter);

        return "/admin/users";
    }

    @PostMapping("/filterUsers")
    public String showFilteredUsers(Model model, @ModelAttribute("userRoleFilter") UserRole userRoleFilter, BindingResult result) {

        if (userRoleFilter.getId() == 0) {

            model.addAttribute("users", userRepository.findAll());

        } else {

            model.addAttribute("users", userRepository.findByUserRoleId(userRoleFilter.getId()));
        }

        return "/admin/users";
    }


    @GetMapping("/change")
    public String changeUserStatus(Model model, @RequestParam Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {

            if (user.get().isActive()) {
                user.get().setActive(false);
            } else {
                user.get().setActive(true);
            }

            userRepository.save(user.get());

        }

        return "redirect:/admin/users";

    }

    @GetMapping("/changeRole")
    public String changeUserRoleForm(Model model, @RequestParam Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        }

        return "/admin/changeRole";

    }

    @PostMapping("/changeRole")
    public String changeUserRole(@RequestParam String userId, @RequestParam String newRoleId) {

            userRepository.customUpdateUserRole(Long.parseLong(userId), Long.parseLong(newRoleId));
            return "redirect:/admin/users";


    }



    @ModelAttribute("userRoles")
    public List<UserRole> getUserRoles() {

        return userRoleRepository.findAll();
    }

    @ModelAttribute("users")
    public List<User> getUsers() {

        return userRepository.findAll();
    }

    @ModelAttribute("shops")
    public List<Shop> getShops() {

        return shopRepository.findAll();
    }
}
