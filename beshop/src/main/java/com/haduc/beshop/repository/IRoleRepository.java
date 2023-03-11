package com.haduc.beshop.repository;


import com.haduc.beshop.model.Role;
import com.haduc.beshop.util.enum_role.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findById(Integer roleId);// use java 8

    Optional<Role> findByName(ERole role);// use java 8

}
