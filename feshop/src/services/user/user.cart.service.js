import axios from "axios";
import { API_COMMON,authHeaderJson} from "../../utils/utils";

const getCountProductCategoryService = async (userId) => {
    const response = await axios.get(API_COMMON +`cart/count-cart/${userId}`);
    return response;
}

const getAllProductInCartService = async (userId) => {
    const response = await axios.get(API_COMMON +`cart/all-product/${userId}`,{ headers: authHeaderJson() });
    return response;
}

const addProductToCartService = async (data) => {// dung chung them va sua
    const response = await axios.post( API_COMMON +"cart",data,{ headers: authHeaderJson() })
    return response;
}



const deleteCartService = async (data) => {
    const response = await axios.delete( API_COMMON +"cart",{data:data},{ headers: authHeaderJson() })
    return response;
}

const checkProductQuantityCartService = async (data) => {// dung chung them va sua
    const response = await axios.post( API_COMMON +"cart/check",data,{ headers: authHeaderJson() })
    return response;
}

const cartServiceUser = {
    getCountProductCategoryService,
    getAllProductInCartService,
    addProductToCartService,
    deleteCartService,
    checkProductQuantityCartService
}

export default cartServiceUser;