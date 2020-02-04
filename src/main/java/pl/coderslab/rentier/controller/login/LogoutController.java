package pl.coderslab.rentier.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.coderslab.rentier.entity.ProductCategory;
import pl.coderslab.rentier.repository.ProductCategoryRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LogoutController {

    private final ProductCategoryRepository productCategoryRepository;

    public LogoutController(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "/login/logoutSuccess";
    }

    @ModelAttribute("productCategories")
    public List<ProductCategory> getProductCategories() {

        return productCategoryRepository.findProductCategoriesByActiveTrueOrderByCategoryOrder();
    }

}
