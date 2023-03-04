package com.haduc.beshop.config.paymentMomo;

import com.mservice.allinone.models.CaptureMoMoRequest;
import com.mservice.allinone.models.CaptureMoMoResponse;
import com.mservice.shared.constants.Parameter;
import com.mservice.shared.sharedmodels.HttpResponse;
import com.mservice.shared.utils.Encoder;
import com.mservice.shared.utils.Execute;
import org.json.JSONObject;

import static com.mservice.shared.sharedmodels.AbstractProcess.errorMoMoProcess;
import static com.mservice.shared.sharedmodels.AbstractProcess.getGson;

public class MomoConfig {

    public CaptureMoMoResponse process(String orderId, String requestId, String amount, String orderInfo, String returnURL, String notifyURL, String extraData){
        try {

            CaptureMoMoRequest captureMoMoRequest = this.createPaymentCreationRequest(orderId, requestId, amount, orderInfo, returnURL, notifyURL, extraData);
            CaptureMoMoResponse captureMoMoResponse = this.execute(captureMoMoRequest);

            return captureMoMoResponse;
        } catch (Exception exception) {
            System.out.println("process error");
        }
        return null;
    }

    public CaptureMoMoResponse execute(CaptureMoMoRequest request) {
        try {

            JSONObject json = new JSONObject();
            json.put("partnerCode", request.getPartnerCode());
            json.put("accessKey", request.getAccessKey());
            json.put("requestId", request.getRequestId());
            json.put("amount", request.getAmount());
            json.put("orderId", request.getOrderId());
            json.put("orderInfo", request.getOrderInfo());
            json.put("returnUrl", request.getReturnUrl());// link sau khi thanh toan thanh cong thi chuyen tiep tren giao dien
            json.put("notifyUrl", request.getNotifyUrl());// link gui data den voi method post
            json.put("extraData", request.getExtraData());
            json.put("requestType", "captureMoMoWallet");
            json.put("signature", request.getSignature());
            Execute execute = new Execute();
            HttpResponse response = execute.sendToMoMo("https://test-payment.momo.vn/gw_payment/transactionProcessor", json.toString());

            if (response.getStatus() != 200) {
                System.out.println("execute error");
            }
            System.out.println("123456789987654321: " + response.getData());
            CaptureMoMoResponse captureMoMoResponse = getGson().fromJson(response.getData(), CaptureMoMoResponse.class);
            errorMoMoProcess(captureMoMoResponse.getErrorCode());// trang thai ko co loi la 0

            return captureMoMoResponse;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid params capture MoMo Request");
        }

    }

    public CaptureMoMoRequest createPaymentCreationRequest(String orderId, String requestId, String amount, String orderInfo, String returnUrl, String notifyUrl, String extraData) {

        try {
            String requestRawData = new StringBuilder()
                    .append(Parameter.PARTNER_CODE).append("=").append("MOMO7XKJ20220612").append("&")//partnercode
                    .append(Parameter.ACCESS_KEY).append("=").append("dtwUCifariAsOfO8").append("&")// codeaccesskey
                    .append(Parameter.REQUEST_ID).append("=").append(requestId).append("&")
                    .append(Parameter.AMOUNT).append("=").append(amount).append("&")
                    .append(Parameter.ORDER_ID).append("=").append(orderId).append("&")
                    .append(Parameter.ORDER_INFO).append("=").append(orderInfo).append("&")
                    .append(Parameter.RETURN_URL).append("=").append(returnUrl).append("&")
                    .append(Parameter.NOTIFY_URL).append("=").append(notifyUrl).append("&")
                    .append(Parameter.EXTRA_DATA).append("=").append(extraData)
                    .toString();
            ;// chuoi momo tra ve khi giao dich thanh cong. PayUrl - link sang QR thanh toan

            String signRequest = Encoder.signHmacSHA256(requestRawData, "3ByWLLOo708Ptyc5Q5CoWnngNSM4vMEy");// secretKey

            return new CaptureMoMoRequest("MOMO7XKJ20220612", orderId, orderInfo, "dtwUCifariAsOfO8", amount, signRequest, extraData, requestId, notifyUrl, returnUrl, "captureWallet");
        } catch (Exception e) {
            System.out.println("createPaymentCreationRequest error");
        }
        return null;
    }
}
