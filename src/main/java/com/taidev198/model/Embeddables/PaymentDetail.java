package com.taidev198.model.Embeddables;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaymentDetail {
    private int amount;
    private String bankCode;
}
