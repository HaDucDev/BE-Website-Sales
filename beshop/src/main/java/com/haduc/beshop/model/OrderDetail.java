package com.haduc.beshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "order_detail")
public class OrderDetail implements Serializable {

  @EmbeddedId
  private OrderDetailIDKey id;

  private Integer quantity;

  private Long amount;

  @Column(name = "is_delete")
  private boolean isDelete;

  private String isReview;// trạng thái đánh giá sản phẩm được mua

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "orders_id", referencedColumnName = "orders_id", insertable = false, updatable = false, nullable = false)
  @JsonBackReference
  @JsonIgnore
  private Order order;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = false)
  @JsonBackReference
  private Product product;

}
