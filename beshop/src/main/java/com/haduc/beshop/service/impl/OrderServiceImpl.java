package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.*;
import com.haduc.beshop.repository.*;
import com.haduc.beshop.service.IOrderService;
import com.haduc.beshop.util.ConstantValue;
import com.haduc.beshop.util.dto.request.user.CreateOrderResquest;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;
import com.haduc.beshop.util.dto.response.user.GetProductBuyResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    private IOrderDetailRepository iOrderDetailRepository;

    @Override
    public boolean checkProductOrderConfirmation(OrderConfirmationRequest orderConfirmationRequest) {
        List<Integer> productList = orderConfirmationRequest.getProductIdBuyList();// lay list id product
        productList.stream().map((id) ->
                        this.iProductRepository.findByProductIdAndIsDeleteFalse(id)
                                .orElseThrow(() -> new NotXException("Sanr phẩm này không tồn tại", HttpStatus.NOT_FOUND)))
                .collect(Collectors.toList());
        return true;
    }

    // get thong tin de xac nhan don hang
    @Override
    public GetLoadOrderComfirmResponse loadOrderComfirm(Integer userId) {

        List<Cart> cartOfUser = this.iCartRepository.findById_UserIdAndIsDeleteFalse(userId);//lay list cart
        // bien list cart thanh list  List<GetProductBuyResponse>

        List<GetProductBuyResponse> list = cartOfUser.stream().map((item) -> {
            GetProductBuyResponse a = convertProductBuyResponse(item.getProduct(), item.getQuantity());
            return a;
        }).collect(Collectors.toList());

        GetLoadOrderComfirmResponse resp = new GetLoadOrderComfirmResponse();
        resp.setGetProductBuyResponseList(list);
        User user = this.iUserRepository.findByUserIdAndIsDeleteFalse(userId).orElseThrow(() -> new NotXException("Không tìm thấy người dùng này", HttpStatus.NOT_FOUND));
        resp.setFullName(user.getFullName());
        resp.setPhone(user.getPhone());
        resp.setAddress(user.getAddress());
        return resp;
    }

    private Integer sellingPrice(Product product) {// gia ban san pham
        Integer price = product.getUnitPrice() - (product.getUnitPrice() * product.getDiscount()) / 100;
        return price;
    }

    private Integer totalSellingPrice(Product product, Integer quanity) {// tong gia 1 san pham theo so luong
        Integer price = sellingPrice(product) * quanity;
        return price;
    }

    // bien Produc thanh GetProductBuyResponse
    private GetProductBuyResponse convertProductBuyResponse(Product product, Integer quatity) {
        GetProductBuyResponse response = this.modelMapper.map(product, GetProductBuyResponse.class);
        response.setSellingPrice(sellingPrice(product));
        response.setQuantityBuy(quatity);
        response.setTotalMoney(totalSellingPrice(product, quatity));
        return response;
    }

    // tao don hang
    @Transactional
    @Override
    public MessageResponse createOrderVsOfflineOrLinkTransferPayment(CreateOrderResquest createOrderResquest) {

        Order order = new Order();
        order.setReceiptUser(createOrderResquest.getReceiptUser());
        order.setAddress(createOrderResquest.getAddress());
        order.setPhoneNumber(createOrderResquest.getPhone());

        if (createOrderResquest.getMethodPayment().equals("tien_mat")) {
            order.setNote(ConstantValue.STATUS_ORDER_NO_TT);
        }
        if (createOrderResquest.getMethodPayment().equals("tn_momo")) {
            order.setNote(ConstantValue.STATUS_ORDER_YES_TT_MOMO_GG);
        }
        order.setStatusOrder(ConstantValue.STATUS_ORDER_NOT_APPROVED);
        order.setCreatedDate(new Date(System.currentTimeMillis()));

        System.out.println("den day 1");
        User user = this.iUserRepository.findByUserIdAndIsDeleteFalse(createOrderResquest.getUserId())
                .orElseThrow(() -> new NotXException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND));
        order.setUser(user);

        //list san pahm dat mua

        List<Cart> cartList = this.iCartRepository.findById_UserIdAndIsDeleteFalse(createOrderResquest.getUserId());
        System.out.println("mang ban dau co   "+ cartList.size());
        List<Cart> cartBuyList = cartList.stream().filter((item)-> createOrderResquest.getBuyProducts().contains(item.getId().getProductId())).collect(Collectors.toList());
        // tong tien
        Integer totalOrder = cartBuyList.stream()
                .reduce(0, (initTotal, item)-> initTotal + (totalSellingPrice(item.getProduct(), item.getQuantity())), Integer::sum);

        order.setTotalAmount(Long.valueOf(totalOrder));// chuyên sang do dat la Long
        Order order1= this.iOrderRepository.save(order);

        if(!cartBuyList.isEmpty()){
            cartBuyList.forEach((item)->{
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(new OrderDetailIDKey(order1.getOrdersId(),item.getId().getProductId()));
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setAmount(Long.valueOf(totalSellingPrice(item.getProduct(),item.getQuantity())));
                this.iOrderDetailRepository.save(orderDetail);
                this.iCartRepository.deleteProductFromCart(item.getId());
                if(cartBuyList.isEmpty()){
                    return;
                }
            });
        }
        return new MessageResponse("Bạn đã tạo đơn hàng thành công với id đơn hàng là:" + order1.getOrdersId());
    }
}
