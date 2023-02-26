package com.haduc.beshop.service;

import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.CartIDKey;
import com.haduc.beshop.util.dto.request.user.CartRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;

public interface ICartService {
    MessageResponse addProductToCart(CartRequest cartRequest);

    void deleteById(CartIDKey id);
}
