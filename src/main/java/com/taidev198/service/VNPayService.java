package com.taidev198.service;

import com.taidev198.model.Embeddables.PaymentDetail;

public interface VNPayService {

    public String createPaymentRequest(PaymentDetail paymentDetail);
}
