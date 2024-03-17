import React, { useEffect, useState } from "react";
import {Outlet, useNavigate } from "react-router-dom";
import "./css/admin.css";


const sideBar = [
  {
    id: 0,
    link: "/admin/user",
    text: "Tài khoản người dùng",

  },
  {
    id: 1,
    link: "/admin/category",
    text: "Danh mục",
  },
  {
    id: 2,
    link: "/admin/supplier",
    text: "Nhà cung cấp",

  },
  {
    id: 3,
    link: "/admin/product",
    text: "Sản phẩm",
  },
  {
    id: 4,
    link: "/admin/orders",
    text: "Đơn hàng",
  },
  {
    id: 5,
    link: "/admin/statistical",
    text: "Thống kê",

  },
  // {
  //   id: 6,
  //   link: "/admin/reviews",
  //   text: "Đánh giá",
  // },
];

const Admin = () => {

  const nav = useNavigate();
  const user = JSON.parse(localStorage.getItem("currentUser"));

  //const textLogin = useContext(LoginContext);

  useEffect(() => {
    if (user == null || user === "" || user.roleName !== "ROLE_ADMIN") {
      alert("Bạn không có quyền vào trang này");
      nav("/");
    }
    else{
      //textLogin.setLoadPage(1);
    }
  })
  const [activeIndex, setActiveIndex] = useState(null);
  return (
    <>
      {
        !user ? "" : (
          <div className="admin" style={{ marginTop: "0px", marginRight: "0px" }}>
            <div className="sidebar" style={{}}>
              <div className="sidebar_menu" >
                <img style={{ margin: "auto", marginBottom: "5px", marginTop: "0px",borderRadius: "50%"  }}
                  src={ user ? user.avatar : "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwbGozsS9QP10p16rZiCrQD0koXVkI4c7LwUHab9dkmFRcN0VqCkB37f2y0EnySItwykg&usqp=CAU"}
                  alt=""
                  width="200"
                  height="200"
                />
                {sideBar.length > 0 &&
                  sideBar.map((item, index) => (
                    <div
                      key={index}
                      className={`sidebar_menu_item ${activeIndex === index && "active"
                        } relative`}
                      onClick={(e) => {
                        e.preventDefault();                  
                        setActiveIndex(item.id);
                        nav(`${item.link}`)                    
                      }}
                    >
                      {item.text}
                    </div>
                  ))}
              </div>
            </div>
            <div style={{ zIndex: "100", width: "72%", marginLeft: "25%" }}>
              {
                console.log("ok nhes")
              }
              <Outlet /> {/* Hien thi cac trang con */}
            </div>
          </div>
        )
      }

    </>
  )
}

export default Admin;