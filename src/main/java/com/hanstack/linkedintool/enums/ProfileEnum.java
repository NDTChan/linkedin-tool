package com.hanstack.linkedintool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileEnum {
    PROD("prod"), DEV("dev");
    private final String name;
}
