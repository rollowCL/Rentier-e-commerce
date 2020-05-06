package pl.coderslab.rentier.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Size(max = 255)
    @Column(name = "image_file_name")
    private String imageFileName;

    @NotNull
    @Column(name = "main_image")
    private boolean mainImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public boolean isMainImage() {
        return mainImage;
    }

    public void setMainImage(boolean mainImage) {
        this.mainImage = mainImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductImage that = (ProductImage) o;
        return mainImage == that.mainImage &&
                Objects.equals(id, that.id) &&
                Objects.equals(product, that.product) &&
                Objects.equals(imageFileName, that.imageFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, imageFileName, mainImage);
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", imageFileName='" + imageFileName + '\'' +
                ", main_image=" + mainImage +
                '}';
    }
}
