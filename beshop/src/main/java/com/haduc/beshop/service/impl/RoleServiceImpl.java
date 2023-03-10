package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.Role;
import com.haduc.beshop.repository.IRoleRepository;
import com.haduc.beshop.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleRepository iRoleRepository;

    @Override
    public List<Role> getAllRole() {
        return this.iRoleRepository.findAll();
    }
}
