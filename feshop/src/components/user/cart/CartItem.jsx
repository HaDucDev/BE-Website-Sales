import { useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import cartServiceUser from "../../../services/user/user.cart.service";
import { addProductId, deleteProductId } from "../../../utils/reducer/selectProductBuyReducer";


const CartItem = (props) => {
    console.log("ok roi chu")
    console.log(props.data);

    const data = useSelector(state => state.listProductBuy);
    console.log("list redux");
    console.log(data.productSelectList);

    // lay ham de thuc hien action
    const dispatch = useDispatch();

    const [quantityBuy, setQuantityBuy] = useState(props.data.quantity);



    const handleQuantity = (operator) => {
        let data = {
            "userId": JSON.parse(localStorage.getItem("currentUser")).userId,
            "productId": props.data.id.productId,
            "quantity": 1,
            "operator": operator
        }
        cartServiceUser.addProductToCartService(data).then((dataResponse) => {
            console.log(dataResponse.data);
            if (operator === "add") {
                setQuantityBuy(quantityBuy + 1);
            }
            if (operator === "sub") {
                setQuantityBuy(quantityBuy - 1);
            }
        }).catch((err) => {
            console.log(err.response.data);
        })

    }

    const handleDelete = () => {
        let dataRequest = {
            "userId": JSON.parse(localStorage.getItem("currentUser")).userId,
            "productId": props.data.id.productId
        }
        console.log(dataRequest);
        cartServiceUser.deleteCartService(dataRequest).then((dataResponse) => {
            console.log(dataResponse.data);
            dispatch(deleteProductId(props.data.id.productId));
            props.loadCart(Math.random());
            alert(dataResponse.data.message);
        }).catch((err) => {
            console.log(err.response.data);
            alert(err.response.data)
        })
    }

    const [isChecked, setIsChecked] = useState(false);//checkbox
    const [focus, setFocus] = useState(false);//dau cach
    const [modalError, setModalError] = useState(false);//mo modal
    const [error, setError] = useState("");

    const [isAddSubmitting, setIsAddSubmitting] = useState(false);// khoa nut cong khi state quantityBuy qua sl ton kho
    const [isSubSubmitting, setIsSubSubmitting] = useState(false);// khoa nut tru khi state quantityBuy qua sl ton kho

    const list = data.productSelectList;
    const handleCheckChange = () => {
        console.log("testnao the kia:"+isChecked)
        setFocus(true);
        let dataRequest = {
            "userId": JSON.parse(localStorage.getItem("currentUser")).userId,
            "productId": props.data.id.productId,
            "quantity": quantityBuy
        }
        if (isChecked === false) {
            if (list.includes(props.data.id.productId) === true) {
                dispatch(deleteProductId(props.data.id.productId));
                setIsChecked(false);
                setFocus(false);
            }
            else {
                cartServiceUser.checkProductQuantityCartService(dataRequest).then((dataResponse) => {
                    if (dataResponse.data === true) {
                        dispatch(addProductId(props.data.id.productId));
                        //props.loadCart(Math.random());
                        setIsChecked(true);
                        props.loadCart(Math.random());
                    }
                }).catch((err) => {
                    setFocus(false);
                    setModalError(!modalError);
                    let error = err.response.data
                    setError(error);
                })
            }
        }
        if (isChecked === true) {
            dispatch(deleteProductId(props.data.id.productId));
            setIsChecked(false);
            setFocus(false);
        }
    }

    return (
        <>
            <tr>
                <td style={{ padding: "2%" }}>
                    <input type="checkbox" checked={list.includes(props.data.id.productId) ? true : false} onChange={handleCheckChange} style={{ transform: "scale(2)" }} />
                </td>
                <td style={{ padding: "2%" }}>{props.data.id.productId}</td>
                <td style={{ display: "flex", justifyContent: "center", height: "100%", padding: "3%" }}>
                    <div style={{ width: "20%", height: "20%" }}>
                        <img src={props.data.product.productImage} alt="" style={{ width: "30%" }} />
                    </div>
                    <div style={{ width: "40%" }}>
                        <Link to={`/product-detail/${props.data.id.productId}`}>
                            <div>{props.data.product.productName}</div>
                        </Link>
                    </div>
                </td>
                <td style={{ padding: "2%" }}>
                    <div style={{ display: "flex", justifyContent: "center", alignItems: "center" }}>
                        <button disabled={isSubSubmitting} onClick={(e) => {
                            e.preventDefault();
                            setIsAddSubmitting(false)
                            if (quantityBuy < 2) { setIsSubSubmitting(true); setIsAddSubmitting(true); }
                            else handleQuantity("sub");
                        }}
                        >-</button>
                        <input type="number" value={quantityBuy} min={1} max={props.data.product.quantity}
                            onChange={(e) => {
                                setQuantityBuy(e.target.value);
                            }}
                            readOnly={focus}
                            style={{ width: "50%", textAlign: 'center' }} />
                        <button disabled={isAddSubmitting} onClick={(e) => {
                            e.preventDefault();
                            setIsSubSubmitting(false);
                            if (quantityBuy > props.data.product.quantity) {
                                setIsAddSubmitting(true); setIsSubSubmitting(true);
                            }
                            else handleQuantity("add");
                        }}>+</button>
                    </div>
                </td>
                <td>{Number(props.data.product.unitPrice) - Number(props.data.product.unitPrice) * Number(props.data.product.discount) / 100}đ
                    <pre /><s>{props.data.product.unitPrice}đ</s>
                </td>
                <td style={{ justifyContent: "center", alignItems: "center" }}>
                    <Button onClick={handleDelete} style={{ marginTop: "10%" }}> Xóa </Button>
                </td>
            </tr>

            <Modal show={modalError} onHide={() => setModalError(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Lỗi</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p> {error["message"]}</p>
                    <p> {error["quantity"]}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={() => setModalError(false)}>
                        Đóng
                    </Button>
                </Modal.Footer>
            </Modal>

        </>
    )
}
export default CartItem;