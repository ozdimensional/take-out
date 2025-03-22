package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;


public interface ReportService {

    /*
     * 获取营业额统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate startDate, LocalDate endDate);

    /*
     * 获取用户统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    UserReportVO getUserStatistics(LocalDate startDate, LocalDate endDate);

    /*
     * 获取订单统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /*
     * 获取销售排行榜数据
     * @param begin 开始日期
     * @param end 结束日期
    */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /*
     * 导出营业额统计数据
     * @param response 响应对象
     */
    void exportBusinessReport(HttpServletResponse response);
}
