import React, { useEffect, useState } from "react";
import { Form, Row, Col, Button } from "react-bootstrap";
import ReactPaginate from 'react-paginate';
import "./css/confirm_order.css"
import 'bootstrap/dist/css/bootstrap.css';
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import orderServiceUser from "../../../services/user/user.order.service";


const ConfirmOrder = () => {

    const data = useSelector(state => state.listProductBuy);
    const listRequest = ((data.productSelectList).length === 0) ? (JSON.parse(sessionStorage.getItem("listBuySave")).productIdBuyList) : (data.productSelectList);

    const [productList, setProductList] = useState([]);
    const [pageNumber, setPageNumber] = useState(0);
    const usersPerPage = 3; // Số lượng user hiển thị trên mỗi trang
    const pageCount = Math.ceil(listRequest.length / usersPerPage); // Tính tổng số trang
    const pagesVisited = pageNumber * usersPerPage; // Số user đã xem

    const [inforUser, setInforUser] = useState({
        fullName: "",
        phone: "",
        address: ""
    });

    const nav = useNavigate();

    const [selectedOption, setSelectedOption] = useState("tien_mat");// radio thanh toan


    useEffect(() => {
        orderServiceUser.loadOrderComfirmService(JSON.parse(localStorage.getItem("currentUser")).userId).then((dataResponse) => {
            let resp = dataResponse.data;
            console.log(resp.getProductBuyResponseList)
            setInforUser({
                fullName: resp.fullName,
                phone: resp.phone,
                address: resp.address
            })
            setProductList(resp.getProductBuyResponseList);
        })
    }, [pageNumber]);


    const fitlerProductBuy = (list) => {// loc san pham mua
        if (listRequest.length > 0) {
            return list.filter(item => listRequest.includes(item.productId))
        }
        return list;
    }
    const displayProducts = fitlerProductBuy(productList).slice(pagesVisited, pagesVisited + usersPerPage).map(producOrderInfor => {
        return (
            <tr key={producOrderInfor.productId}>
                <td>{producOrderInfor.productId}</td>
                <td style={{ padding: "0px", width: "10%" }}><img src={producOrderInfor.productImage} alt="" style={{ width: "10%", margin: "0px" }} /></td>
                <td>{producOrderInfor.productName}</td>
                <td>
                    <div> {(producOrderInfor.sellingPrice).toLocaleString('en-US')} đ </div>
                    <div>
                        <s>{(producOrderInfor.unitPrice).toLocaleString('en-US')} đ</s>
                    </div>
                </td>
                <td>{producOrderInfor.quantityBuy}</td>
                <td>{(producOrderInfor.totalMoney).toLocaleString('en-US')} đ</td>
            </tr>
        );
    });

    //tinh tong tien san pham mua
    const totalMoneyBuy = fitlerProductBuy(productList).reduce((initTotal, item) => {
        return initTotal + item.totalMoney;
    }, 0)


    const changePage = ({ selected }) => {
        setPageNumber(selected);
    };

    const handleCreateOrder = (e) => {
        e.preventDefault();
        if (listRequest.length === 0) {
            alert("Sản phẩm trống, yêu cầu người dùng chọn lại sản phẩm")
            nav("/cart")
        }
        if (listRequest.length !== 0) {

            let dataRequest = {
                "userId": JSON.parse(localStorage.getItem("currentUser")).userId,
                "receiptUser": inforUser.fullName,
                "phone": inforUser.phone,
                "address": inforUser.address,
                "methodPayment": selectedOption,
                "buyProducts": listRequest
            }
            orderServiceUser.createOrderOfflineOrPaymentLinkOnline(dataRequest).then((dataResponse)=>{
                let dataok =dataResponse.data;
                if(selectedOption==="tien_mat"){
                    alert(dataok["message"]);
                    sessionStorage.clear();
                    nav("/history-order")
                }
                if(selectedOption==="tn_momo"){
                    alert(dataok["message"]);
                    window.location.href = dataok["message"];
                }
            })
        }
    }

    return (
        <>
            <h2 style={{ textAlign: "center" }}>Xác nhận đơn hàng</h2>
            <div style={{ display: "flex", border: "1px solid #2522ca" }}>
                <div style={{ width: "70%", margin: "0px 5px" }}>
                    <h5 style={{ textAlign: "center" }}>Sản phẩm mua</h5>
                    <div style={{ margin: "1% 0x 0px 1%", padding: "0%", borderRadius: "0%" }} className="table-responsive">
                        <table >
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th style={{ width: "30%" }}>Ảnh sản phẩm</th>
                                    <th>tên sản phẩm</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Tổng cộng</th>
                                </tr>
                            </thead>
                            {
                                (listRequest.length > 0 && displayProducts.length > 0) && (
                                    <tbody>
                                        {displayProducts}
                                    </tbody>)
                            }
                        </table>
                        {
                            (listRequest.length === 0) && (<div style={{ textAlign: "center" }}><i>Không có sản phẩm nào</i></div>)
                        }
                        <div>
                            <div style={{ float: "left", padding: "1%" }}>
                                <ReactPaginate
                                    previousLabel={'Previous'}
                                    nextLabel={'Next'}
                                    pageCount={pageCount}
                                    onPageChange={changePage}
                                    containerClassName={'pagination'}
                                    previousLinkClassName={'pagination__link'}
                                    nextLinkClassName={'pagination__link'}
                                    disabledClassName={'pagination__link--disabled'}
                                    activeClassName={'pagination__link--active'}
                                />
                            </div>
                            <div style={{ float: "right", }}>
                                <div>Shipping: Miễn phí</div>
                                <div>Tổng tiền: {(totalMoneyBuy).toLocaleString('en-US')} đ</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div style={{ width: "30%", margin: "0px 5px", borderLeft: "2px solid #2522ca" }}>
                    <h5 style={{ textAlign: "center" }}>Thông tin</h5>
                    <div style={{ margin: "1% 0x 0px 1%", padding: "1%", borderRadius: "0%" }}>
                        <Form>
                            <Row>
                                <Col>
                                    <Form.Group controlId="formName">
                                        <Form.Label>Tên người nhận</Form.Label>
                                        <Form.Control type="text" placeholder="Người nhận" defaultValue={inforUser.fullName} />
                                    </Form.Group>
                                    <Form.Group controlId="formSdt">
                                        <Form.Label>Số điện thoại</Form.Label>
                                        <Form.Control type="text" placeholder="Enter email" defaultValue={inforUser.phone} />
                                    </Form.Group>
                                    <Form.Group controlId="formAddress">
                                        <Form.Label>Địa chỉ nhận</Form.Label>
                                        <Form.Control type="text" placeholder="Địa chỉ" defaultValue={inforUser.address} />
                                    </Form.Group>
                                    <div >
                                        <Form.Group controlId="formRadio" >
                                            <Form.Label>Chọn phương thức thanh toán</Form.Label>
                                            <Form.Check type="radio" label="Tiền mặt" name="formRadio" id="option1"
                                                value="tien_mat"
                                                checked={selectedOption === "tien_mat"} onChange={(e) => setSelectedOption(e.target.value)} />
                                            <Form.Check type="radio" label="Thanh toán với momo" name="formRadio" id="option2"
                                                value="tn_momo"
                                                checked={selectedOption === "tn_momo"} onChange={(e) => setSelectedOption(e.target.value)} />
                                        </Form.Group>
                                    </div>
                                </Col>
                            </Row>
                        </Form>
                    </div>
                </div>
            </div>

            <div style={{ margin: "1% 40%", width: "60%" }}>
                <Button variant="outline-success" style={{ width: "30%" }} onClick={handleCreateOrder}>Đặt hàng</Button>
            </div>
        </>
    )
}

export default ConfirmOrder;