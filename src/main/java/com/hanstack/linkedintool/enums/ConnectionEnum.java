package com.hanstack.linkedintool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConnectionEnum {
    CONNECTION_1ST(1), CONNECTION_2ST(2), CONNECTION_3ST(3);

    private final int value;
}
