package com.tiscon11.domain;

/**
 * 配偶者有無種別。
 *
 * @author TIS Taro
 *
 * @param marriedType 配偶者有無種別
 * @param marriedName 配偶者有無名
 */
public record MarriedType(
        Integer marriedType, // 配偶者有無種別
        String marriedName // 配偶者有無名
) {

}
