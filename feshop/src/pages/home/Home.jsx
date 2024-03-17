import { Outlet} from "react-router";
import HomeHeader from "../../components/user/home/HomeHeader";
const Home = () => {
    return (
        <>
            <HomeHeader/>
            <Outlet/>
        </>
    );// ngoac tong
}

export default Home;