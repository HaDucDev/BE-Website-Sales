package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.*;
import com.haduc.beshop.repository.*;
import com.haduc.beshop.service.IReviewsService;
import com.haduc.beshop.util.ConstantValue;
import com.haduc.beshop.util.dto.request.user.ReviewsRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ReviewsServiceImpl implements IReviewsService {

    @Autowired
    private IReviewsRepository iReviewsRepository;

    @Autowired
    private IProductRepository iProductRepository;
    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    private IOrderDetailRepository iOrderDetailRepository;

    @Override
    public MessageResponse addReviewsToProduct(ReviewsRequest reviewsRequest) {

        ReviewsIdKey reviewsIdKey = new ReviewsIdKey(reviewsRequest.getOrdersId(), reviewsRequest.getProductId(), reviewsRequest.getUserId());
        Optional<Reviews> reviews = this.iReviewsRepository.findById(reviewsIdKey);

        Product product = this.iProductRepository.findByProductIdAndIsDeleteFalse(reviewsRequest.getProductId()).orElseThrow(()-> new  NotXException("Id sản phẩm lỗi", HttpStatus.NOT_FOUND));
        User user = this.iUserRepository.findByUserIdAndIsDeleteFalse(reviewsRequest.getUserId()).orElseThrow(()-> new  NotXException("Id người dùng lỗi", HttpStatus.NOT_FOUND));
        Order order= this.iOrderRepository.findByIdAndStatusOrder(reviewsRequest.getOrdersId(), ConstantValue.STATUS_ORDER_DELIVERED).orElseThrow(()-> new  NotXException("Id order lỗi", HttpStatus.NOT_FOUND));

        if(!reviews.isPresent()){// chua coment thi dc coment, co roi ma post vo la loi
            Reviews reviews1 = new Reviews();
            reviews1.setId(reviewsIdKey);
            reviews1.setComments(reviewsRequest.getComment());
            reviews1.setRating(reviewsRequest.getRating());
            reviews1.setCreatedDate(new Date(System.currentTimeMillis()));
            reviews1.setProduct(product);
            reviews1.setUser(user);
            reviews1.setOrder(order);
            Reviews reviewsSave = this.iReviewsRepository.save(reviews1);

            //cap nhat khong cho comment khi da danh gia
            OrderDetailIDKey  orderDetailIDKey = new OrderDetailIDKey(reviewsRequest.getOrdersId(), reviewsRequest.getProductId());
            // sửa lại đánh giá của sản phẩm khi comment thành công.
            this.iOrderDetailRepository.updateDisableReviewOrderDetail(ConstantValue.STATUS_ORDER_DETAIL_YES,orderDetailIDKey);

            return new MessageResponse(String.format("Sản phẩm có id là %s được đánh giá thành công!", reviewsSave.getId().getProductId()));
        }
        throw new NotXException("Sản phẩm đã được đánh giá rồi", HttpStatus.BAD_REQUEST);
    }
}
