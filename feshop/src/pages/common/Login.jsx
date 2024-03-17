import { useContext, useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { LoginContext } from '../../App';
import accountService from '../../services/account/account.service';
const Login = () => {

  const [isShow, setIsShow] = useState(false);
  const [loginAccount, setLoginaccount] = useState({
    username: "",
    password: ""
  })

  const nav= useNavigate();

  const textLogin = useContext(LoginContext);

  const handleLogin =(e) =>{
    e.preventDefault();
      accountService.loginService(loginAccount).then((dataResponse)=>{
        let dataUser= dataResponse.data;
        localStorage.setItem("currentUser",JSON.stringify(dataUser));
        const getcurrentUser=JSON.parse(localStorage.getItem("currentUser"));
        console.log("Người dùng "+ getcurrentUser.username +" đang dăng nhập");
        textLogin.setLoadPage(1);
        if(getcurrentUser){
            if(getcurrentUser.roleName==="ROLE_ADMIN"){
              nav("/admin")
            }
            if(getcurrentUser.roleName==="ROLE_CUSTOMER"){
              nav("/")
            }
            if(getcurrentUser.roleName==="ROLE_SHIPPER"){
              nav("/shipper")
            }
        }
        
      }).catch((err) => {
        let errorShow = err.response.data;
        alert(errorShow["message"]);
    })

  }

  return (
    <>
      <div
        style={{ height: "100vh" }}
        className="d-flex justify-content-center align-items-center"
      >
        <div style={{ width: 500, border: "1px solid #2522ca", padding: "40px", borderRadius: "5%" }}>
          <h1 className="text-center">Đăng nhập</h1>
          <Form>
            <Form.Group>
              <Form.Label>Tên đăng nhập</Form.Label>
              <Form.Control
                type="text"
                placeholder="Nhập tên đăng nhập"
                onChange={e => {
                  setLoginaccount({ ...loginAccount, username: e.target.value })
                }}
              />
            </Form.Group>

            <Form.Group>
              <Form.Label>Mật khẩu</Form.Label>
              <Form.Control
                type={isShow ? "text" : "password"}
                placeholder="Mật khẩu"
                onChange={e => {
                  setLoginaccount({ ...loginAccount, password: e.target.value })
                }}
              />
            </Form.Group>
            <div>
              <input
                id="checkbox"
                type="checkbox"
                checked={isShow}
                onChange={(e) => setIsShow(e.target.checked)}
                style={{ marginRight: '5px' }}
              />
              <label htmlFor="checkbox">  Hiển thị mật khẩu</label>
            </div>
            <Button variant="primary" type="submit" className="w-100 mt-3" onClick={handleLogin}> Đăng nhập</Button>
            <div>
              <Link to="/register" style={{ float: "left" }}>Đăng kí tài khoản</Link>
              <Link to="/forget-pass" style={{ float: "right" }}>Quên mật khẩu</Link>
            </div>
          </Form>
        </div>
      </div>
    </>
  );
}

export default Login;