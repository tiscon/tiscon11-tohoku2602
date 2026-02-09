package com.tiscon11.service;

/**
 * 見積もり結果を表すクラス。
 *
 * @param annualFee           保険料（年額）
 * @param adjustmentRateByAge 年齢による調整率
 * @param age                 年齢
 */
public record EstimateResult(

        int annualFee, // 保険料（年額）
        double adjustmentRateByAge, // 年齢による調整率
        int age // 年齢

) {
}
