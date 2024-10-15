package com.taidev198.controller.admin;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

import com.taidev198.annotation.PreAuthorizeAdmin;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taidev198.bean.RevenueInfo;
import com.taidev198.service.ExcelService;
import com.taidev198.service.OrdersService;
import com.taidev198.util.excel.ExportExcel;
import com.taidev198.util.util.CommonUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@PreAuthorizeAdmin
@RequestMapping("/admin/statistic")
public class StatisticController {

        private final OrdersService ordersService;
        private final ExcelService excelService;

        @GetMapping
        public String index(Model model,
                        @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
                if (yearMonth == null) {
                        yearMonth = YearMonth.now(); // Mặc định là tháng/năm hiện tại nếu không có ngày được cung cấp
                }

                RevenueInfo revenueInfo = ordersService.getMonthlyRevenue(yearMonth);
                Map<String, RevenueInfo> dailyRevenue = ordersService.getDailyRevenueDetails(yearMonth);
                Map<String, RevenueInfo> monthlyRevenue = ordersService.getMonthlyRevenueDetails(yearMonth.getYear());

                model.addAttribute("revenueInfo", revenueInfo);
                model.addAttribute("dailyRevenue", dailyRevenue);
                model.addAttribute("monthlyRevenue", monthlyRevenue);
                model.addAttribute("selectedDate", yearMonth);
                model.addAttribute("selectedYear", yearMonth.getYear());

                model.addAttribute("currentPage", "statistic-management");
                return "/screens/admin/statistic/index";
        }

        @GetMapping("/data")
        @ResponseBody
        public Map<String, Object> show(
                        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
                RevenueInfo revenueInfo = ordersService.getMonthlyRevenue(yearMonth);
                Map<String, RevenueInfo> dailyRevenue = ordersService.getDailyRevenueDetails(yearMonth);
                Map<String, RevenueInfo> monthlyRevenue = ordersService.getMonthlyRevenueDetails(yearMonth.getYear());

                Map<String, Object> response = new HashMap<>();
                response.put("revenueInfo", revenueInfo);
                response.put("dailyRevenue", dailyRevenue);
                response.put("monthlyRevenue", monthlyRevenue);

                return response;
        }

        @GetMapping("/downloadfile")
        public void downloadExcel(
                        @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
                        HttpServletResponse response) throws IOException {

                if (yearMonth == null) {
                        yearMonth = YearMonth.now(); // Mặc định là tháng/năm hiện tại nếu không có ngày được cung cấp
                }

                ExportExcel exportExcel = excelService.exportFileExcelStatistic(yearMonth);

                String fileName = "ThongKe_" + yearMonth.getMonthValue() + "_" + yearMonth.getYear() + ".xlsx";
                exportExcel.export(response, fileName);
        }
}
