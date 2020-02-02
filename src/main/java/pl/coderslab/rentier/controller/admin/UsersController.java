package pl.coderslab.rentier.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/users")
public class UsersController {

    @GetMapping("")
    public String showUsers() {


        return "/admin/users";
    }


}
