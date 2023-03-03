package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.Product;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.ICartRepository;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.IOrderService;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;
import com.haduc.beshop.util.dto.response.user.GetProductBuyResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IProductRepository iProductRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ICartRepository iCartRepository;

    @Override
    public boolean checkProductOrderConfirmation(OrderConfirmationRequest orderConfirmationRequest) {
        List<Integer> productList = orderConfirmationRequest.getProductIdBuyList();// lay list id product
        productList.stream().map((id)->
            this.iProductRepository.findByProductIdAndIsDeleteFalse(id)
                    .orElseThrow(()-> new NotXException("Sanr phẩm này không tồn tại", HttpStatus.NOT_FOUND)))
                    .collect(Collectors.toList());
        return true;
    }

    @Override
    public GetLoadOrderComfirmResponse loadOrderComfirm(Integer userId) {

        List<Cart> cartOfUser = this.iCartRepository.findById_UserIdAndIsDeleteFalse(userId);//lay list cart
        // bien list cart thanh list  List<GetProductBuyResponse>

        List<GetProductBuyResponse> list = cartOfUser.stream().map((item)->{
            GetProductBuyResponse a = convertProductBuyResponse(item.getProduct(), item.getQuantity());
            return a;
        }).collect(Collectors.toList());

        GetLoadOrderComfirmResponse resp = new GetLoadOrderComfirmResponse();
        resp.setGetProductBuyResponseList(list);
        User user= this.iUserRepository.findByUserIdAndIsDeleteFalse(userId).orElseThrow(()->new NotXException("Không tìm thấy người dùng này", HttpStatus.NOT_FOUND));
        resp.setFullName(user.getFullName());
        resp.setPhone(user.getPhone());
        resp.setAddress(user.getAddress());
        return resp;
    }

    private Integer sellingPrice(Product product ){// gia ban san pham
        Integer price = product.getUnitPrice() - (product.getUnitPrice()*product.getDiscount())/100;
        return price;
    }

    private Integer totalSellingPrice(Product product, Integer quanity ){// tong gia 1 san pham theo so luong
        Integer price = sellingPrice(product)*quanity;
        return price;
    }

    // bien Produc thanh GetProductBuyResponse
    private GetProductBuyResponse convertProductBuyResponse(Product product, Integer quatity){
        GetProductBuyResponse response =  this.modelMapper.map(product,GetProductBuyResponse.class);
        response.setSellingPrice(sellingPrice(product));
        response.setQuantityBuy(quatity);
        response.setTotalMoney(totalSellingPrice(product,quatity));
        return response;
    }
}
