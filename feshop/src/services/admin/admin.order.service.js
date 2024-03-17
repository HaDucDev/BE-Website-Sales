import axios from "axios";
import { API_COMMON,authHeaderJson } from "../../utils/utils";

//lay tat ca don hang cua nguoi dung
const getAllOrder = async () => {
    const response = await axios.get(API_COMMON + "order/admin/all-order",{ headers: authHeaderJson() });
    return response;
}


//huy don hang neu chua duyet

const cancelOrderService = async (ordersId) => {
    const response = await axios.delete(API_COMMON + `order/${ordersId}`,{ headers: authHeaderJson() })
    return response;
}

const acceptOrderService = async (data) => {
    const response = await axios.post(API_COMMON + "order/assignment/shipper", data,{ headers: authHeaderJson() })
    return response;
}


const orderService = {
    getAllOrder,
    cancelOrderService,
    acceptOrderService
}

export default orderService;