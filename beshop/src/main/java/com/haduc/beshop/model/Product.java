package com.haduc.beshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Product implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "product_name")
  @Nationalized
  private String productName;

  private Integer quantity;

  @Column(name = "product_image")
  private String productImage;

  private Integer discount;

  @Column(name = "unit_price")
  private Integer unitPrice;


  @Column(name = "description_product")
  @Nationalized
  private String descriptionProduct;

  @Column(name = "is_delete")
  private boolean isDelete;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "category_id")
  @JsonBackReference
  private Category category;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "supplier_id")
  @JsonBackReference
  private Supplier supplier;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  @JsonIgnore
  private Set<Cart> cartEntities;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
  @JsonBackReference
  @JsonIgnore
  private Set<OrderDetail> productEntities;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
  @JsonManagedReference
  @JsonIgnore
  private Set<Reviews> reviewsEntities;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Product product = (Product) o;

    return Objects.equals(productId, product.productId);
  }

  @Override
  public int hashCode() {
    return productId != null ? productId.hashCode() : 0;
  }
}
