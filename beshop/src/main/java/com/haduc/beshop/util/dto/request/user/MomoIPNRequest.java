package com.haduc.beshop.util.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MomoIPNRequest {

    private String partnerCode;
    private String accessKey;
    private String requestId;
    private String orderId;
    private long amount;
    private String orderInfo;
    private String orderType;
    private String transId;
    private int errorCode;
    private String message;
    private String localMessage;
    private String payType;
    private String responseTime;
    private String extraData;
    private String signature;
}
