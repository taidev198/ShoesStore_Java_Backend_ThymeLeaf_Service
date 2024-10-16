package com.taidev198.controller.customer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.taidev198.annotation.CurrentAccount;
import com.taidev198.annotation.PreAuthorizeCustomer;
import com.taidev198.bean.OrderInfo;
import com.taidev198.bean.ToastMessage;
import com.taidev198.model.Account;
import com.taidev198.model.Enum.OrderStatus;
import com.taidev198.service.OrdersService;
import com.taidev198.util.constant.CommonConstant;
import com.taidev198.util.util.CommonUtils;
import com.taidev198.util.util.PaginationUtil;
import com.taidev198.util.util.WebUtils;

import lombok.RequiredArgsConstructor;

@Controller("customer-orders-controller")
@RequestMapping("/customer/orders")
@RequiredArgsConstructor
@PreAuthorizeCustomer
public class OrdersController {
    private final OrdersService ordersService;

    @GetMapping
    public String index(
            @RequestParam(value = "status", required = false) String status,
            Model model,
            @CurrentAccount Account account,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        if (CommonUtils.isEmptyOrNullString(status)) return "redirect:/customer/orders?status=all";
        var statusEnum = CommonUtils.isNotEmptyOrNullString(status)
                ? Arrays.stream(OrderStatus.values())
                        .filter(e -> e.name().equalsIgnoreCase(status))
                        .findFirst()
                        .orElse(null)
                : null;
        var models = getOrders(account.getId(), statusEnum, page, 20);
        model.addAllAttributes(models);

        return "screens/customer/orders/index";
    }

    @PatchMapping("/{id}")
    public String changeStatus(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, ?> body,
            @CurrentAccount Account account,
            Model model) {
        try {
            if (!body.containsKey("status")) throw new Exception("Trạng thái đơn hàng không hợp lệ");

            ordersService.changeOrderStatus(account, id, body.get("status").toString());

            // Set success toast message
            model.addAttribute("toastMessages", new ToastMessage("success", "Cập nhật trạng thái đơn hàng thành công"));
        } catch (Exception e) {
            model.addAttribute("toastMessages", new ToastMessage("error", e.getMessage()));
        }

        // Get status, page and set to model
        var currentUrl = WebUtils.Sessions.getAttribute(CommonConstant.CURRENT_GET_URL, String.class);
        if (currentUrl == null || !currentUrl.contains("/customer/orders"))
            currentUrl = "/customer/orders?status=all&page=1";
        var params = CommonUtils.extractQueryParamsFromURL(currentUrl, "status", "page");
        var models = getOrders(
                account.getId(),
                Arrays.stream(OrderStatus.values())
                        .filter(e -> e.name().equalsIgnoreCase(params.get(0)))
                        .findFirst()
                        .orElse(null),
                Integer.parseInt(params.get(1)),
                20);
        model.addAllAttributes(models);
        return "screens/customer/orders/index";
    }

    private Map<String, ?> getOrders(Integer accountId, OrderStatus status, int page, int pageSize) {
        // create page request
        var pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("createdAt")));
        var orderInfoPage = ordersService.getOrders(accountId, status, pageRequest);

        // create pagination helper
        List<OrderInfo> orderInfos = orderInfoPage.getContent();
        PaginationUtil paginationHelper = new PaginationUtil(
                (int) orderInfoPage.getTotalElements(),
                pageSize,
                page,
                5,
                UriComponentsBuilder.fromUriString("/customer/orders")
                        .queryParam("status", status != null ? status.name().toLowerCase() : "all")
                        .build()
                        .encode()
                        .toUriString());

        return Map.of(
                "paginationHelper", paginationHelper,
                "page", page,
                "orderInfos", orderInfos);
    }
}
