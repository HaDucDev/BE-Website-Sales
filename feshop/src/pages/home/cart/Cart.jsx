import { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import cartServiceUser from '../../../services/user/user.cart.service';
import CartItem from '../../../components/user/cart/CartItem';
import "./css/cart.css"
import ReactPaginate from 'react-paginate';
import { useSelector } from 'react-redux';
import orderServiceUser from '../../../services/user/user.order.service';
import { useNavigate } from 'react-router';


const Cart = () => {

    const [carts, setCarts] = useState([]);// lưu trữ dữ liệu list

    const [loadCart, setLoadCart] = useState(0);

    const [currentPage, setCurrentPage] = useState(0); // lưu trữ trang hiện tại
    const itemsPerPage = 4; // số lượng items trên mỗi trang
    const pagesVisited = currentPage * itemsPerPage;

    const currentItems=  carts.slice(pagesVisited, pagesVisited+itemsPerPage);

    useEffect(() => {
        cartServiceUser.getAllProductInCartService(JSON.parse(localStorage.getItem("currentUser")).userId).then((dataResponse) => {
            console.log("test 12345678 - " + JSON.parse(localStorage.getItem("currentUser")).userId)
            setCarts(dataResponse.data);
            console.log(dataResponse.data)
        })
    }, [loadCart, currentPage])

    // hàm callback được gọi khi người dùng chọn trang mới
    const onPageChange = ({selected}) => {
        console.log(selected)
        setCurrentPage(selected);
    };

    const data = useSelector(state => state.listProductBuy);
    const listRequest=data.productSelectList;
    console.log(listRequest);

    const nav = useNavigate();
    const confirmList =()=>{
        let dataRequest = {
            "productIdBuyList": listRequest
        }
        orderServiceUser.checkProductOrderConfirmationService(dataRequest).then((dataResponse)=>{
            if(dataResponse.data===true){
                sessionStorage.setItem("listBuySave",JSON.stringify(dataRequest));
                nav("/confirm-order")
            }
        }).catch((e)=>{
            let data = e.response.data;
            (data["productIdBuyList"]) ? alert(data["productIdBuyList"]) : alert(data["message"]) ;
        })
    }
    return (
        <>
            <h2 style={{ textAlign: 'center' }}>Giỏ hàng</h2>
            <div style={{ margin: "auto", width: "85%" }}>
                <Table responsive>
                    <thead>
                        <tr>
                            <th style={{ width: "1%" }}>Chọn</th>
                            <th style={{ width: "6%" }}>Mã sản phẩm</th>
                            <th style={{ width: "30%" }}>Sản phẩm</th>
                            <th style={{ width: "8%" }}>Số lượng</th>
                            <th style={{ width: "5%" }}>Giá</th>
                            <th style={{ width: "5%" }}></th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            currentItems.length > 0 ? (
                                currentItems.map((item) => {
                                    return (<CartItem key={item.id.productId} data={item} loadCart={setLoadCart} />)
                                })
                            ) : (<tr>
                                <td colSpan={4}>Không có sản phẩm nào</td>
                            </tr>)
                        }
                    </tbody>
                </Table>
                <div style={{ justifyContent: "center" }}>
                    <div style={{ float: "left", padding: "1px" }}>               
                        <ReactPaginate
                            previousLabel={'Previous'}
                            nextLabel={'Next'}
                            pageCount={Math.ceil(carts.length / itemsPerPage)}
                            onPageChange={onPageChange}
                            containerClassName={'pagination'}
                            previousLinkClassName={'pagination__link'}
                            nextLinkClassName={'pagination__link'}
                            disabledClassName={'pagination__link--disabled'}
                            activeClassName={'pagination__link--active'}
                        />
                    </div>
                    <div style={{ float: "right", padding: "1px" }}>
                        <Button variant="outline-dark" style={{ width: "100%" }} onClick={confirmList}> Xác nhận đơn hàng </Button>
                    </div>
                </div>
            </div>

        </>
    )
}

export default Cart;