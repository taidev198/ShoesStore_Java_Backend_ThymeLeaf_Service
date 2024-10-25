package com.taidev198.model.Enum;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    SUCCESSFUL(
        "00",
        "Giao dịch thành công"),
    SUSPECTED_FRAUD(
        "07",
        "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)."),
    INTERNET_BANKING_NOT_REGISTERED(
            "09",
            "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng."),
    AUTHENTICATION_FAILED_3_TIMES(
            "10", "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"),
    PAYMENT_TIMEOUT(
            "11",
            "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch."),
    ACCOUNT_LOCKED(
        "12",
        "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa."),
    WRONG_OTP(
            "13",
            "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch."),
    TRANSACTION_CANCELLED(
        "24",
        "Giao dịch không thành công do: Khách hàng hủy giao dịch"),
    INSUFFICIENT_FUNDS(
            "51", "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch."),
    DAILY_LIMIT_EXCEEDED(
            "65", "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày."),
    BANK_MAINTENANCE(
        "75",
        "Ngân hàng thanh toán đang bảo trì."),
    WRONG_PASSWORD_TOO_MANY_TIMES(
            "79",
            "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch"),
    OTHER_ERRORS(
        "99",
        "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)");

    private final String code;
    private final String message;

    TransactionStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TransactionStatus fromCode(String code) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null; // or throw an exception if invalid code
    }
}
