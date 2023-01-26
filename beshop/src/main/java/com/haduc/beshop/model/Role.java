package com.haduc.beshop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haduc.beshop.util.enum_role.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Role implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private int id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20, unique = true)
  private ERole name;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
  @JsonIgnore
  private Set<User> users;

}
