import React, { useEffect, useState } from "react";
import { Button, Form, Modal, Spinner } from "react-bootstrap";
import DataTable from "react-data-table-component";
import 'bootstrap/dist/css/bootstrap.css';
import convert_vi_to_en from "../../../utils/utils";
import productService from "../../../services/admin/admin.product.service";
import categoryService from "../../../services/admin/admin.category.service";
import supplierService from "../../../services/admin/admin.supplier.service";
import { CKEditor } from 'ckeditor4-react';
import ValidationMessage from "../../acommon-component/ValidationMessage";
const AdminProduct = () => {

    const [productList, setProductList] = useState([]);// state datatable
    const [intiText, setText] = useState("");// state search

    const [showAddModal, setShowAddModal] = useState(false);// state bat/tat modal create
    const [load, setLoadTable] = useState(false);// state load khi them thanh cong

    const [value, setValue] = useState({
        productName: "",
        quantity: -1,
        discount: -1,
        unitPrice: -1,
        descriptionProduct: "",
        categoryId: 0,
        supplierId: 0

    });// state supplier ban dau khi modal add

    const [descriptionProductOk, setDescriptionProduct] = useState("");

    const [isSubmitting, setIsSubmitting] = useState(false);// state nut chi nhan dc mot lan
    const [isLoading, setIsLoading] = useState(false);// tao spiner quay de biet data dang gui, cham 1 ti
    const [selectedFile, setSelectedFile] = useState(null);


    const [selectCategoryList, setSelectCategoryList] = useState([]);// luu de map du lieu
    const [selectSupplierList, setSelectSupplierList] = useState([]);// luu de map du lieu

    const [errorResponse, setErrorResponse] = useState({
        productName: "",
        quantity: "",
        discount: "",
        unitPrice: "",
        descriptionProduct: "",
        categoryId: "",
        supplierId: ""
    });//state error


    const [productById, setProductById] = useState({
        productId: 0,
        productName: "",
        quantity: "",
        discount: "",
        unitPrice: "",
        descriptionProduct: "",
        productImage: "",
        category: "",
        supplier: "",
        categoryId: 0,
        supplierId: 0
    })// state getbyId

    const [showUpdateModal, setShowUpdateModal] = useState(false);// state bat/tat modal xem/sua

    const [openInputUpdate, setopenInputUpdate] = useState(true);// do thuoc tinh readOnly true ms khoa input


    const [confirmModal, setComfirmModal] = useState(false);//state bat/tat modal comfirm

    const search = (data) => {
        return data.filter(row => convert_vi_to_en(row.productName.toLowerCase()).indexOf(convert_vi_to_en(intiText.toLowerCase())) > -1
            || row.productId.toString().includes(intiText)
            || convert_vi_to_en(row.category.categoryName.toLowerCase()).indexOf(convert_vi_to_en(intiText.toLowerCase())) > -1
            || convert_vi_to_en(row.supplier.supplierName.toLowerCase()).indexOf(convert_vi_to_en(intiText.toLowerCase())) > -1
            || row.unitPrice.toString().includes(intiText));
    }

    useEffect(() => {
        productService.getAllProductService().then((response) => {
            setProductList(response.data)
        }).catch(error => alert("Lỗi " + error + ". Bạn hãy quay lại sau."));
    }, [intiText, load])

    const getAllCategoryAndSupplier = () => {
        categoryService
            .getAllCategoryService()
            .then(dataResponse => {
                setSelectCategoryList(dataResponse.data);
            })
            .catch(error => alert("Lỗi " + error + ". Bạn hãy quay lại sau."));
        supplierService
            .getAllSupplierService()
            .then(dataResponse => {
                setSelectSupplierList(dataResponse.data);
            })
            .catch(error => alert("Lỗi " + error + ". Bạn hãy quay lại sau."));
    };

    //them san pham
    const handleAddProduct = () => {
        setIsSubmitting(true);// khoa nut
        setIsLoading(true);// mo quay tron
        let dataRequest = value;
        dataRequest.descriptionProduct = descriptionProductOk;
        let fileRequest = selectedFile;
        productService.createCProductService(dataRequest, fileRequest).then((dataResponse) => {
            let dataShow = dataResponse.data;
            setValue({
                productName: "",
                quantity: -1,
                discount: -1,
                unitPrice: -1,
                descriptionProduct: "",
                categoryId: 0,
                supplierId: 0

            })
            setSelectedFile(null)
            alert(dataShow["message"]);
            setDescriptionProduct("");
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

    //xem, sua san pham
    const handleGetById = (id) => {
        setopenInputUpdate(true);
        productService.getProductById(id).then((dataResponse) => {
            let productDetail = dataResponse.data;
            setProductById({
                productId: id,
                productName: productDetail.productName,
                quantity: productDetail.quantity,
                discount: productDetail.discount,
                unitPrice: productDetail.unitPrice,
                descriptionProduct: productDetail.descriptionProduct,
                productImage: productDetail.productImage,
                category: productDetail.category,
                supplier: productDetail.supplier,
                categoryId: productDetail.category.categoryId,
                supplierId:productDetail.supplier.supplierId
            });
            setDescriptionProduct(productDetail.descriptionProduct)
            setShowUpdateModal(true);
        }).catch((e) => alert(e.response.data))
    }

    //update san pham
    const handleUpdateProduct =() =>{
        setIsSubmitting(true);// khoa nut
        setIsLoading(true);// mo quay tron
        let dataRequest = {
            productId: productById.productId,
            productName: productById.productName,
            quantity: productById.quantity,
            discount: productById.discount,
            unitPrice: productById.unitPrice,
            descriptionProduct: descriptionProductOk,
            categoryId: productById.categoryId,
            supplierId: productById.supplierId
        };
        let fileRequest = selectedFile;
        productService.updateProductService(dataRequest, fileRequest).then((dataResponse) => {
            let dataShow = dataResponse.data;
            //ko can dat lai gia tri producById do khi nhat get thi no cx load lai thoi
            setSelectedFile(null)
            alert(dataShow["message"]);
            setDescriptionProduct("");
            setLoadTable(!load);
            setIsLoading(false);//tat spiner
            setIsSubmitting(false);// mo nut
            setShowUpdateModal(false);
        })
            .catch((err) => {
                console.log(err)
                let errorShow = err.response.data;
                console.log(errorShow);
                setIsLoading(false);// tat spinner
                setErrorResponse(errorShow);
            })
    }

    // xoa san pham
    const comfirmDeleteProduct = (id) =>{
        setProductById({...productById, productId: id})
        setComfirmModal(true);
    }

    const handleDeleteProduct = () =>{
        productService.deleteProductService(productById.productId).then((dataResponse) => {
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
            name: "Product id",
            selector: row => row.productId,
            sortable: true, // nha ten cot thi sort
            center: true
        },
        {
            name: "Product name",
            selector: (row) => (<div title={row.productName}>{row.productName}</div>),
            center: true,
        }
        ,
        {
            name: "Số lượng",
            selector: row => row.quantity,
            center: true
        }
        ,
        {
            name: "produc Image",
            selector: (row) => <img width={50} height={50} src={row.productImage} alt="lỗi ảnh" />,
            center: true
        }
        ,
        {
            name: "Loại hàng",
            selector: row => row.category.categoryName,
            center: true
        }
        , {
            name: "Hãng",
            selector: row => row.supplier.supplierName,
            center: true
        },
        {
            name: "Action",
            cell: (row) => {
                return <>
                    <div style={{ margin: "auto" }}>
                        <Button variant="outline-dark" onClick={
                            () => {
                                setErrorResponse({
                                    productName: "",
                                    quantity: "",
                                    discount: "",
                                    unitPrice: "",
                                    descriptionProduct: "",
                                    productImage: "",
                                    categoryId: "",
                                    supplierId: ""
                                });
                                getAllCategoryAndSupplier();//goi de them vo modal them
                                handleGetById(row.productId)
                            }
                        }>Xem</Button>
                        <button style={{ marginLeft: "5px" }} className="btn btn-primary" onClick={() => comfirmDeleteProduct(row.productId)}>XÓA</button> 
                    </div>

                </>
            },
            center: true
        }
    ];

    const customStyles = {// css datatable
        headCells: {
            style: {
                backgroundColor: "#C0D5FF"
            },
        },
        headRow: {
            style: {

            },
        },

    };

    return (
        <>
            <DataTable
                title="Admin product list"
                columns={colunmns}
                data={search(productList)}
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
                                setErrorResponse({
                                    productName: "",
                                    quantity: "",
                                    discount: "",
                                    unitPrice: "",
                                    descriptionProduct: "",
                                    categoryId: "",
                                    supplierId: ""
                                });
                                getAllCategoryAndSupplier();//goi de them vo modal them
                                setShowAddModal(true)
                            }}>THÊM</Button>
                    </>
                }
                subHeaderAlign="right"
                customStyles={customStyles}
            />

            {/* Modal them product*/}
            <Modal show={showAddModal} onHide={() => setShowAddModal(false)} size="lg">
                <Modal.Header closeButton style={{ width: "100%" }}>
                    <Modal.Title>Thêm product</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                        <Form.Label>Tên sản phẩm</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Nhập tên sản phẩm"
                            onChange={(e) => {
                                setValue({ ...value, productName: e.target.value });
                                setErrorResponse({
                                    ...errorResponse,
                                    productName: ""
                                })
                                setIsSubmitting(false);// mo nut
                            }}
                        />
                        <ValidationMessage errorResponse={errorResponse} field="productName" />
                    </Form.Group>
                    <div style={{ display: "flex" }}>

                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput2">
                            <Form.Label>Số lượng</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Nhập số lượng"
                                onChange={(e) => {
                                    setValue({ ...value, quantity: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        quantity: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}
                            />
                            <ValidationMessage errorResponse={errorResponse} field="quantity" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput3" style={{ marginLeft: "10px", }}>
                            <Form.Label>Giảm giá</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Giảm giá"
                                onChange={(e) => {
                                    setValue({ ...value, discount: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        discount: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}
                            />
                            <ValidationMessage errorResponse={errorResponse} field="discount" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput4" style={{ marginLeft: "10px", }} >
                            <Form.Label>Giá gốc</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập tên hãng"
                                onChange={(e) => {
                                    setValue({ ...value, unitPrice: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        unitPrice: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}
                            />
                            <ValidationMessage errorResponse={errorResponse} field="unitPrice" />
                        </Form.Group>
                    </div>

                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput5">
                        <Form.Label>Đặc tả</Form.Label>
                        <CKEditor
                            onChange={(e) => {
                                setErrorResponse({
                                    ...errorResponse,
                                    descriptionProduct: ""
                                })
                                setDescriptionProduct(e.editor.getData());
                            }}
                        />
                        <ValidationMessage errorResponse={errorResponse} field="descriptionProduct" />
                    </Form.Group>
                    <div style={{ display: "flex" }}>

                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput6">
                            <Form.Label>Chọn loại hàng</Form.Label>
                            <Form.Control
                                as="select"
                                name="categoryId"
                                value={value.categoryId}
                                onChange={(e) => {
                                    setValue({ ...value, categoryId: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        categoryId: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}>
                                <option value={0}>Chọn category...</option>
                                {
                                    selectCategoryList.map((category) => (
                                        <option key={category.categoryId} value={category.categoryId}>{category.categoryName}</option>
                                    ))
                                }
                            </Form.Control>
                            <ValidationMessage errorResponse={errorResponse} field="categoryId" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput7" style={{ marginLeft: "10px" }}>
                            <Form.Label>Tên hãng</Form.Label>
                            <Form.Control
                                as="select"
                                name="categoryId"
                                value={value.supplierId}
                                onChange={(e) => {
                                    setValue({ ...value, supplierId: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        supplierId: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}>
                                <option value={0}>Chọn supplier...</option>
                                {
                                    selectSupplierList.map((supplier) => (
                                        <option key={supplier.supplierId} value={supplier.supplierId}>{supplier.supplierName}</option>
                                    ))
                                }
                            </Form.Control>
                            <ValidationMessage errorResponse={errorResponse} field="supplierId" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput8" style={{ marginLeft: "10px" }}>
                            <Form.Label>Chọn ảnh cho sản phẩm</Form.Label>
                            <Form.Control
                                type="file"
                                onChange={(e) => {
                                    (setSelectedFile(e.target.files[0]));
                                }}
                            />
                        </Form.Group>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowAddModal(false)}>
                        Đóng
                    </Button>

                    <Button variant="outline-primary" disabled={isSubmitting}
                        style={{ zIndex: "1" }}
                        onClick={() => handleAddProduct()}>
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

            {/* Modal sửa product==================================================================================================*/}
            <Modal show={showUpdateModal} onHide={() => setShowUpdateModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title> {openInputUpdate ? "Chi tiết sản phẩm" : "Sửa Product"}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                        <Form.Label>Mã sản phẩm</Form.Label>
                        <Form.Control
                            type="text"
                            defaultValue={productById.productId}
                            readOnly
                        />                   
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                        <Form.Label>Tên sản phẩm</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Nhập tên sản phẩm"
                            onChange={(e) => {
                                setProductById({ ...productById, productName: e.target.value });
                                setErrorResponse({
                                    ...errorResponse,
                                    productName: ""
                                })
                                setIsSubmitting(false);// mo nut
                            }}
                            defaultValue={productById.productName}
                            readOnly={openInputUpdate}
                        />
                        <ValidationMessage errorResponse={errorResponse} field="productName" />
                    </Form.Group>
                    <div style={{ display: "flex" }}>

                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput2">
                            <Form.Label>Số lượng</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Nhập số lượng"
                                onChange={(e) => {
                                    setProductById({ ...productById, quantity: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        quantity: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}
                                defaultValue={productById.quantity}
                                readOnly={openInputUpdate}
                            />
                            <ValidationMessage errorResponse={errorResponse} field="quantity" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput3" style={{ marginLeft: "10px", }}>
                            <Form.Label>Giảm giá</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Giảm giá"
                                onChange={(e) => {
                                    setProductById({ ...productById, discount: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        discount: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}
                                defaultValue={productById.discount}
                                readOnly={openInputUpdate}
                            />
                            <ValidationMessage errorResponse={errorResponse} field="discount" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput4" style={{ marginLeft: "10px", }} >
                            <Form.Label>Giá gốc</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Nhập giá gốc"
                                onChange={(e) => {
                                    setProductById({ ...productById, unitPrice: e.target.value });
                                    setErrorResponse({
                                        ...errorResponse,
                                        unitPrice: ""
                                    })
                                    setIsSubmitting(false);// mo nut
                                }}
                                defaultValue={productById.unitPrice}
                                readOnly={openInputUpdate}
                            />
                            <ValidationMessage errorResponse={errorResponse} field="unitPrice" />
                        </Form.Group>
                    </div>

                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput5">
                        <Form.Label>Đặc tả</Form.Label>
                        {
                            openInputUpdate ? (<div style={{ border: "1px solid blue", padding: "10px" }}
                                dangerouslySetInnerHTML={{
                                    __html: productById.descriptionProduct,
                                }}
                            />) : (<>
                                <CKEditor
                                    onChange={(e) => {
                                        setErrorResponse({
                                            ...errorResponse,
                                            descriptionProduct: ""
                                        })
                                        setDescriptionProduct(e.editor.getData());
                                    }}
                                    initData={productById.descriptionProduct}
                                />
                                <ValidationMessage errorResponse={errorResponse} field="descriptionProduct" />
                            </>)
                        }
                    </Form.Group>
                    <div style={{ display: "flex" }}>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput6">
                            <Form.Label>Chọn loại hàng</Form.Label>
                            {
                                openInputUpdate ? (
                                    <Form.Control
                                        type="input"
                                        placeholder="loại hàng"
                                        defaultValue={productById.category.categoryName}
                                        readOnly={openInputUpdate}
                                    />
                                ) : (<>
                                    <Form.Control
                                        as="select"
                                        name="categoryId"
                                        value={productById.categoryId}
                                        onChange={(e) => {
                                            setProductById({ ...productById, categoryId: e.target.value });
                                            setErrorResponse({
                                                ...errorResponse,
                                                categoryId: ""
                                            })
                                            setIsSubmitting(false);// mo nut
                                        }}>
                                        <option value={0}>Chọn category...</option>
                                        {
                                            selectCategoryList.map((category) => (
                                                <option key={category.categoryId} value={category.categoryId}>{category.categoryName}</option>
                                            ))
                                        }
                                    </Form.Control>
                                    <ValidationMessage errorResponse={errorResponse} field="categoryId" />
                                </>)
                            }

                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput7" style={{ marginLeft: "10px" }}>
                            <Form.Label>Tên hãng</Form.Label>
                            {
                                openInputUpdate ? (<Form.Control
                                    type="input"
                                    placeholder="loại hàng"
                                    defaultValue={productById.supplier.supplierName}
                                    readOnly={openInputUpdate}
                                />) : (
                                    <>
                                        <Form.Control
                                            as="select"
                                            name="categoryId"
                                            value={productById.supplierId}
                                            onChange={(e) => {
                                                setProductById({ ...productById, supplierId: e.target.value });
                                                setErrorResponse({
                                                    ...errorResponse,
                                                    supplierId: ""
                                                })
                                                setIsSubmitting(false);// mo nut
                                            }}>
                                            <option value={0}>Chọn supplier...</option>
                                            {
                                                selectSupplierList.map((supplier) => (
                                                    <option key={supplier.supplierId} value={supplier.supplierId}>{supplier.supplierName}</option>
                                                ))
                                            }
                                        </Form.Control>
                                        <ValidationMessage errorResponse={errorResponse} field="supplierId" />
                                    </>
                                )
                            }

                        </Form.Group>
                    </div>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput8" style={{ marginLeft: "10px" }}>
                        <Form.Label>Ảnh cho sản phẩm</Form.Label>
                        <div style={{ display: "flex" }}>
                            {
                                openInputUpdate ? (<div>
                                    <img style={{ border: "1px solid black" }} src={productById.productImage} width={100} height={100} alt="lỗi ảnh" />
                                </div>) : (
                                    <>
                                        <div>
                                            <img style={{ border: "1px solid black" }} src={productById.productImage} width={100} height={100} alt="lỗi ảnh" />
                                        </div>
                                        <div style={{ margin: "auto" }}>
                                            <Form.Label >Đổi ảnh</Form.Label>
                                            <Form.Control
                                                type="file"
                                                onChange={(e) => {
                                                    (setSelectedFile(e.target.files[0]));
                                                }}
                                            />
                                        </div>
                                    </>
                                )
                            }
                        </div>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowUpdateModal(false)}>
                        Đóng
                    </Button>
                    {
                        openInputUpdate ? (<Button variant="outline-primary" disabled={isSubmitting}
                            style={{ zIndex: "1" }}
                            onClick={() => setopenInputUpdate(!openInputUpdate)}>
                            Sửa
                        </Button>) : (<Button variant="outline-primary" disabled={isSubmitting}
                            style={{ zIndex: "1" }}
                            onClick={() => handleUpdateProduct()}>
                            {isLoading && (
                                <Spinner
                                    style={{ margin: "auto", zIndex: "9" }}
                                    animation="border"
                                    variant="primary"
                                />
                            )}
                            Lưu
                        </Button>)
                    }
                </Modal.Footer>
            </Modal>

            {/* Modal Xác nhận product*/}
            <Modal show={confirmModal} onHide={() => setComfirmModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Xác nhận</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p> {`Bạn có muốn xóa sản phẩm "${productById.productId}" không?`}</p>              
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setComfirmModal(!confirmModal)}>
                        Không
                    </Button>
                    <Button variant="primary" onClick={() => handleDeleteProduct()}>
                        Có
                    </Button>
                </Modal.Footer>
            </Modal>



        </>
    )
}

export default AdminProduct;