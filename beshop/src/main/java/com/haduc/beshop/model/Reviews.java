package com.haduc.beshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Reviews implements Serializable {
  @EmbeddedId
  private ReviewsIdKey id;

  @Nationalized
  private String comments;

  private Integer rating;// lượt đánh giá của khách hàng

  @Column(name = "created_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;// ngay tao comment

  @Column(name = "is_delete")
  private Boolean delete = Boolean.FALSE;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "orders_id", referencedColumnName = "orders_id", insertable = false, updatable = false, nullable = false)
  private Order order;// comment nay thuoc don hang nao day.
  @JsonIgnore
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn (name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = false)
  private Product product;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "user_id",referencedColumnName = "user_id", insertable = false, updatable = false, nullable = false)
  private User user;

}
