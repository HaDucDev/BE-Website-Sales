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
import com.haduc.beshop.util.dto.request.user.CheckAndUpdateProductBuyRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<Cart> getAllProductFromCartByUserId(Integer userId) {
        return this.iCartRepository.findById_UserIdAndIsDeleteFalse(userId);
    }


    @Override
    public Integer countById_UserIdAndIsDeleteFalse(Integer userId) {
        Long count = this.iCartRepository.countById_UserIdAndIsDeleteFalse(userId);
        Integer convertCount = Integer.valueOf(count.toString());
        return convertCount;
    }

    @Transactional
    @Override
    public MessageResponse addProductToCart(CartRequest cartRequest) {

        // tim kiem xem san pham va nguoi dung ton tai ko
        //User user =this.iUserRepository.findByUserIdAndIsDeleteFalse(cartRequest.getUserId()).orElseThrow(()-> new  NotXException("Id người dùng lỗi", HttpStatus.NOT_FOUND));
        //Product product= this.iProductRepository.findByProductIdAndIsDeleteFalse(cartRequest.getProductId()).orElseThrow(()-> new  NotXException("Id sản phẩm lỗi", HttpStatus.NOT_FOUND));

        CartIDKey cartIDKey= new CartIDKey(cartRequest.getUserId(), cartRequest.getProductId());

        //kiem tra xme san pham them da co trong gio chua
        Optional<Cart> cart = this.iCartRepository.findById(cartIDKey);

        Product product = this.iProductRepository.findByProductIdAndIsDeleteFalse(cartRequest.getProductId()).orElseThrow(()-> new  NotXException("Id sản phẩm lỗi", HttpStatus.NOT_FOUND));

        if(cartRequest.getQuantity() > product.getQuantity()){// dung o trang chi tiet don hang
            throw new NotXException("Só sản phẩm đặt mua không được lớn hơn số lượng sản phẩm đang có", HttpStatus.BAD_REQUEST);
        }

        if (!cart.isPresent()) {// neu la null - khong co trong gio
            Cart cartNew = new Cart();
            cartNew.setId(cartIDKey);
            cartNew.setQuantity(cartRequest.getQuantity());
            cartNew.setUser(this.iUserRepository.findByUserIdAndIsDeleteFalse(cartRequest.getUserId()).orElseThrow(()-> new  NotXException("Id người dùng lỗi", HttpStatus.NOT_FOUND)));
            cartNew.setProduct(product);
            Cart saveCart =  this.iCartRepository.save(cartNew);
            return new MessageResponse(String.format("Sản phẩm có id là %s được thêm vào giỏ thành công!", saveCart.getId().getProductId().toString()));
        }

        // neu san pham co trong gio. can xet san pham nay da mua hay chua. Mua roi thi iDelete la false
        Cart oldCart = cart.get();
        if (oldCart.isDelete()) {// trang thai true da bi xoa mem
            oldCart.setQuantity(cartRequest.getQuantity());
            oldCart.setDelete(false);
        }
        else {
            if(cartRequest.getOperator().equals("add")){
                oldCart.setQuantity(cart.get().getQuantity() + cartRequest.getQuantity());
            }
            if(cartRequest.getOperator().equals("sub")){
                oldCart.setQuantity(cart.get().getQuantity() - cartRequest.getQuantity());
            }
        }
        Cart saveCartSecond =  this.iCartRepository.save(oldCart);
        return new MessageResponse(String.format("Sản phẩm có id là %s được thêm vào giỏ thành công!",saveCartSecond.getId().getProductId().toString()));
    }

    @Transactional
    @Override
    public void deleteById(CartIDKey id) {
        int affectedRows = this.iCartRepository.deleteProductFromCart(id);
        System.out.println(affectedRows);
        if (affectedRows == 0) {
            throw new NotXException("Xảy ra lỗi khi xóa sản phẩm khỏi giỏ hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean checkAndUpdateProductBuy(CheckAndUpdateProductBuyRequest checkAndUpdateProductBuyRequest) {
        Product product = this.iProductRepository.findByProductIdAndIsDeleteFalse(checkAndUpdateProductBuyRequest.getProductId()).orElseThrow(()-> new NotXException("Sản phẩm này không tìm thấy", HttpStatus.INTERNAL_SERVER_ERROR));
        if(checkAndUpdateProductBuyRequest.getQuantity() <= product.getQuantity()){
            
            CartIDKey cartIDKey = new CartIDKey(checkAndUpdateProductBuyRequest.getUserId(),checkAndUpdateProductBuyRequest.getProductId());
            Cart cart = this.iCartRepository.findByIdAndIsDeleteFalse(cartIDKey);
            cart.setQuantity(checkAndUpdateProductBuyRequest.getQuantity());
            return true;
        }
        else throw new NotXException("Còn " + product.getQuantity() +" sản phẩm",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
