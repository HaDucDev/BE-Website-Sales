package com.haduc.beshop.service.impl;


import com.haduc.beshop.config.paymentMomo.MomoConfig;
import com.haduc.beshop.model.*;
import com.haduc.beshop.repository.*;
import com.haduc.beshop.service.IOrderService;
import com.haduc.beshop.util.ConstantValue;
import com.haduc.beshop.util.FunctionCommon;
import com.haduc.beshop.util.dto.request.user.CreateOrderResquest;
import com.haduc.beshop.util.dto.request.user.MomoIPNRequest;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;
import com.haduc.beshop.util.dto.response.user.GetProductBuyResponse;
import com.haduc.beshop.util.exception.NotXException;
import com.mservice.allinone.models.CaptureMoMoResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private MomoConfig momoConfig;

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
    public MessageResponse createOrderVsOfflineOrLinkTransferPayment(CreateOrderResquest createOrderResquest) throws Exception {

        Order order = new Order();
        order.setReceiptUser(createOrderResquest.getReceiptUser());
        order.setAddress(createOrderResquest.getAddress());
        order.setPhoneNumber(createOrderResquest.getPhone());
        order.setStatusOrder(ConstantValue.STATUS_ORDER_NOT_APPROVED);
        order.setCreatedDate(new Date(System.currentTimeMillis()));

        System.out.println("den day 1");
        User user = this.iUserRepository.findByUserIdAndIsDeleteFalse(createOrderResquest.getUserId())
                .orElseThrow(() -> new NotXException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND));
        order.setUser(user);

        //list san pahm dat mua

        List<Cart> cartList = this.iCartRepository.findById_UserIdAndIsDeleteFalse(createOrderResquest.getUserId());
        System.out.println("mang ban dau co   " + cartList.size());
        List<Cart> cartBuyList = cartList.stream().filter((item) -> createOrderResquest.getBuyProducts().contains(item.getId().getProductId())).collect(Collectors.toList());
        // tong tien
        Integer totalOrder = cartBuyList.stream()
                .reduce(0, (initTotal, item) -> initTotal + (totalSellingPrice(item.getProduct(), item.getQuantity())), Integer::sum);

        order.setTotalAmount(Long.valueOf(totalOrder));// chuyên sang do dat la Long
        Order order1 = this.iOrderRepository.save(order);

        if (!cartBuyList.isEmpty()) {
            cartBuyList.forEach((item) -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(new OrderDetailIDKey(order1.getOrdersId(), item.getId().getProductId()));
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setAmount(Long.valueOf(totalSellingPrice(item.getProduct(), item.getQuantity())));
                this.iOrderDetailRepository.save(orderDetail);
                //this.iCartRepository.deleteProductFromCart(item.getId());
                if (cartBuyList.isEmpty()) {
                    return;
                }
            });
        }
        if (createOrderResquest.getMethodPayment().equals("tien_mat")) {
            order.setNote(ConstantValue.STATUS_ORDER_NO_TT);
            return new MessageResponse("Bạn đã tạo đơn hàng thành công với id đơn hàng là:" + order1.getOrdersId());
        }
        if (createOrderResquest.getMethodPayment().equals("tn_momo")) {
            order.setNote(ConstantValue.STATUS_ORDER_YES_TT_MOMO_GG);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println("lay ra   " + username);
            User userCheck = this.iUserRepository.findByUsername(username).orElseThrow(() -> new NotXException("Không tìm thấy người dùng này", HttpStatus.NOT_FOUND));
            if (userCheck != null) {
                String orderId = FunctionCommon.getRandomNumber(5) + userCheck.getUsername() + System.currentTimeMillis() + "hdshop" + order1.getOrdersId();
                String requestId = FunctionCommon.getRandomNumber(4) + userCheck.getUserId().toString() + System.currentTimeMillis();
                String total = totalOrder.toString();
                String orderInfo = "Thanh toán đơn hàng";
                String returnURL = "https://ngrok.com/tos";
                String notifyURL = "https://faf2-171-240-243-88.ap.ngrok.io/api/order/result-payment-online-momo";
                String extraData = "6";
                CaptureMoMoResponse captureMoMoResponse = this.momoConfig.process(orderId, requestId, total, orderInfo, returnURL, notifyURL, extraData);
                String url = captureMoMoResponse.getPayUrl();
                return new MessageResponse(url);
            }
        }
        return null;
    }

    @Transactional
    @Override
    public void handleOrderAfterPaymentMoMo(MomoIPNRequest request) {
        String idOrderMoMoSend = request.getOrderId();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        System.out.println("lay ra   " + username);
        String[] idOrderMoMoSendAray = idOrderMoMoSend.split("hdshop");
        System.out.println("id don hang gui den"+idOrderMoMoSendAray.length);
        String getOrderId = idOrderMoMoSendAray[idOrderMoMoSendAray.length-1];// lay so cuoi la id
        System.out.println("id don hang can sua"+getOrderId);
        Order order = this.iOrderRepository.findById(Integer.parseInt(getOrderId)).orElseThrow(()-> new NotXException("Không tìm thấy đơn hàng này", HttpStatus.NOT_FOUND));

        System.out.println("don hang lay dc"+order.getNote());
        if (request.getErrorCode() == 0) {
            // Đơn hàng đã thanh toán thành công
            // Xử lý tương ứng với trạng thái này ở đây
            System.out.println("Bạn đã thanh toán thành công với đơn hàng có id" + request.getOrderId());
            if(order.getNote().equals(ConstantValue.STATUS_ORDER_YES_TT_MOMO_GG)){
                order.setNote(ConstantValue.STATUS_ORDER_YES_TT_MOMO_SUCCES);
            }
            //
        } else {
            // Đơn hàng không thanh toán thành công
            String errorMessage = request.getMessage();
            if(order.getNote().equals(ConstantValue.STATUS_ORDER_YES_TT_MOMO_GG)){
                order.setNote(ConstantValue.STATUS_ORDER_FAIL_TT_MOMO_GG);
            }
            System.out.println("error-status" + request.getErrorCode());
            System.out.println("Bạn đã gặp lỗi" + errorMessage);
        }
    }
}
