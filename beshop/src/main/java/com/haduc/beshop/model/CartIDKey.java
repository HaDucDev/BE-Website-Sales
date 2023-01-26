package com.haduc.beshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CartIDKey implements Serializable {

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "product_id")
  private Integer productId;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CartIDKey that = (CartIDKey) o;
    return Objects.equals(userId, that.userId) && Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, productId);
  }


}
