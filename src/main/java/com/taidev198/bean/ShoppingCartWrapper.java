package com.taidev198.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartWrapper {
    private List<ShoppingCartInfo> shoppingCartInfos;
}
