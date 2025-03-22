package com.sky.service.impl;


import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;
    /*
     * 获取营业额统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate startDate, LocalDate endDate){
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(startDate);
        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusDays(1);
            dateList.add(startDate);
        }
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 营业额统计
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            if (turnover == null)
                turnover = 0.0;
            turnoverList.add(turnover);

        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();

    }

    /*
     * 获取用户统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    public UserReportVO getUserStatistics(LocalDate startDate, LocalDate endDate){
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(startDate);
        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusDays(1);
            dateList.add(startDate);
        }

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 新增用户统计
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("endTime", endTime);
            Integer total = userMapper.countByMap(map);
            map.put("beginTime", beginTime);
            Integer newUser = userMapper.countByMap(map);
            totalUserList.add(total);
            newUserList.add(newUser);
        }

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    public OrderReportVO getOrderStatistics(LocalDate startDate, LocalDate endDate){
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(startDate);
        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusDays(1);
            dateList.add(startDate);
        }

        List<Integer> totalOrderList = new ArrayList<>();
        List<Integer> completedOrderList = new ArrayList<>();

        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Integer Total = getOrderStatisticsCount(beginTime, endTime, null);
            Integer Completed = getOrderStatisticsCount(beginTime, endTime, Orders.COMPLETED);

            //log.info("日期："+date+" 总订单数："+Total+" 有效订单数："+Completed);
            totalOrderList.add(Total);
            completedOrderList.add(Completed);
        }

        Integer sum = totalOrderList.stream().reduce(Integer::sum).get();
        //log.info("总订单数："+sum);

        Integer completed = completedOrderList.stream().reduce(Integer::sum).get();

        //log.info("有效订单数："+completed);

        Double  orderCompletedRate = 0.0;
        if(sum!= 0){
            orderCompletedRate = completed.doubleValue()/sum.doubleValue();
        }

        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(totalOrderList, ","))
                .validOrderCountList(StringUtils.join(completedOrderList, ","))
                .totalOrderCount(sum)
                .validOrderCount(completed)
                .orderCompletionRate(orderCompletedRate)
                .build();

    }
    private Integer getOrderStatisticsCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status){
        Map map = new HashMap();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }

    /*
     * 获取销售排行榜数据
     * @param begin 开始日期
     * @param end 结束日期
     */
    public SalesTop10ReportVO  getSalesTop10(LocalDate begin, LocalDate end){
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        log.info("时间段："+beginTime+"-"+endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        log.info("销售排行榜："+names+" "+numbers);

        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();
    }
    /*
     * 导出营业额统计数据
     * @param response 响应对象
     */
    public void exportBusinessReport(HttpServletResponse response){

        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try
        {
            XSSFWorkbook excel = new XSSFWorkbook(resourceAsStream);
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            sheet1.getRow(1).getCell(1).setCellValue("时间："+startDate+"——"+endDate);

            XSSFRow row = sheet1.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());

            row = sheet1.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());


            for(int i=0;i<30;i++) {
                LocalDate date = startDate.plusDays(i);

                BusinessDataVO businessData1 = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                row = sheet1.getRow(i+7);

                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData1.getTurnover());
                row.getCell(3).setCellValue(businessData1.getValidOrderCount());
                row.getCell(4).setCellValue(businessData1.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData1.getUnitPrice());
                row.getCell(6).setCellValue(businessData1.getNewUsers());

            }

            ServletOutputStream outputStream = response.getOutputStream();

            excel.write(outputStream);

            outputStream.close();
            excel.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
