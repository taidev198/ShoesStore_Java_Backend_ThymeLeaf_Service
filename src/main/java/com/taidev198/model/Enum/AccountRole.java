package com.taidev198.model.Enum;

public enum AccountRole {
    ADMIN,
    CUSTOMER,
    SELLER,
    VIEWER;

    public int getIndex() {
        return switch (this) {
            case ADMIN -> 0;
            case CUSTOMER -> 1;
            case SELLER -> 2;
            case VIEWER -> 3;
        };
    }
}
