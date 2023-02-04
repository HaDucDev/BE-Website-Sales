package com.haduc.beshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Category implements Serializable {
  //sản phẩm gồm máy tính, chuột, bàn phím, tai nghe
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Integer categoryId;

  @Column(name = "category_name")
  @Nationalized
  private String categoryName;

  @Column(name = "is_delete")
  private boolean isDelete= Boolean.FALSE;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
  @JsonIgnore
  private Set<Product> productList;


  @Override
  public String toString() {
    return "CategoryEntity{" +
        "categoryId=" + categoryId +
        ", categoryName='" + categoryName + '\'' +
        ", productEntityList=" + productList +
        '}';
  }

}
