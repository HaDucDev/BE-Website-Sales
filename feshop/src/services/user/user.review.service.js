import axios from "axios";
import { API_COMMON,authHeaderJson} from "../../utils/utils";


const addReviewPropductService = async (data) => {// dung chung them va sua
    const response = await axios.post( API_COMMON +"reviews",data,{ headers: authHeaderJson() })
    return response;
}

const getReviewAllByProductIdService = async (id) => {
    const response = await axios.get(API_COMMON +`reviews/all-review/${id}`);
    return response;
}

const reviewServiceUser = {
    addReviewPropductService,
    getReviewAllByProductIdService
}

export default reviewServiceUser;