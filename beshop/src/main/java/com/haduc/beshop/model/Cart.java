package com.haduc.beshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Cart implements Serializable {
  @EmbeddedId
  private CartIDKey id;

  private Integer quantity;


  @Column(name = "is_delete")
  private boolean isDelete= Boolean.FALSE;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id",referencedColumnName = "user_id", insertable = false, updatable = false, nullable = false)
  private User user;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id",referencedColumnName = "product_id", insertable = false, updatable = false, nullable = false)
  private Product product;

}
