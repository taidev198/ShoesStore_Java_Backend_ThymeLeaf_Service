package com.taidev198.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "constants")
public class Constant extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private String value;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> categoryProducts;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private List<Product> materialProducts;

    @OneToMany(mappedBy = "style", fetch = FetchType.LAZY)
    private List<ProductDetail> styleProducts;

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY)
    private List<ProductDetail> colorProducts;

    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    private List<ProductQuantity> sizeProducts;
}
