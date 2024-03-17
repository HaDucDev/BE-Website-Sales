import React, {  useEffect, useState } from "react";
import DataTable from "react-data-table-component";
import 'bootstrap/dist/css/bootstrap.css';
import { Button, Form, Modal } from "react-bootstrap";
import convert_vi_to_en from "./../../../utils/utils";
import categoryService from "../../../services/admin/admin.category.service";
import ValidationMessage from "../../acommon-component/ValidationMessage";

const AdminCategory = () => {


    const [categoryList, setCategoryList] = useState([]);// state datatable

    const [intiText, setText] = useState("");// state search

    const [showAddModal, setShowAddModal] = useState(false);// state bat/tat modal create

    const [value, setValue] = useState("");// state category ban dau khi modal add
    const [load, setLoadTable] = useState(false);// state load khi them thanh cong
    const [errorResponse, setErrorResponse] = useState({
        categoryName: ""
    });//state error

    const [categoryById, setCategoryById] = useState({
        categoryId: -1,
        categoryName: ""
    })// state getbyId

    const [showUpdateModal, setShowUpdateModal] = useState(false);// state bat/tat modal update
    const [confirmModal, setComfirmModal] = useState(false);//state bat/tat modal comfirm
    const search = (data) => {
        return data.filter(row => convert_vi_to_en(row.categoryName.toLowerCase()).indexOf(convert_vi_to_en(intiText.toLowerCase())) > -1
            || row.categoryId.toString().includes(intiText));
    }

    const colunmns = [
        {
            name: "Category id",
            selector: row => row.categoryId,
            sortable: true, // nha ten cot thi sort
            center: true
        },
        {
            name: "Category name",
            selector: row => row.categoryName,
            center: true
        }
        , {
            name: "Action",
            cell: (row) => {
                return <>
                    <div style={{ margin: "auto" }}>
                        <Button variant="outline-dark" onClick={() => {setErrorResponse({ categoryName: "" });handleGetById(row.categoryId)}}>SỬA</Button>
                        <button style={{ marginLeft: "5px" }} className="btn btn-primary" onClick={() => comfirmDeleteCategory(row.categoryId)}>XÓA</button>
                    </div>

                </>
            },
            // style: {
            //     backgroundColor: 'rgba(187, 204, 221, 1)',
            // },
            center: true
        }
    ]

    useEffect(() => {
        categoryService.getAllCategoryService().then((response) => {
            setCategoryList(response.data)
        }).catch(error => alert("Lỗi " + error + ". Bạn hãy quay lại sau."));
    }, [intiText, load])

    // them category
    const handleAddCategory = () => {
        let dataRequest = {
            categoryName: ""
        }
        dataRequest.categoryName = value;// hoac dataRequest['...']=...
        categoryService.createCategoryService(dataRequest).then((dataResponse) => {
            let dataShow = dataResponse.data;
            setValue("")
            alert(dataShow["message"]);
            setLoadTable(!load);
            setShowAddModal(false);
        }).catch((err) => {
            let errorShow = err.response.data;
            setErrorResponse(errorShow);
        })
    }
    //get category theo id
    const handleGetById = (id) => {
        categoryService.getCategoryById(id).then((dataResponse) => {
            let cateogryDetail = dataResponse.data;
            setCategoryById({
                categoryId: id,
                categoryName: cateogryDetail.categoryName
            });
            setShowUpdateModal(true);
        }).catch((e) => alert(e.response.data))
    }
    // update category
    const handleUpdateCategory = (category) =>{
        categoryService.updateCategoryService(category).then((dataResponse) => {
            let dataShow = dataResponse.data;
            alert(dataShow["message"]);
            setLoadTable(!load);
            setShowUpdateModal(false);
        }).catch((err) => {
            let errorShow = err.response.data;
            setErrorResponse(errorShow);
        })
    }
    // delete category
    const comfirmDeleteCategory = (id) =>{
        setCategoryById({...categoryById, categoryId:id});
        console.log(categoryById.categoryId)
        console.log(categoryById.categoryName)
        setComfirmModal(true);
    }
    const handleDeleteCategory = () =>{
        categoryService.deleteCategoryService(categoryById.categoryId).then((dataResponse) => {
            let dataShow = dataResponse.data;
            alert(dataShow["message"]);
            setLoadTable(!load);
            setComfirmModal(!confirmModal);
        }).catch((err) => {
            let errorShow = err.response.data;
            setErrorResponse(errorShow);
        })
    }



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
                title="Admin category list"
                columns={colunmns}
                data={search(categoryList)}
                pagination
                fixedHeader // thanh keo cua bang
                fixedHeaderScrollHeight="400px" // cho cai thanh keo 400px va sat thanh keo cua page luon
                highlightOnHover // dua chuot vo dong doi mau
                paginationIconFirstPage
                // actions={
                //     <button className="btn btn-sm">Export</button>
                // }
                responsive
                paginationPerPage={5}
                paginationRowsPerPageOptions={[5, 15, 23, 50]}
                subHeader
                subHeaderComponent={
                    <>
                        <input type="text" placeholder="search here" className="w-25 form-control"
                            value={intiText} onChange={(e) => setText(e.target.value)} />
                        <Button style={{ marginLeft: "10px" }} variant="outline-dark"
                            onClick={() => { setErrorResponse({ categoryName: "" }); setShowAddModal(true) }}>THÊM</Button>
                    </>
                }
                subHeaderAlign="right"
                customStyles={customStyles}
            />



            {/* Modal them category*/}

            <Modal show={showAddModal} onHide={() => setShowAddModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Thêm category</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Label>Tên danh mục</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Nhập tên danh mục"
                        onChange={(e) => {
                            (setValue(e.target.value));
                            setErrorResponse({
                                categoryName: ""
                            })

                        }}
                    />
                    <ValidationMessage
                        errorResponse={errorResponse}
                        field="categoryName" />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowAddModal(false)}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => handleAddCategory()}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal>

            {/* Modal sửa category*/}
            <Modal show={showUpdateModal} onHide={() => setShowUpdateModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Sửa category </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3">
                        <Form.Label>Mã category</Form.Label>
                        <Form.Control
                            type="number"
                            value={categoryById.categoryId}
                            readOnly
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Tên danh mục</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Nhập tên danh mục"
                            onChange={(e) => {
                                (setCategoryById({
                                    ...categoryById, 
                                    categoryName: e.target.value
                                }));
                                setErrorResponse({
                                    categoryName: ""
                                });
                            }}
                            defaultValue={categoryById.categoryName}
                        />
                        <ValidationMessage
                            errorResponse={errorResponse}
                            field="categoryName" />
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowUpdateModal(!showUpdateModal)}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => handleUpdateCategory(categoryById)}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal>
            {/* Modal Xác nhận category*/}
            <Modal show={confirmModal} onHide={() => setComfirmModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Xác nhận</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p> {`Bạn có muốn xóa sản phẩm "${categoryById.categoryId}" không?`}</p>              
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setComfirmModal(!confirmModal)}>
                        Không
                    </Button>
                    <Button variant="primary" onClick={() => handleDeleteCategory()}>
                        Có
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

export default AdminCategory;
