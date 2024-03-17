import axios from "axios";
import { API_COMMON,convertObjectBlob,authHeaderJson ,authHeaderFormData} from "../../utils/utils";


const loginService = async (user) => {
    const response = await axios.post(API_COMMON + "login", user);
    return response;
}

const registerService = async (user) => {
    const response = await axios.post(API_COMMON + "register", user);
    return response;
}


const changePassService = async (user) => {
    const response = await axios.put(API_COMMON + "change-pass", user, { headers: authHeaderJson() });
    return response;
}

const inforUserByIdService = async (userId) => {// chung get user admin
    const response = await axios.get(API_COMMON + `user/${userId}`, { headers: authHeaderJson() });
    return response;
}


const updateInforUserService = async (dataRequest, file) => {
    const blob = convertObjectBlob(dataRequest);
    const formData = new FormData();
    formData.append('changeInforAccountRequest', blob);
    formData.append('avatar', file);
    const response = await axios.put(API_COMMON + "change-account-info", formData, { headers: authHeaderFormData() });
    return response;
}

const forgetSendMailService = async (data) => {
    const response = await axios.post(API_COMMON + "forget-send-code", data);
    return response;
}

const cofirmCodeFromEmailService = async (data) => {
    const response = await axios.post(API_COMMON + "confirm-code-send-new-pass", data);
    return response;
}


const accountService = {
    loginService,
    registerService,
    changePassService,
    inforUserByIdService,
    updateInforUserService,
    forgetSendMailService,
    cofirmCodeFromEmailService
}

export default accountService;