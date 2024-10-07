package com.taidev198.repository.base;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WhereElements {
    private String key;
    private Object value;
    private WhereClauseType type;
}
