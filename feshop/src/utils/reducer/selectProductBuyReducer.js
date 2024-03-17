
// ham thuc hien hanh dong
export const addProductId = (productId) => {
    return {
        type: "ADD_PRODUCT_ID", productId
    }
}

export const deleteProductId = (productId) => {
    return {
        type: "DELETE_PRODUCT_ID", productId
    }
}

const initState = {
    productSelectList: []
}

const selectproductBuyReducer = (state =initState, action) => {
    switch (action.type) {
        case "ADD_PRODUCT_ID":
            return {
                ...state, productSelectList: [...state.productSelectList, action.productId]
            };
            case "DELETE_PRODUCT_ID":
                return {
                    ...state, productSelectList: state.productSelectList.filter(id=> id !== action.productId)
                };
            default:
                return state;
    }
}

export default selectproductBuyReducer;


