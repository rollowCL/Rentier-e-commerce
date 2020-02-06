package pl.coderslab.rentier.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.entity.Shop;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.entity.UserRole;
import pl.coderslab.rentier.repository.ShopRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ShopRepository shopRepository;

    public UserController(UserRepository userRepository, UserRoleRepository userRoleRepository, ShopRepository shopRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.shopRepository = shopRepository;
    }


    @GetMapping("")
    public String showUsers(Model model) {

        UserRole userRoleFilter = new UserRole();
        userRoleFilter.setId(0L);

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

    @PostMapping("/filterUsersName")
    public String showFilteredUsersByName(Model model, @RequestParam String userNameSearch,
                                          @ModelAttribute(binding = false, name = "userRoleFilter") UserRole userRoleFilter,
                                          BindingResult result) {

        List<User> foundUsers = userRepository.findByLastNameContainingIgnoreCase(userNameSearch);

        model.addAttribute("users", foundUsers);

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

        return "/admin/userChangeRole";

    }

    @PostMapping("/changeRole")
    public String changeUserRole(@RequestParam String userId, @RequestParam String newRoleId) {

            userRepository.customUpdateUserRole(Long.parseLong(userId), Long.parseLong(newRoleId));
            return "redirect:/admin/users";


    }

    @GetMapping("/shops")
    public String showUserShops(Model model, @RequestParam Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {

            if (!user.get().getUserRole().getOrderType().getOrderTypeName().equals("internal")) {

                return "redirect:/admin/users";

            } else {

                model.addAttribute("user", user.get());
                return "/admin/userShops";

            }

        } else {

            return "redirect:/admin/users";
        }

    }

    @PostMapping("/shops")
    public String updateUserShops(Model model, @RequestParam Long userId, @RequestParam List<Long> userShops) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {

            user.get().setShops(new ArrayList<>());

            if (userShops.size() > 0) {
                List<Shop> userNewShops = user.get().getShops();

                for (Long shopId: userShops) {
                    Optional<Shop> shop = shopRepository.findById(shopId);

                    if (shop.isPresent()) {
                        userNewShops.add(shop.get());
                    }

                }

            }

            userRepository.save(user.get());

        }

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
