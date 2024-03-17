import { useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import accountService from '../../services/account/account.service';
const ChangePassword = () => {

    const [isShow, setIsShow] = useState(false);
    const [changePass, setChangePass] = useState({
        passOld: "",
        passnew: "",
        passConfirm: ""
    })
    const nav = useNavigate();

    const handleChangePass = (e) => {
        if (changePass.passnew === changePass.passConfirm) {
            e.preventDefault();
            let dataRequest = {
                oldPassword: changePass.passOld,
                newPassword: changePass.passnew,
            }
            accountService.changePassService(dataRequest).then((dataResponse) => {
                let dataUser = dataResponse.data;
                alert(dataUser["message"]);
                nav("/login");
            }).catch((err) => {
                let errorShow = err.response.data;
                alert(errorShow["message"]);
            })

        }
    }

    return (
        <>
            <div style={{ height: "85vh" }} className="d-flex justify-content-center align-items-center">
                <div style={{ width: 500, border: "1px solid #2522ca", padding: "10px", borderRadius: "5%" }}>
                    <h3 className="text-center">Đổi mật khẩu</h3>
                    <Form>
                        <Form.Group>
                            <Form.Label>Mật khẩu cũ</Form.Label>
                            <Form.Control
                                type={isShow ? "text" : "password"}
                                placeholder="Mật khẩu cũ"
                                onChange={e => {
                                    setChangePass({ ...changePass, passOld: e.target.value })
                                }}
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Mật khẩu mới</Form.Label>
                            <Form.Control
                                type={isShow ? "text" : "password"}
                                placeholder="Mật khẩu mới"
                                onChange={e => {
                                    setChangePass({ ...changePass, passnew: e.target.value })
                                }}
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Nhập lại mật khẩu mới</Form.Label>
                            <Form.Control
                                type={isShow ? "text" : "password"}
                                placeholder="Nhập lại mật khẩu mới"
                                onChange={e => {
                                    setChangePass({ ...changePass, passConfirm: e.target.value })
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
                        <Button variant="primary" type="submit" className="w-100 mt-3"
                            onClick={handleChangePass}
                        >
                            Đổi mật khẩu</Button>
                    </Form>
                </div>
            </div>
        </>
    )
}

export default ChangePassword;