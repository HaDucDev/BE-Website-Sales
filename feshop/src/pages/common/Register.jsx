import { useState } from "react";
import { Button, Form, Spinner } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import accountService from "../../services/account/account.service";


const Register = () => {

    const [isSubmitting, setIsSubmitting] = useState(false);// state nut chi nhan dc mot lan
    const [isLoading, setIsLoading] = useState(false);// tao spiner quay de biet data dang gui, cham 1 ti

    const [registerAccount, setRegisterAccount] = useState({
        email: "",
        fullName: "",
        username: "",
        address: "",
        phone: ""
    })

    const nav = useNavigate();

    const handleRegister = (e) => {
        e.preventDefault();
        setIsSubmitting(true);// khoa nut
        setIsLoading(true);// mo quay tron
        accountService.registerService(registerAccount).then((dataResponse) => {
            let dataUser = dataResponse.data;
            setIsLoading(false);//tat spiner
            setIsSubmitting(false);// mo nut
            alert(dataUser["message"]);
            nav("/login");
        }).catch((err) => {
            let errorShow = err.response.data;
            alert(errorShow["message"]);
        })

    }

    return (
        <>
            <div style={{ height: "85vh" }} className="d-flex justify-content-center align-items-center">
                <div style={{ width: 500, border: "1px solid #2522ca", padding: "10px", borderRadius: "5%" }}>
                    <h3 className="text-center">Đăng kí</h3>
                    <Form>
                        <Form.Group>
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập tên email"
                                onChange={e => {
                                    setRegisterAccount({ ...registerAccount, email: e.target.value })
                                }}
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập họ và tên"
                                onChange={e => {
                                    setRegisterAccount({ ...registerAccount, fullName: e.target.value })
                                }}
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Tên đăng nhập</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập tên đăng nhập"
                                onChange={e => {
                                    setRegisterAccount({ ...registerAccount, username: e.target.value })
                                }}
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Địa chỉ</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập địa chỉ"
                                onChange={e => {
                                    setRegisterAccount({ ...registerAccount, address: e.target.value })
                                }}
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Số điện thoại</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Số điện thoại"
                                onChange={e => {
                                    setRegisterAccount({ ...registerAccount, phone: e.target.value })
                                }}
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100 mt-3"
                            onClick={handleRegister} disabled={isSubmitting}>
                            {isLoading && (
                                <Spinner
                                    style={{ margin: "auto", zIndex: "9" }}
                                    animation="border"
                                    variant="warning"
                                />
                            )} Đăng kí</Button>
                        <div>
                            <Link to="/" style={{ float: "left" }}>Quay lại đăng nhập</Link>
                            <Link to="/forget-pass" style={{ float: "right" }}>Quên mật khẩu</Link>
                        </div>
                    </Form>
                </div>
            </div>
        </>
    )

}

export default Register;