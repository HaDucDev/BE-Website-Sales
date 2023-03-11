package com.haduc.beshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "unique_username_constraint", columnNames = "username"),
    @UniqueConstraint(name = "unique_email_constraint2", columnNames = "email"),
    //@UniqueConstraint(name = "unique_phone_constraint2", columnNames = "phone")
})
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer userId;

  private String avatar;

  private String username;

  private String address;

  @Column(name = "full_name")
  @Nationalized
  private String fullName;// de anh xa mysql la nvarchar

  private String email;

  private String password;

  private String phone;

  @Column(name = "is_delete")
  private boolean isDelete = Boolean.FALSE;

  @Column(name = "token_reset_password")
  @JsonIgnore
  private String tokenResetPass;

  @Column(name = "expiration_time_token")
  @JsonIgnore
  @Temporal(TemporalType.TIMESTAMP)
  private Date expirationTimeToken;// thoi gian ma token doi mat khau het han

  @Column(name = "assignment", columnDefinition = "integer default 0")// gia tri mac dinh la 0 Integer.
  private Integer assignment;// shipper da duoc phan cong giao hang

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  @JsonIgnore
  private Set<Cart> cart;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  @JsonIgnore
  private Set<Order> orders;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  @JsonIgnore
  private Set<Reviews> reviews;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id", referencedColumnName = "role_id")
  private Role role;

}
