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
public class ReviewsIdKey implements Serializable {
  @Column(name = "orders_id")
  private Integer ordersId;

  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "user_id")
  private Integer userId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReviewsIdKey that = (ReviewsIdKey) o;
    return Objects.equals(ordersId, that.ordersId) && Objects.equals(productId, that.productId) && Objects.equals(userId,
        that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ordersId, productId, userId);
  }
}
