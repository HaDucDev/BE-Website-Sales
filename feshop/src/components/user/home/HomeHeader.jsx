import { useNavigate } from "react-router-dom";
import "./home-header.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSearch } from '@fortawesome/free-solid-svg-icons'
import { useEffect, useState } from "react";
import categoryService from "../../../services/admin/admin.category.service";
import productServiceUser from "../../../services/user/user.product.service";
const HomeHeader = () => {

    const [inputValue, setInputValue] = useState("");// value thanh tim kiem
    const [categoryList, setCategoryList] = useState([]);
    const [supplierFormCategortList, setSupplierFromCatgoryList] = useState([])


    const nav = useNavigate();

    const handleSendText = (e) => {
        if (e.key === 'Enter') {
            console.log(inputValue);
            nav(`/search-filter?textSearch=${inputValue}`)
        }
    }


    const handleMouseEnter = (id) => {
        productServiceUser.getAllSupplierFromCatgoryService(id).then((dataResponse) => {
            setSupplierFromCatgoryList(dataResponse.data);
        }).catch((e) => alert(e.response.data))
    };

    useEffect(() => {
        categoryService.getAllCategoryService().then((dataResponse) => {
            setCategoryList(dataResponse.data);
        }).catch((e) => alert(e));
    }, [])

    return (
        <>
            <div className="search-container">
                <input
                    type="text"
                    className="search-input"
                    placeholder="Tìm kiếm sản phẩm..."
                    onChange={(e) => { setInputValue(e.target.value) }}
                    //onKeyPress={handleKeyPress}
                    onKeyDown={handleSendText}
                    key={"hd123456opk"}
                />
                <button key={("navbar-item" + 6).toString()}
                    className="search-btn" onClick={(e) => {
                        e.preventDefault();
                        nav(`/search-filter?textSearch=${inputValue}`)
                    }}>
                    <FontAwesomeIcon icon={faSearch} />
                </button>
            </div>
            <nav className="navbar">
                <ul className="navbar-menu">
                    <li className="navbar-item" key={("navbar-item" + 1).toString()}>
                        <a href="/khongdung" className="navbar-link navbar-logo" onClick={(e) => { e.preventDefault(); nav("/") }}>Trang chủ</a>
                    </li>
                    <li className="navbar-item" key={("navbar-item" + 2).toString()}>
                        <a href="/" className="navbar-link" key={("navbar-item" + 7).toString()}>Danh mục</a>
                        <ul className="navbar-submenu" key={("navbar-item" + 3).toString()}>
                            {
                                categoryList.map((item, index) => (
                                
                                        <li key={item.categoryId} className="navbar-submenu-item" style={{ width: "125px" }}
                                            onMouseEnter={() => handleMouseEnter(item.categoryId)}
                                            onClick={(e) => {e.preventDefault(); nav(`/search-filter?categoryId=${item.categoryId}`) }}>
                                            {item.categoryName}
                                            <ul className="navbar-submenu-item-submenu" key={`${index}${item.categoryId}`}>
                                                {
                                                    supplierFormCategortList.map((item1) => (
                                                   
                                                            <li key={`${item1.supplierId}`}
                                                                className="navbar-submenu-item" style={{ width: "200px" }}
                                                                onClick={(e) => {e.preventDefault(); 
                                                                    e.stopPropagation(); // ngăn chặn sự kiện click của thẻ cha
                                                                    nav(`/search-filter?categoryId=${item.categoryId}&supplierId=${item1.supplierId}`) }}
                                                            >
                                                                {item1.supplierName}
                                                            </li>
                                                    
                                                    ))
                                                }

                                            </ul>
                                        </li>
                                  
                                )

                                )
                            }

                        </ul>
                    </li>
                    <li className="navbar-item" key={("navbar-item" + 4).toString()}>
                        <a href="/policy" className="navbar-link" onClick={(e) => { e.preventDefault(); nav("/") }}
                        key={("navbar-item" + 8).toString()}>Chính sách bán hàng</a>
                    </li>
                    <li className="navbar-item" key={("navbar-item" + 5).toString()}>
                        <a href="/contact" className="navbar-link" onClick={(e) => { e.preventDefault(); nav("/") }}
                        key={("navbar-item" + 9).toString()}>Liên hệ</a>
                    </li>
                </ul>
            </nav>

        </>
    );
};

export default HomeHeader;
