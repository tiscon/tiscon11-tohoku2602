package com.tiscon11.domain;

/**
 * 保険種別。
 *
 * @author TIS Taro
 *
 * @param insuranceType 保険種別
 * @param insuranceName 保険名
 * @param monthlyFee    月額保険料
 */
public record InsuranceType(
        Integer insuranceType, // 保険種別
        String insuranceName, // 保険名
        Integer monthlyFee // 月額保険料
) {

}
