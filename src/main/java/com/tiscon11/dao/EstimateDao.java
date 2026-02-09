package com.tiscon11.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.tiscon11.domain.InsuranceOrder;
import com.tiscon11.domain.InsuranceType;
import com.tiscon11.domain.JobType;
import com.tiscon11.domain.MarriedType;
import com.tiscon11.domain.TreatedType;

/**
 * 保険見積もり機能においてDBとのやり取りを行うクラス。
 *
 * @author TIS Taro
 */
@Component
public class EstimateDao {

    /**
     * データベース・アクセスAPIである「JDBC」を使い、名前付きパラメータを用いてSQLを実行するクラス
     */
    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    /**
     * 保険種別テーブルに登録されているすべての保険種別を取得する。
     *
     * @return すべての保険種別
     */
    public List<InsuranceType> getAllInsurances() {
        String sql = "SELECT INSURANCE_TYPE, INSURANCE_NAME, MONTHLY_FEE FROM INSURANCE_TYPE";
        return parameterJdbcTemplate.query(sql, DataClassRowMapper.newInstance(InsuranceType.class));
    }

    /**
     * 職業種別テーブルに登録されているすべての職業種別を取得する。
     *
     * @return すべての職業種別
     */
    public List<JobType> getAlljobTypes() {
        String sql = "SELECT JOB_TYPE, JOB_NAME FROM JOB_TYPE";
        return parameterJdbcTemplate.query(sql, DataClassRowMapper.newInstance(JobType.class));
    }

    /**
     * 配偶者有無種別テーブルに登録されているすべての配偶者有無種別を取得する。
     * 「配偶者あり」を先頭にするためにMARRIED_TYPEの降順でソートする。
     *
     * @return すべての配偶者有無種別
     */
    public List<MarriedType> getAllMarriedTypes() {
        String sql = "SELECT MARRIED_TYPE, MARRIED_NAME FROM MARRIED_TYPE ORDER BY MARRIED_TYPE DESC";
        return parameterJdbcTemplate.query(sql, DataClassRowMapper.newInstance(MarriedType.class));
    }

    /**
     * 病歴有無種別テーブルに登録されているすべての病歴有無種別を取得する。
     * 「はい」を先頭にするためにTREATED_TYPEの降順でソートする。
     *
     * @return すべての病歴有無種別
     */
    public List<TreatedType> getAllTreatedTypes() {
        String sql = "SELECT TREATED_TYPE, TREATED_NAME FROM TREATED_TYPE ORDER BY TREATED_TYPE DESC";
        return parameterJdbcTemplate.query(sql, DataClassRowMapper.newInstance(TreatedType.class));
    }

    /**
     * 保険種別名を取得する。
     *
     * @param insuranceType 保険種別タイプ
     * @return 保険種別名
     */
    public String findInsuranceName(Integer insuranceType) {
        String sql = "SELECT INSURANCE_NAME FROM INSURANCE_TYPE WHERE INSURANCE_TYPE = :insuranceType";
        SqlParameterSource paramSource = new MapSqlParameterSource("insuranceType", insuranceType);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, String.class);
    }

    /**
     * 職業種別名を取得する。
     *
     * @param jobType 職業種別タイプ
     * @return 職業種別名
     */
    public String findJobName(Integer jobType) {
        String sql = "SELECT JOB_NAME FROM JOB_TYPE WHERE JOB_TYPE = :jobType";
        SqlParameterSource paramSource = new MapSqlParameterSource("jobType", jobType);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, String.class);
    }

    /**
     * 配偶者有無種別名を取得する。
     *
     * @param marriedType 配偶者有無種別タイプ
     * @return 配偶者有無種別名
     */
    public String findMarriedName(Integer marriedType) {
        String sql = "SELECT MARRIED_NAME FROM MARRIED_TYPE WHERE MARRIED_TYPE = :marriedType";
        SqlParameterSource paramSource = new MapSqlParameterSource("marriedType", marriedType);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, String.class);
    }

    /**
     * 病歴有無種別名を取得する。
     *
     * @param treatedType 病歴有無種別タイプ
     * @return 病歴有無種別名
     */
    public String findTreatedName(Integer treatedType) {
        String sql = "SELECT TREATED_NAME FROM TREATED_TYPE WHERE TREATED_TYPE = :treatedType";
        SqlParameterSource paramSource = new MapSqlParameterSource("treatedType", treatedType);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, String.class);
    }

    /**
     * ユーザーが選択した保険種別の保険料を取得する。
     *
     * @param insuranceType 保険種別タイプ
     * @return 保険料
     */
    public int findMonthlyFee(Integer insuranceType) {
        String sql = "SELECT MONTHLY_FEE FROM INSURANCE_TYPE WHERE INSURANCE_TYPE = :insuranceType";
        SqlParameterSource paramSource = new MapSqlParameterSource("insuranceType", insuranceType);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }

    /**
     * ユーザーの年齢に合致する調整率を取得する。
     *
     * @param age 年齢
     * @return 保険料年齢別調整率
     */
    public double findAdjustmentRateByAge(int age) {
        String sql = "SELECT ADJUSTMENT_RATE FROM AGE_ADJUSTMENT_RATE WHERE AGE = :age";
        SqlParameterSource paramSource = new MapSqlParameterSource("age", age);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Double.class);
    }

    /**
     * データベースに見積もり依頼を登録する。
     *
     * @param insuranceOrder 見積もり依頼情報
     */
    public void insertInsuranceOrder(InsuranceOrder insuranceOrder) {
        String sql = """
                INSERT INTO INSURANCE_ORDER(
                    INSURANCE_TYPE, KANJI_NAME, KANA_NAME, DATE_OF_BIRTH, ADDRESS, TEL, EMAIL_ADDRESS, MARRIED, JOB, INCOME, TREATED, MEDICAL_HISTORY
                )
                VALUES(
                    :insuranceType, :kanjiName, :kanaName, :dateOfBirth, :address, :tel, :email, :marriedType, :jobType, :income, :treatedType, :medicalHistory
                )
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        parameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(insuranceOrder), keyHolder);
    }
}
