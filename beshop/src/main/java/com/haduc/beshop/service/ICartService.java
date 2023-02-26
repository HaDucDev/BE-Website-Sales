package com.haduc.beshop.service;

import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.CartIDKey;
import com.haduc.beshop.util.dto.request.user.CartRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;

import java.util.List;

public interface ICartService {

    List<Cart> getAllProductFromCartByUserId(Integer userId);//lay san pham chua xoa mem trong gio nguoi dung
    MessageResponse addProductToCart(CartRequest cartRequest);

    void deleteById(CartIDKey id);
}
