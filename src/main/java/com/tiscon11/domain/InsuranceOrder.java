package com.tiscon11.domain;

/**
 * 保険申し込み
 *
 * @author TIS Taro
 *
 * @param receiptNo      受付番号
 * @param insuranceType  保険種別
 * @param kanjiName      漢字氏名
 * @param kanaName       カナ氏名
 * @param dateOfBirth    生年月日
 * @param Address        住所
 * @param tel            電話番号
 * @param email          メールアドレス
 * @param marriedType    配偶者有無
 * @param jobType        職業
 * @param income         所得金額
 * @param treatedType    病歴有無
 * @param medicalHistory 病歴
 */
public record InsuranceOrder(
        Integer receiptNo, // 受付番号
        Integer insuranceType, // 保険種別
        String kanjiName, // 漢字氏名
        String kanaName, // カナ氏名
        String dateOfBirth, // 生年月日
        String Address, // 住所
        String tel, // 電話番号
        String email, // メールアドレス
        Integer marriedType, // 配偶者有無
        Integer jobType, // 職業
        Integer income, // 所得金額
        Integer treatedType, // 病歴有無
        String medicalHistory // 病歴
) {

}
