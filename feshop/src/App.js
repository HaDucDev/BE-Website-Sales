import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import AdminCategory from './components/admin/category/AdminCategory';
import AdminProduct from './components/admin/product/AdminProduct';
import AdminSupplier from './components/admin/supplier/AdminSupplier';
import Admin from './pages/admin/Admin';
import Login from './pages/common/Login';
import Home from './pages/home/Home';
import HeaderApp from './pages/common/HeaderApp';
import { createContext } from 'react';
import { useState } from 'react';
import ProductDetail from './components/user/product-detail/ProductDetail';
//import ListProductCommon from './components/user/ListProductCommon/ListProductCommon';
import Cart from './pages/home/cart/Cart';
import ConfirmOrder from './pages/home/confirm-order/ConfirmOrder';
import HistoryOrder from './pages/home/history-order/HistoryOrder';
import AdminOrder from './components/admin/order/AdminOrder';
import Shipper from './pages/shipper/Shipper';
import AdminStatistical from './components/admin/statistical/AdminStatistical';
import ListProductCommon from './components/user/home/list-product-common/ListProductCommon';
import ListProductSearchFilter from './components/user/home/list-product-search-silter/ListProductSearchFilter';
import AdminUser from './components/admin/user/AdminUser';
import Register from './pages/common/Register';
import ChangePassword from './pages/common/ChangePassword';
import ChangeInforUser from './pages/common/ChangeInforUser';
import ForgetPassword from './pages/common/ForgetPassword';



export const LoginContext = createContext();

function App() {

  const [loadPage, setLoadPage] = useState(0);

  const state = {
    loadPage,
    setLoadPage
  }
  return (
    <>
      <LoginContext.Provider value={state}>
        <BrowserRouter>
          <HeaderApp />
          <Routes>
            {/* common */}

            <Route path="/" element={<Home />}>
              <Route path="" element={<ListProductCommon />} />
              <Route path="/product-detail/:id" element={<ProductDetail />} />
              <Route path="/search-filter" element={<ListProductSearchFilter />} />

            </Route>
            <Route path="/login" element={<Login />}></Route>
            <Route path="/register" element={<Register />}></Route>
            <Route path="/cart" element={<Cart />}></Route>
            <Route path="/confirm-order" element={<ConfirmOrder />}></Route>
            <Route path="/history-order" element={<HistoryOrder/>}></Route>
            <Route path="/change-pasword" element={<ChangePassword/>}></Route>
            <Route path="/change-infor-user" element={<ChangeInforUser/>}></Route>
            <Route path="/forget-pass" element={<ForgetPassword/>}></Route>

            {/* admin */}
            <Route path="/admin" element={<Admin />} exact>
              <Route path="user" element={<AdminUser />}></Route>
              <Route path="category" element={<AdminCategory />}></Route>
              <Route path="supplier" element={<AdminSupplier />}></Route>
              <Route path="product" element={<AdminProduct />}></Route>
              <Route path="orders" element={<AdminOrder />}></Route>
              {/* StatisticalProducQuantityMoney */}
              <Route path="statistical" element={<AdminStatistical />}></Route>
              
            </Route>

            <Route path="/shipper" element={<Shipper />}></Route>
          
          </Routes>
        </BrowserRouter>
      </LoginContext.Provider>
    </>

  );
}

export default App;
