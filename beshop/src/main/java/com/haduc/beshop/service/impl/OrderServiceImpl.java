package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.Order;
import com.haduc.beshop.model.Product;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.ICartRepository;
import com.haduc.beshop.repository.IOrderRepository;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.repository.IUserRepository;
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
        List<Cart> cartBuyList = cartList.stream().filter((item)-> createOrderResquest.getBuyProducts().contains(item.getId())).collect(Collectors.toList());
        // tong tien
        if(cartBuyList.isEmpty()){
            System.out.println("mang nay rong cmnr");
        }
        Integer totalOrder = cartBuyList.stream()
                .reduce(0, (initTotal, item)-> initTotal + (totalSellingPrice(item.getProduct(), item.getQuantity())), Integer::sum);
        System.out.println("den day 1"+ totalOrder);
        //System.out.println("den day 1"+ totalSellingPrice(cartBuyList.get(0).getProduct(), cartBuyList.get(0).getQuantity()));

        order.setTotalAmount(Long.valueOf(totalOrder));// chuyên sang do dat la Long

        Order order1= this.iOrderRepository.save(order);

        return new MessageResponse("Bạn đã tạo đơn hàng thành công");
    }
}
