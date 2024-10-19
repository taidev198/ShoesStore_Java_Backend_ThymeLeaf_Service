package com.taidev198.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.taidev198.config.VNPayConfig;
import com.taidev198.model.Embeddables.PaymentDetail;
import com.taidev198.service.VNPayService;
import com.taidev198.util.VNPayUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayServiceImpl implements VNPayService {

    @Autowired
    private final VNPayConfig vnPayConfig;

    @Override
    public String createPaymentRequest(PaymentDetail paymentDetail) {
        long amount = paymentDetail.getAmount() * 100L;
        String bankCode = paymentDetail.getBankCode();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put(
                "vnp_IpAddr",
                ((WebAuthenticationDetails) SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getDetails())
                        .getRemoteAddress());
        // build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }
}
