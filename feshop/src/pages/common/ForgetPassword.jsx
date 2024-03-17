import { useState } from "react";
import { Button, Form, Spinner } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import accountService from "../../services/account/account.service";


const ForgetPassword = () => {

    const [openInputUpdate, setopenInputUpdate] = useState(false);// do thuoc tinh readOnly true ms khoa input

    const [isSubmitting, setIsSubmitting] = useState(false);// state nut chi nhan dc mot lan
    const [isLoading, setIsLoading] = useState(false);// tao spiner quay de biet data dang gui, cham 1 ti 
    const [isLoading1, setIsLoading1] = useState(false);// tao spiner quay de biet data dang gui, cham 1 ti - spinner 1

    const [inforSendForget, setInforSendForget] = useState({
        email: "",
        code: ""
    })

    const nav = useNavigate();

    const handleSendMail = (e) => {
        e.preventDefault();
        setIsSubmitting(true);// khoa nut
        setIsLoading(true);// mo quay tron
        let dataRequest = {
            email: inforSendForget.email
        }
        accountService.forgetSendMailService(dataRequest).then((dataResponse) => {
            let dataUser = dataResponse.data;
            alert(dataUser["message"]);
            setopenInputUpdate(true);//khoa input, show input code and change button
            setIsLoading(false);//tat spiner
            setIsSubmitting(false);// mo nut button
        }).catch((err) => {
            let errorShow = err.response.data;
            alert(errorShow["message"]);
        })
    }

    const comfirmForgetPass = (e) => {
        e.preventDefault();
        setIsLoading1(true);// mo quay tron
        let dataRequest = {
            email: inforSendForget.email,
            resetCode: inforSendForget.code
        }
        accountService.cofirmCodeFromEmailService(dataRequest).then((dataResponse) => {
            let dataUser = dataResponse.data;
            setIsLoading1(false);//tat spiner
            alert(dataUser["message"]);
            nav("/login");

        }).catch((err) => {
            let errorShow = err.response.data;
            alert(errorShow["message"]);
        })
    }

    return (
        <>
            <div style={{ height: "60vh" }} className="d-flex justify-content-center align-items-center">
                <div style={{ border: "1px solid #2522ca", padding: "10px", borderRadius: "5%", width: "30%" }}>
                    <h3 className="text-center">Quên mật khẩu</h3>
                    <Form>
                        <Form.Group style={{ clear: "both" }}>
                            <Form.Label>Email:</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập email để lấy mã xác nhận"
                                defaultValue={inforSendForget.email}
                                readOnly={openInputUpdate}
                                onChange={e => {
                                    setInforSendForget({ ...inforSendForget, email: e.target.value })
                                }}
                            />
                        </Form.Group>
                        {
                            openInputUpdate && (
                                <Form.Group style={{ clear: "both" }}>
                                    <Form.Label>Nhập mã xác nhận:</Form.Label>
                                    <Form.Control
                                        type="text"
                                        placeholder="Mã xác nhận gửi từ email"
                                        defaultValue={inforSendForget.code}
                                        onChange={e => {
                                            setInforSendForget({ ...inforSendForget, code: e.target.value })
                                        }}
                                    />
                                </Form.Group>
                            )
                        }
                        {
                            openInputUpdate ? (<Button variant="outline-primary" className="w-100 mt-3"
                                style={{ zIndex: "1" }}
                                onClick={comfirmForgetPass}>
                                {isLoading1 && (
                                    <Spinner
                                        style={{ margin: "auto", zIndex: "9" }}
                                        animation="border"
                                        variant="warning"
                                    />
                                )}
                                Xác nhận
                            </Button>) : (
                                <>
                                    <Button variant="primary" type="submit" className="w-100 mt-3"
                                        onClick={handleSendMail}
                                        disabled={isSubmitting}
                                    >
                                        {isLoading && (
                                            <Spinner
                                                style={{ margin: "auto", zIndex: "9" }}
                                                animation="border"
                                                variant="warning"
                                            />
                                        )}
                                        Gửi mã xác nhận</Button>
                                </>
                            )
                        }
                    </Form>
                </div>
            </div>
        </>
    )
}

export default ForgetPassword;