package com.tiscon11.domain;

/**
 * 病歴有無種別。
 *
 * @author TIS Taro
 *
 * @param treatedType 病歴有無種別
 * @param treatedName 病歴有無名
 */
public record TreatedType(
        Integer treatedType, // 病歴有無種別
        String treatedName // 病歴有無名
) {

}
