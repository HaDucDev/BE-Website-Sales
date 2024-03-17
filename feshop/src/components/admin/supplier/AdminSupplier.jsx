import React, { useEffect, useState } from "react";
import { Button, Form, Modal, Spinner } from "react-bootstrap";
import DataTable from "react-data-table-component";
import 'bootstrap/dist/css/bootstrap.css';
import convert_vi_to_en from "./../../../utils/utils";
import ValidationMessage from "../../acommon-component/ValidationMessage";
import supplierService from "../../../services/admin/admin.supplier.service";
const AdminSupplier = () => {

    const [supplierList, setSupplierList] = useState([]);// state datatable

    const [intiText, setText] = useState("");// state search

    const [showAddModal, setShowAddModal] = useState(false);// state bat/tat modal create

    const [value, setValue] = useState("");// state supplier ban dau khi modal add
    const [load, setLoadTable] = useState(false);// state load khi them thanh cong
    const [errorResponse, setErrorResponse] = useState({
        supplierName: ""
    });//state error

    const [selectedFile, setSelectedFile] = useState(null);

    const [supplierById, setSupplierById] = useState({
        supplierId: -1,
        supplierName: "",
        supplierImage: ""
    })// state getbyId

    const [showUpdateModal, setShowUpdateModal] = useState(false);// state bat/tat modal update

    const [isSubmitting, setIsSubmitting] = useState(false);// state nut chi nhan dc mot lan
    const [isLoading, setIsLoading] = useState(false);// tao spiner quay de biet data dang gui, cham 1 ti

    const [confirmModal, setComfirmModal] = useState(false);//state bat/tat modal comfirm
    const search = (data) => {
        return data.filter(row => convert_vi_to_en(row.supplierName.toLowerCase()).indexOf(convert_vi_to_en(intiText.toLowerCase())) > -1
            || row.supplierId.toString().includes(intiText));
    }

    useEffect(() => {
        supplierService.getAllSupplierService().then((response) => {
            setSupplierList(response.data)
        }).catch(error => alert("Lỗi " + error + ". Bạn hãy quay lại sau."));
    }, [intiText, load])

    // them supplier
    const handleAddSupplier = () => {
        setIsSubmitting(true);// khoa nut
        setIsLoading(true);// mo quay tron
        let dataRequest = {
            supplierName: ""
        }
        dataRequest.supplierName = value;
        let fileRequest = selectedFile;
        supplierService.createCSupplierService(dataRequest,fileRequest).then((dataResponse) => {
            let dataShow = dataResponse.data;
            setValue("")
            setSelectedFile(null)
            alert(dataShow["message"]);
            setLoadTable(!load);
            setIsLoading(false);//tat spiner
            setIsSubmitting(false);// mo nut
            setShowAddModal(false);
        })
            .catch((err) => {
                console.log(err)
                let errorShow = err.response.data;
                console.log(errorShow);
                setIsLoading(false);// tat spinner
                setErrorResponse(errorShow);
            })
    }
    //get by id
    const handleGetById = (id) => {
        supplierService.getSupplierById(id).then((dataResponse) => {
            let supplierDetail = dataResponse.data;
            setSupplierById({
                supplierId: id,
                supplierName: supplierDetail.supplierName,
                supplierImage: supplierDetail.supplierImage
            });
            setShowUpdateModal(true);
        }).catch((e) => alert(e.response.data))
    }

    const handleUpdateSupplier = () => {
        const dataRequest = {
            supplierId: -1,
            supplierName: ""
        }
        dataRequest.supplierId = supplierById.supplierId;
        dataRequest.supplierName = supplierById.supplierName
        let fileResquest =selectedFile;

        supplierService.updateSupplierService(dataRequest,fileResquest).then((dataResponse) => {
            let dataShow = dataResponse.data;
            setSelectedFile(null);// ko can dat cac thong so con lai
            alert(dataShow["message"]);
            setLoadTable(!load);
            setShowUpdateModal(false);
        }).catch((e) => alert(e.response.data))
    }

    //delete supplier
    const comfirmDeleteSupplier = (id) =>{
        setSupplierById({...supplierById, supplierId:id});
        setComfirmModal(true);
    }

    const handleDeleteSupplier =()=>{
        supplierService.deleteSupplierService(supplierById.supplierId).then((dataResponse) => {
            let dataShow = dataResponse.data;
            alert(dataShow["message"]);
            setLoadTable(!load);
            setComfirmModal(!confirmModal);
        }).catch((err) => {
            let errorShow = err.response.data;
            setErrorResponse(errorShow);
        })
    }

    const colunmns = [
        {
            name: "Supplier id",
            selector: row => row.supplierId,
            sortable: true, // nha ten cot thi sort
            center: true
        },
        {
            name: "Supplier name",
            selector: row => row.supplierName,
            center: true
        }
        ,
        {
            name: "Supplier Image",
            selector: (row) => <img width={50} height={50} src={row.supplierImage} alt="lỗi ảnh" />
        }
        , {
            name: "Action",
            cell: (row) => {
                return <>
                    <div style={{ margin: "auto" }}>
                        <Button variant="outline-dark" onClick={() => { setErrorResponse({ supplierName: "" }); handleGetById(row.supplierId) }}>SỬA</Button>
                        <button style={{ marginLeft: "5px" }} className="btn btn-primary" onClick={() => comfirmDeleteSupplier(row.supplierId)}>XÓA</button> 
                    </div>

                </>
            },
            // style: {
            //     backgroundColor: 'rgba(187, 204, 221, 1)',
            // },
            center: true
        }
    ];

    const customStyles = {// css datatable
        headCells: {
            style: {
                backgroundColor: "#C0D5FF"
            },
        },
    };


    return (
        <>
            <DataTable
                title="Admin supplier list"
                columns={colunmns}
                data={search(supplierList)}
                pagination
                fixedHeader // thanh keo cua bang
                fixedHeaderScrollHeight="400px" // cho cai thanh keo 400px va sat thanh keo cua page luon
                highlightOnHover // dua chuot vo dong doi mau
                paginationIconFirstPage
                responsive
                paginationPerPage={5}
                paginationRowsPerPageOptions={[5, 15, 23, 50]}
                subHeader
                subHeaderComponent={
                    <>
                        <input type="text" placeholder="search here" className="w-25 form-control"
                            value={intiText} onChange={(e) => setText(e.target.value)} />
                        <Button style={{ marginLeft: "10px" }} variant="outline-dark"
                            onClick={() => {
                                setErrorResponse({ supplierName: "" });
                                setShowAddModal(true)
                            }}>THÊM</Button>
                    </>
                }
                subHeaderAlign="right"
                customStyles={customStyles}
            />

            {/* Modal them supplier*/}
            <Modal show={showAddModal} onHide={() => setShowAddModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Thêm supplier</Modal.Title>
                </Modal.Header>
                <Modal.Body>

                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                        <Form.Label>Tên hãng</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Nhập tên hãng"
                            onChange={(e) => {
                                (setValue(e.target.value));
                                setErrorResponse({
                                    supplierName: ""
                                })
                                setIsSubmitting(false);// mo nut
                            }}
                        />
                        <ValidationMessage
                            errorResponse={errorResponse}
                            field="supplierName" />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput2">
                        <Form.Label>Chọn ảnh cho hãng</Form.Label>
                        <Form.Control
                            type="file"
                            onChange={(e) => {
                                (setSelectedFile(e.target.files[0]));
                            }}
                        />
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowAddModal(false)}>
                        Đóng
                    </Button>

                    <Button variant="outline-primary" disabled={isSubmitting}
                        style={{ zIndex: "1" }}
                        onClick={() => handleAddSupplier()}>
                        {isLoading && (
                            <Spinner
                                style={{ margin: "auto", zIndex: "9" }}
                                animation="border"
                                variant="primary"
                            />
                        )}
                        Lưu
                    </Button>


                </Modal.Footer>
            </Modal>

            {/* Modal sửa supplier*/}
            <Modal show={showUpdateModal} onHide={() => setShowUpdateModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Sửa supplier </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3">
                        <Form.Label>Mã supplier</Form.Label>
                        <Form.Control
                            type="number"
                            value={supplierById.supplierId}
                            readOnly
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Tên supplier</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Nhập tên danh mục"
                            onChange={(e) => {
                                (setSupplierById({
                                    ...supplierById,
                                    supplierName: e.target.value
                                }));
                                setErrorResponse({
                                    supplierName: ""
                                });
                            }}
                            defaultValue={supplierById.supplierName}
                        />
                        <ValidationMessage
                            errorResponse={errorResponse}
                            field="supplierName" />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label >Ảnh hãng</Form.Label>
                        <div style={{ display: "flex" }}>
                            <div>
                                <img style={{ border: "1px solid black" }} src={supplierById.supplierImage} width={100} height={100} alt="lỗi ảnh" />
                            </div>
                            <div style={{ margin: "auto" }}>
                                <Form.Label >Đổi ảnh</Form.Label>
                                <Form.Control type="file" onChange={(e) => {
                                    (setSelectedFile(e.target.files[0]));
                                }} />
                            </div>
                        </div>
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowUpdateModal(!showUpdateModal)}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => handleUpdateSupplier()}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal>

            {/* Modal Xác nhận Supplier*/}
            <Modal show={confirmModal} onHide={() => setComfirmModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Xác nhận</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p> {`Bạn có muốn xóa sản phẩm "${supplierById.supplierId}" không?`}</p>              
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setComfirmModal(!confirmModal)}>
                        Không
                    </Button>
                    <Button variant="primary" onClick={() => handleDeleteSupplier()}>
                        Có
                    </Button>
                </Modal.Footer>
            </Modal>


        </>
    )

}

export default AdminSupplier