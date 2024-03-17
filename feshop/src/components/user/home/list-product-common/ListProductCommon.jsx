
import React, { useEffect, useState } from 'react';
import { Card, Button, Image } from 'react-bootstrap';
import './list-product-home.css';
import productServiceUser from '../../../../services/user/user.product.service';
import { Link } from 'react-router-dom';
import ReactPaginate from 'react-paginate';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import StarRatings from 'react-star-ratings';
import cartServiceUser from '../../../../services/user/user.cart.service';
import { useContext } from 'react';
import { LoginContext } from '../../../../App';
const ListProductCommon = () => {
    //npm i --save-dev @types/react-pagination-library

    const [listProductHome, setListProductHome] = useState([])//state san pham trang home
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);//tong so trang san pham
    const sizes = [4, 8, 16];// so luong san pham 1 trang
    const [size, setSize] = useState(8);
    const [listProductSelling, setListProductSelling] = useState([])//state san pham ban chay

    useEffect(() => {
        productServiceUser.getSellingTop10Service().then((responseData) => {
            setListProductSelling(responseData.data);
        }).catch(error => alert("Lỗi " + error + ". Bạn hãy quay lại sau."));
    }, [])

    useEffect(() => {
        productServiceUser.getAllHomeProductService(page, size).then((responseData) => {
            setListProductHome(responseData.data.content);//data    
            setTotalPages(responseData.data.totalPages);//so trang   
        }).catch(error => alert("Lỗi " + error + ". Bạn hãy quay lại sau."));
    }, [page, size])

    const onPageChange = ({ selected }) => {
        setPage(selected);
    }

    const settings = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 3,
        slidesToScroll: 1
    };
    const textLogin = useContext(LoginContext);
    const addProductToCart = (id) => {
        if (!localStorage.getItem("currentUser")) {
            alert("Bạn phải đăng nhập mới có thể thêm sản phẩm vào giỏ hàng")
        }
        else {
            let data = {
                "userId": JSON.parse(localStorage.getItem("currentUser")).userId,
                "productId": Number(id),
                "quantity": 1,
                "operator": "add"
            }
            cartServiceUser.addProductToCartService(data).then((dataResponse) => {
                let dataShow = dataResponse.data;
                textLogin.setLoadPage(Math.floor(Math.random() * 10) + 1);
                alert(dataShow["message"]);
            }).catch((err) => {
                console.log(err)
                let errorShow = err.response.data;
                alert(errorShow["message"]);
            })
        }
    }

    return (
        <>

            <div style={{ marginBottom: "50px", width: "90%", margin: "5%" }}>
                <h4 style={{ textAlign: "center" }}>Sản phẩm bán chạy</h4>
                <Slider {...settings}>
                    {listProductSelling.map((item, index) => (
                        <Card key={index} className="card-container">
                            <div>
                                <Image src={item.productImage} fluid className="card-image" style={{ borderBottom: '2px solid #ddd', width: "40%", float: "left" }} />
                                <div style={{ backgroundColor: "blue", color: "white" }}>Giảm giá: {item.discount}%</div>
                                <Card.Text className="card-text">Giá bán: {item.unitPrice - item.unitPrice * item.discount / 100}đ  <s>{item.unitPrice} đ</s></Card.Text>
                                <Card.Text className="card-text">Đã bán: {item.sumQuantity} sản phẩm</Card.Text>
                                <div>{(item.rating).toFixed(1)}
                                    <StarRatings
                                        rating={1}
                                        starRatedColor="yellow"
                                        //changeRating={changeRating}
                                        numberOfStars={1} // tong so sao tuy y
                                        name='rating'
                                    />
                                </div>
                            </div>
                            <Card.Body style={{ clear: "both" }}>
                                <div style={{}}>{item.productName}</div>
                                <Link to={`/product-detail/${item.productId}`} className="btn-click">
                                    <Button variant="primary" className="card-button">Chi tiết sản phẩm</Button>
                                </Link>
                            </Card.Body>
                        </Card>
                    ))}
                </Slider>
            </div>
            <div className="card-list-container" >
                {listProductHome.map((item, index) => (
                    <Card key={index} className="card-container">
                        <Image src={item.productImage} fluid className="card-image" style={{ borderBottom: '2px solid #ddd' }} />
                        <Card.Body>
                            <Card.Title className="card-title" style={{ height: "28%" }}>{item.productName}</Card.Title>
                            <Card.Text className="card-text">{item.unitPrice} đ</Card.Text>
                            <Button variant="primary" className="card-button"
                                onClick={() => addProductToCart(item.productId)}>Thêm vào giỏ hàng</Button>
                            <Link to={`/product-detail/${item.productId}`} className="btn-click">
                                <Button variant="primary" className="card-button">Chi tiết sản phẩm</Button>
                            </Link>
                        </Card.Body>
                    </Card>
                ))}
            </div>
            <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "50px" }}>
                <select onChange={(e) => { setSize(e.target.value); }} value={size} style={{ display: "flex", justifyContent: "center", alignItems: "center", width: "12%" }}>
                    {sizes.map((size) => (
                        <option key={size} value={size}>
                            {size} sản phẩm trang này
                        </option>
                    ))}
                </select>
                <ReactPaginate
                    previousLabel={'Previous'}
                    nextLabel={'Next'}
                    pageCount={totalPages}
                    onPageChange={onPageChange}
                    containerClassName={'pagination'}
                    previousLinkClassName={'pagination__link'}
                    nextLinkClassName={'pagination__link'}
                    disabledClassName={'pagination__link--disabled'}
                    activeClassName={'pagination__link--active'} // mau cua phan trang
                />
            </div>
        </>
    );

}

export default ListProductCommon;