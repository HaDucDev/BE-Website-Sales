package com.haduc.beshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haduc.beshop.service.IAccountService;
import com.haduc.beshop.util.dto.request.account.RegisterRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j // log lombok
@SpringBootTest
@AutoConfigureMockMvc // để sử dụng mockMvc
public class UserControllerTest {


    private RegisterRequest request;

    // dang ki response
    private MessageResponse userResponse;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAccountService iAccountService;

    @BeforeEach
    void initData() {

        request = RegisterRequest.builder()
                .email("longca@gmail.com")
                .fullName("Long ca")
                .username("long12345")
                .address("adadadada")
                .phone("012345698")
                .build();


        userResponse =MessageResponse.builder()
                .message("User Long ca đã tạo tài khoản thành công!")
                .build();
    }

    @Test
    void createUser() throws Exception {
        log.info("Hello test");
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(iAccountService.register(ArgumentMatchers.any())).thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("User Long ca đã tạo tài khoản thành công!"));
    }
}
