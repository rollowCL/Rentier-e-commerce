package pl.coderslab.rentier.controller.admin;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.rentier.service.ProductServiceImpl;

@RestController
@RequestMapping("/image")
public class ImageRestController {
    private final ProductServiceImpl productService;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ImageRestController.class);

    public ImageRestController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @DeleteMapping("/del/{imageId}")
    public void deleteImage(@PathVariable("imageId") Long imageId) {

    logger.info("REST DELETE: " + imageId);
    productService.restDeleteImage(imageId);
    }

    @PutMapping("/main/{imageId}")
    public void makeMainImage(@PathVariable("imageId") Long imageId) {

        logger.info("REST PUT: " + imageId);
        productService.restMakeMainImage(imageId);
    }

}




