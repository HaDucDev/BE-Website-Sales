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
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`order`")
public class Order implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orders_id", nullable = false)
  private Integer ordersId;

  @Column(name = "receipt_user")
  @Nationalized
  private String receiptUser;

  @Nationalized
  private String address;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "total_amount")
  private Long totalAmount;

  @Nationalized
  private String note;// thanh toan hay chua


  @Column(name = "status_order")
  @Nationalized
  private String statusOrder;

//  @Column(name = "is_delete")
//  private boolean isDelete= Boolean.FALSE;

  @Column(name = "shipper_id")
  private Integer shipperId;

  @Column(name = "created_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;// ngay tao don hang

  @Column(name = "received_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date receivedDate;// ngay nhan don hang

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
  @JsonIgnore
  private Set<OrderDetail> orderDetailEntities;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
  @JsonIgnore
  private Set<Reviews> reviewsEntities;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "user_id")
  private User user;

}
