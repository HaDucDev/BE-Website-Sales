package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.CartIDKey;
import com.haduc.beshop.model.Product;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.ICartRepository;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.ICartService;
import com.haduc.beshop.util.dto.request.user.CartRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {


    @Autowired
    private ICartRepository iCartRepository;


    @Autowired
    private IUserRepository iUserRepository;


    @Autowired
    private IProductRepository iProductRepository;


    @Override
    public MessageResponse addProductToCart(CartRequest cartRequest) {

        CartIDKey cartIDKey= new CartIDKey(cartRequest.getUserId(), cartRequest.getProductId());

        //kiem tra xme san pham them da co trong gio chua
        Optional<Cart> cart = this.iCartRepository.findById(cartIDKey);

        // tim kiem xem san pham va nguoi dung ton tai ko
        User user =this.iUserRepository.findByUserIdAndIsDeleteFalse(cartRequest.getUserId()).orElseThrow(()-> new  NotXException("Id người dùng lỗi", HttpStatus.NOT_FOUND));
        Product product= this.iProductRepository.findByProductIdAndIsDeleteFalse(cartRequest.getProductId()).orElseThrow(()-> new  NotXException("Id sản phẩm lỗi", HttpStatus.NOT_FOUND));

        if (!cart.isPresent()) {// neu la null - khong co trong gio
            Cart cartNew = new Cart();
            cartNew.setId(cartIDKey);
            cartNew.setQuantity(cartRequest.getQuantity());
            cartNew.setUser(user);
            cartNew.setProduct(product);
            Cart saveCart =  this.iCartRepository.save(cartNew);
            return new MessageResponse(String.format("Sản phẩm có id là %s được thêm vào giỏ thành công!", saveCart.getId().getProductId().toString()));
        }

        // neu san pham co trong gio. can xet san pham nay da mua hay chua. Mua roi thi iDelete la false
        Cart oldCart = cart.get();
        if (oldCart.isDelete()) {
            oldCart.setQuantity(cartRequest.getQuantity());
            oldCart.setDelete(false);
        }
        else oldCart.setQuantity(cart.get().getQuantity() + cartRequest.getQuantity());
        oldCart.setUser(user);
        oldCart.setProduct(product);
        Cart saveCartSecond =  this.iCartRepository.save(oldCart);
        return new MessageResponse(String.format("Sản phẩm có id là %s được thêm vào giỏ thành công!",saveCartSecond.getId().getProductId().toString()));
    }
}
