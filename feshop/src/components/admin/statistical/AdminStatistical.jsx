import Chart from "react-google-charts";
import React, { useEffect, useState } from "react";
import statisticalService from "../../../services/admin/admin.statistical.service";

const AdminStatistical = () => {


    const generateColor = () => {//lay mau ngau nhien
        let r = parseInt(Math.random() * 255);
        let g = parseInt(Math.random() * 255);
        let b = parseInt(Math.random() * 255);
        return `rgb(${r}, ${g}, ${b})`;
    };

    // const data = [
    //     ["name", "money", { role: "style" }],
    //     ["John", 1000, generateColor()],
    //     ["Mary", 2000, generateColor()],
    //     ["Tom", 1500, generateColor()],
    //     ["Lucy", 3000, generateColor()],
    //     ["Bob", 1200, generateColor()],
    // ];

    const [dataChart, setDataChart] = useState([]);


    useEffect(() => {
        statisticalService.getRevenueStatisticsService()
          .then((res) => {
            setDataChart(res.data);
          })
          .catch((err) => {
            console.log(err.response.data);
          });
      }, []);

    let showChartData = [["Sản phẩm", "Tổng tiền", { role: "style" }]];
    dataChart.forEach((item) => {
        showChartData.push([   item["key"], item["value"]  , generateColor()     ]);
      });

    return (
        <>Trang Thong Ke
            <Chart
                width={800}
                height={500}
                chartType="ColumnChart"
                loader={<div>Loading Chart</div>}
                data={showChartData}
                options={{
                    title: "Doanh thu theo loại sản phẩm",
                    // Just add this option
                    is3D: true,
                    //colors: colors,
                }}
                rootProps={{ "data-testid": "2" }}
            />
        </>
    )

}

export default AdminStatistical;