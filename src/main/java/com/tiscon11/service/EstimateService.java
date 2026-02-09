package com.tiscon11.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiscon11.dao.EstimateDao;
import com.tiscon11.domain.InsuranceOrder;
import com.tiscon11.domain.InsuranceType;
import com.tiscon11.domain.JobType;
import com.tiscon11.domain.MarriedType;
import com.tiscon11.domain.TreatedType;

/**
 * 保険見積もり機能において業務処理を担当するクラス。
 *
 * @author TIS Taro
 */
@Service
public class EstimateService {

    /**
     * 見積もりDAO
     */
    @Autowired
    private EstimateDao estimateDAO;

    /**
     * 保険種別テーブルに登録されているすべての保険種別を取得する。
     *
     * @return すべての保険種別
     */
    public List<InsuranceType> getInsurances() {
        return estimateDAO.getAllInsurances();
    }

    /**
     * 職業種別テーブルに登録されているすべての職業種別を取得する。
     *
     * @return すべての職業種別
     */
    public List<JobType> getJobTypes() {
        return estimateDAO.getAlljobTypes();
    }

    /**
     * 配偶者有無種別テーブルに登録されているすべての配偶者有無種別を取得する。
     *
     * @return すべての配偶者有無種別
     */
    public List<MarriedType> getMarriedTypes() {
        return estimateDAO.getAllMarriedTypes();
    }

    /**
     * 病歴有無種別テーブルに登録されているすべての病歴有無種別を取得する。
     *
     * @return すべての病歴有無種別
     */
    public List<TreatedType> getTreatedTypes() {
        return estimateDAO.getAllTreatedTypes();
    }

    /**
     * 保険種別名を取得する。
     *
     * @param insuranceType 保険種別タイプ
     * @return 保険種別名
     */
    public String findInsuranceName(Integer insuranceType) {
        return estimateDAO.findInsuranceName(insuranceType);
    }

    /**
     * 職業種別名を取得する。
     *
     * @param jobType 職業種別タイプ
     * @return 職業種別名
     */
    public String findJobName(Integer jobType) {
        return estimateDAO.findJobName(jobType);
    }

    /**
     * 配偶者有無種別名を取得する。
     *
     * @param marriedType 配偶者有無種別タイプ
     * @return 配偶者有無種別名
     */
    public String findMarriedName(Integer marriedType) {
        return estimateDAO.findMarriedName(marriedType);
    }

    /**
     * 病歴有無種別名を取得する。
     *
     * @param treatedType 病歴有無種別タイプ
     * @return 病歴有無種別名
     */
    public String findTreatedName(Integer treatedType) {
        return estimateDAO.findTreatedName(treatedType);
    }

    /**
     * 生年月日と保険種別から保険料（年額）の見積もりを算出する。
     *
     * @param insuranceType 保険種別タイプ
     * @param dateOfBirth   生年月日
     * @return 見積もり結果
     */
    public EstimateResult calculateInsuranceFee(Integer insuranceType, LocalDate dateOfBirth) {
        // ユーザーが選択した保険種別の月額保険料を取得する。
        int monthlyFee = estimateDAO.findMonthlyFee(insuranceType);

        // ユーザーが選択した生年月日と現在日付から年齢を取得する。
        int age = calculateAge(dateOfBirth);

        // 年齢による調整率を取得する。
        double adjustmentRateByAge = estimateDAO.findAdjustmentRateByAge(age);

        // 保険料（年額）を計算する。
        int annualFee = (int) (monthlyFee * 12 * adjustmentRateByAge);

        // 見積もり結果を返す。
        EstimateResult estimateResult = new EstimateResult(annualFee, adjustmentRateByAge, age);
        return estimateResult;

    }

    /**
     * 生年月日と現在日付から年齢を計算し、年齢が20歳以上100歳以下であるかを判定する。
     *
     * @param dateOfBirth 生年月日
     * @return 年齢が20歳以上100歳以下である場合、真
     */
    public boolean isAgeValid(LocalDate dateOfBirth) {
        int age = calculateAge(dateOfBirth);
        return age >= 20 && age <= 100;
    }

    /**
     * 生年月日と現在日付から年齢を計算する。
     *
     * @param dateOfBirth 生年月日
     * @return 現在の年齢
     */
    private int calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        return period.getYears();
    }

    /**
     * データベースに見積もり依頼を登録する。
     *
     * @param insuranceOrder 見積もり依頼情報
     */
    @Transactional
    public void registerOrder(InsuranceOrder insuranceOrder) {
        estimateDAO.insertInsuranceOrder(insuranceOrder);
    }
}
