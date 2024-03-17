import axios from "axios";
import { API_COMMON,authHeaderJson} from "../../utils/utils";


const checkProductOrderConfirmationService = async (data) => {// dung chung them va sua
    const response = await axios.post( API_COMMON +"order/order-confirmation",data,{ headers: authHeaderJson() })
    return response;
}

//xem cac san pham dang dinh mua
const loadOrderComfirmService = async (userId) => {
  const response = await axios.get( API_COMMON +`order/order-confirmation/${userId}`,{ headers: authHeaderJson() })
  return response;
}

// tao don hang bang thanh toan tien mat hoac xuat link thanh toan
const createOrderOfflineOrPaymentLinkOnline = async (data) =>{
  const response = await axios.post( API_COMMON +"order/create-offline-or-link-payment-online",data, { headers: authHeaderJson() } )
  return response;
}

//lay tat ca don hang cua nguoi dung
const getAllOrderByUserId = async (userId) =>{
  const response = await axios.get( API_COMMON +`order/all-order/${userId}`,{ headers: authHeaderJson() })
  return response;
}

//huy don hang neu chua duyet

const cancelOrderService = async (ordersId) =>{
  const response = await axios.delete(API_COMMON+ `order/${ordersId}`,{ headers: authHeaderJson() })
  return response;
}



const orderServiceUser = {
  checkProductOrderConfirmationService,
  loadOrderComfirmService,
  createOrderOfflineOrPaymentLinkOnline,
  getAllOrderByUserId,
  cancelOrderService
}

export default orderServiceUser;