import { useEffect, useState } from 'react';
import { useContext } from 'react';
import { Button } from 'react-bootstrap';
import { Cart } from 'react-bootstrap-icons';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Offcanvas from 'react-bootstrap/Offcanvas';
import { useNavigate } from 'react-router';
import { LoginContext } from '../../App';
import cartServiceUser from '../../services/user/user.cart.service';
// ui React bootstrap https://react-bootstrap.github.io/components/navbar/#responsive-behaviors
const HeaderApp = () => {

  const [countCart, setCountCart] = useState(0);
  console.log("ok123456-render-header-app");
  const textLogin = useContext(LoginContext);
  useEffect(() => {
    console.log("ok123456");
    console.log(textLogin.loadPage);
  }, [textLogin.loadPage]);

  const doneLogin = localStorage.getItem("currentUser");
  if (doneLogin) {
    cartServiceUser.getCountProductCategoryService(JSON.parse(doneLogin).userId).then((dataResponse) => {
      setCountCart(dataResponse.data);
    })
  }

  const nav = useNavigate();
  return (
    <>
      {['sm'].map((expand) => (
        <Navbar key={expand} bg="light" expand={expand} className="mb-3" style={{ zIndex: 100 }}>
          <Container fluid>
            <Navbar.Brand href="#" onClick={(e) => { e.preventDefault(); nav("/") }}><img src="https://res.cloudinary.com/dkdyl2pcy/image/upload/v1676874555/hdshop_a8eqd5.png" alt='lỗi'
              style={{ height: "40px", width: "100%", objectFit: "cover", borderRadius: "50%" }} />
            </Navbar.Brand>
            <Navbar.Toggle aria-controls={`offcanvasNavbar-expand-${expand}`} />
            <Navbar.Offcanvas id={`offcanvasNavbar-expand-${expand}`} aria-labelledby={`offcanvasNavbarLabel-expand-${expand}`} placement="end">
              <Offcanvas.Header closeButton>
                <Offcanvas.Title id={`offcanvasNavbarLabel-expand-${expand}`}>
                  Offcanvas
                </Offcanvas.Title>
              </Offcanvas.Header>
              <Offcanvas.Body>
                <Nav className="justify-content-end flex-grow-1 pe-3">
                  <Nav.Link href="#action1" onClick={
                    (e) => {
                      e.preventDefault();
                      (doneLogin) ? (nav("/cart")) : (alert("Bạn phải đăng nhập mới có thể xem giỏ hàng"))
                    }
                  } style={{ marginRight: "10px" }}>
                    Giỏ hàng<Cart size={24} className="cursor-pointer" />
                    <span style={{ fontSize: "20px", backgroundColor: "yellow", borderRadius: "50%", color: "red" }}>{countCart}</span>
                  </Nav.Link>
                  {
                    (doneLogin) ? (
                      <>
                        <img src={JSON.parse(doneLogin).avatar ? JSON.parse(doneLogin).avatar : "https://res.cloudinary.com/dkdyl2pcy/image/upload/v1676872862/avatar-default-9_rv6k1c.png"} alt='lỗi'
                          style={{ height: "40px", width: "40px", objectFit: "cover", borderRadius: "50%" }} />
                        <NavDropdown title={JSON.parse(doneLogin).username} id={`offcanvasNavbarDropdown-expand-${expand}`}>
                          {

                            (JSON.parse(doneLogin).roleName === "ROLE_ADMIN") ? (
                              <>
                                <NavDropdown.Item href="#action0" onClick={
                                  (e) => {
                                    e.preventDefault();
                                    nav("/admin")
                                  }
                                }>Trang quản trị</NavDropdown.Item></>
                            ) : ''
                          }
                          <NavDropdown.Item href="#action3" onClick={
                            (e) => {
                              e.preventDefault();
                              nav("/history-order")
                            }
                          }>Lịch sử đơn hàng</NavDropdown.Item>
                          <NavDropdown.Item href="#action4" onClick={
                            (e) => {
                              e.preventDefault();
                              nav("/change-infor-user")
                            }
                          }>
                            Thông tin cá nhân
                          </NavDropdown.Item>
                          <NavDropdown.Divider />
                          <NavDropdown.Item href="#action5" onClick={
                            (e) => {
                              e.preventDefault();
                              nav("/change-pasword")
                            }
                          }>
                            Đổi mật khẩu
                          </NavDropdown.Item>
                        </NavDropdown>
                      </>
                    ) : ("")
                  }
                  {
                    (doneLogin) ? (
                      <Button variant="outline-success" onClick={(e) => {
                        e.preventDefault();
                        localStorage.removeItem("currentUser");
                        sessionStorage.clear();
                        nav("/")
                        setCountCart(0);
                      }}>Đăng xuất</Button>
                    ) :
                      (<Button variant="outline-success" onClick={(e) => { e.preventDefault(); nav("/login") }}>Đăng nhập/Đăng kí</Button>)
                  }
                </Nav>
              </Offcanvas.Body>
            </Navbar.Offcanvas>
          </Container>
        </Navbar>
      ))}

    </>
  )
}

export default HeaderApp;