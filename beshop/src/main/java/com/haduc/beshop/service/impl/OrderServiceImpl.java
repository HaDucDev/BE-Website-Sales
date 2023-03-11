package com.haduc.beshop.service.impl;


import com.haduc.beshop.config.paymentMomo.MomoConfig;
import com.haduc.beshop.model.*;
import com.haduc.beshop.repository.*;
import com.haduc.beshop.service.IOrderService;
import com.haduc.beshop.util.ConstantValue;
import com.haduc.beshop.util.FunctionCommon;
import com.haduc.beshop.util.dto.request.admin.AssignmentShipperRequest;
import com.haduc.beshop.util.dto.request.shipper.ConfirmOrderRequest;
import com.haduc.beshop.util.dto.request.shipper.RemovedOrderRequest;
import com.haduc.beshop.util.dto.request.user.CreateOrderResquest;
import com.haduc.beshop.util.dto.request.user.MomoIPNRequest;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;
import com.haduc.beshop.util.dto.response.user.GetProductBuyResponse;
import com.haduc.beshop.util.enum_role.ERole;
import com.haduc.beshop.util.exception.NotXException;
import com.mservice.allinone.models.CaptureMoMoResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
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
                this.iCartRepository.deleteProductFromCart(item.getId());
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
            User userCheck = this.iUserRepository.findByUsernameAndIsDeleteFalse(username).orElseThrow(() -> new NotXException("Không tìm thấy người dùng này", HttpStatus.NOT_FOUND));
            if (userCheck != null) {
                String orderId = FunctionCommon.getRandomNumber(5) + userCheck.getUsername() + System.currentTimeMillis() + "hdshop" + order1.getOrdersId();
                String requestId = FunctionCommon.getRandomNumber(4) + userCheck.getUserId().toString() + System.currentTimeMillis();
                String total = totalOrder.toString();
                String orderInfo = "Thanh toán đơn hàng";
                String returnURL = "http://localhost:3000/history-order";//link sau khi thanh toan tren fe chuyen den do may chu momo lam trung gian
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
        String[] idOrderMoMoSendAray = idOrderMoMoSend.split("hdshop");
        System.out.println("id don hang gui den" + idOrderMoMoSendAray.length);
        String getOrderId = idOrderMoMoSendAray[idOrderMoMoSendAray.length - 1];// lay so cuoi la id
        System.out.println("id don hang can sua" + getOrderId);
        Order order = this.iOrderRepository.findById(Integer.parseInt(getOrderId)).orElseThrow(() -> new NotXException("Không tìm thấy đơn hàng này", HttpStatus.NOT_FOUND));

        System.out.println("don hang lay dc" + order.getNote());
        if (request.getErrorCode() == 0) {
            // Đơn hàng đã thanh toán thành công
            System.out.println("Bạn đã thanh toán thành công với đơn hàng có id" + request.getOrderId());
            if (order.getNote().equals(ConstantValue.STATUS_ORDER_YES_TT_MOMO_GG)) {
                order.setNote(ConstantValue.STATUS_ORDER_YES_TT_MOMO_SUCCES);
            }
            //
        } else {
            // Đơn hàng không thanh toán thành công
            String errorMessage = request.getMessage();
            if (order.getNote().equals(ConstantValue.STATUS_ORDER_YES_TT_MOMO_GG)) {
                order.setNote(ConstantValue.STATUS_ORDER_FAIL_TT_MOMO_GG);
            }
            System.out.println();
            System.out.println("Bạn đã gặp lỗi error-status" + request.getErrorCode() + " - nội dung: " + errorMessage);
        }
    }

    @Override
    public List<Order> findAllByUser_UserId(Integer id) {
        return this.iOrderRepository.findAllByUser_UserId(id);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        int affectedRows = this.iOrderRepository.softUpdateStatusOrder(id, ConstantValue.STATUS_ORDER_CANCELLED);
        System.out.println(affectedRows);
        if (affectedRows == 0) {
            throw new NotXException("Xảy ra lỗi khi hủy đơn hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //==========================================> ADMIN
    @Override
    public List<Order> findAllOrderByCreatedDateDesc() {
        Sort sort = JpaSort.unsafe(Sort.Direction.DESC, "createdDate");
        return this.iOrderRepository.findAll(sort);
    }

    int randomSize(int a){
        return (int) (Math.random()*a);
    }

    @Transactional
    @Override
    public MessageResponse assignmentOrderForShipper(AssignmentShipperRequest  assignmentShipperRequest) {

        List<User> shipperList = this.iUserRepository.findByRole_NameAndAssignment(ERole.valueOf(String.valueOf(ERole.ROLE_SHIPPER)), 0);
        if(shipperList.isEmpty()){// neu rong
            int affectedRows = this.iUserRepository.updateColumnAssignment(0);
            System.out.println(affectedRows);
            if (affectedRows == 0) {
                throw new NotXException("Xảy ra lỗi khi cập nhật bảng phân công", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<User> againShipperList = this.iUserRepository.findByRole_NameAndAssignment(ERole.valueOf(String.valueOf(ERole.ROLE_SHIPPER)), 0);
            // lay id shipper ngau nhien (userID)
            int random = randomSize(againShipperList.size());
            User userIdSelected = againShipperList.get(random);

            //lay id user de cap nhat don hang
            this.iOrderRepository.softUpdateAssignmentOrder(userIdSelected.getUserId(),ConstantValue.STATUS_ORDER_APPROVED,assignmentShipperRequest.getOrdersId());
            // cap nhat trang thai shipper (tai khoan) da phan cong
            this.iUserRepository.updateAfterAssignment(1,userIdSelected.getUserId());
            return new MessageResponse("Bạn đã tạo phân thành công shipper :" + userIdSelected.getUserId() + "cho don hang" + assignmentShipperRequest.getOrdersId());
        }
        if(!shipperList.isEmpty()){// rong rong
            int randomIndexNew= (int) (Math.random() * shipperList.size());
            User userIdSelectedNew = shipperList.get(randomIndexNew);
            //lay id user de cap nhat don hang
            this.iOrderRepository.softUpdateAssignmentOrder(userIdSelectedNew.getUserId(),ConstantValue.STATUS_ORDER_APPROVED,assignmentShipperRequest.getOrdersId());
            // cap nhat trang thai shipper (tai khoan) da phan cong
            this.iUserRepository.updateAfterAssignment(1,userIdSelectedNew.getUserId());
            return new MessageResponse("Bạn đã tạo phân thành công shipper :" + userIdSelectedNew.getUserId() + "cho don hang" + assignmentShipperRequest.getOrdersId());
        }
        throw  new NotXException("Phân công bị lỗi", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<Order> findAllByShipperId(Integer id) {
        return this.iOrderRepository.findAllByShipperId(id);
    }

    @Transactional
    @Override
    public MessageResponse softUpdateCompleteOrder(ConfirmOrderRequest confirmOrderRequest) {
        System.out.println("ok roi chu text "+confirmOrderRequest.getOrderId() + " "+ confirmOrderRequest.getShipperId());
        int affectedRows = this.iOrderRepository.softUpdateCompleteOrder(confirmOrderRequest.getOrderId(),
                new Date(System.currentTimeMillis()),ConstantValue.STATUS_ORDER_DELIVERED,confirmOrderRequest.getShipperId());
        System.out.println(affectedRows);
        if (affectedRows == 0) {
            throw new NotXException("Xảy ra lỗi khi hủy đơn hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new MessageResponse("Dơn hàng"+ confirmOrderRequest.getOrderId() +" được giao thành công" );
    }

    @Transactional
    @Override
    public MessageResponse softUpdateshipperWhenRemoveOrder(RemovedOrderRequest removedOrderRequest) {
        // lay tat shipper tru shipper vua gui den
        List<User> shippers = this.iUserRepository.findByRole_NameAndAssignmentAndUserIdNot(ERole.valueOf(String.valueOf(ERole.ROLE_SHIPPER)), 0,removedOrderRequest.getShipperId());

        boolean testSizeZero = shippers.isEmpty();
        if(testSizeZero){// neu rong
            int affectedRows = this.iUserRepository.updateColumnAssignment(0);
            System.out.println(affectedRows);
            if (affectedRows == 0) {
                throw new NotXException("Xảy ra lỗi khi cập nhật bảng phân công", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<User> againShipperList = this.iUserRepository.findByRole_NameAndAssignmentAndUserIdNot(ERole.valueOf(String.valueOf(ERole.ROLE_SHIPPER)), 0,removedOrderRequest.getShipperId());
            // lay id shipper ngau nhien (userID)
            int random = randomSize(againShipperList.size());
            User userIdSelected = againShipperList.get(random);

            //lay id user de cap nhat don hang
            this.iOrderRepository.softUpdateAssignmentOrder(userIdSelected.getUserId(),ConstantValue.STATUS_ORDER_APPROVED,removedOrderRequest.getOrderId());
            // cap nhat trang thai shipper (tai khoan) da phan cong
            this.iUserRepository.updateAfterAssignment(1,userIdSelected.getUserId());
            //cap nhat lai trang thai shipper (tai khoan) vua huy nhan don
            this.iUserRepository.updateAfterAssignment(0,removedOrderRequest.getShipperId());
            return new MessageResponse("Bạn đã không nhận đơn hàng :"+ removedOrderRequest.getOrderId());
        }
        if(testSizeZero==false){
            int random = randomSize(shippers.size());
            User userIdSelectedNew = shippers.get(random);
            this.iOrderRepository.softUpdateAssignmentOrder(userIdSelectedNew.getUserId(),ConstantValue.STATUS_ORDER_APPROVED,removedOrderRequest.getOrderId());
            // cap nhat trang thai shipper (tai khoan) da phan cong
            this.iUserRepository.updateAfterAssignment(1,userIdSelectedNew.getUserId());
            //cap nhat lai trang thai shipper (tai khoan) vua huy nhan don
            this.iUserRepository.updateAfterAssignment(0,removedOrderRequest.getShipperId());
            return new MessageResponse("Bạn đã không nhận đơn hàng :"+ removedOrderRequest.getOrderId());
        }
        throw  new NotXException("Không nhận đơn bị lỗi", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
