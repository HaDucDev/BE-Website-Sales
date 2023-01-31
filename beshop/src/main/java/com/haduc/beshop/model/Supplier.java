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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Supplier implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "supplier_id")
  private Integer supplierId;

  @Column(name = "supplier_name")
  @Nationalized
  private String supplierName;

  @Column(name = "supplier_image")
  private String supplierImage;

  @Column(name = "is_delete")
  private boolean isDelete = Boolean.FALSE;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier")
  @JsonIgnore
  private Set<Product> productSet;
}
