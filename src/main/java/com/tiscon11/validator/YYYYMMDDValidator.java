package com.tiscon11.validator;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 検証処理が実装された内部クラス。
 *
 * @author TIS Taro
 */
public class YYYYMMDDValidator implements ConstraintValidator<YYYYMMDD, String> {

    /** 許容するフォーマット */
    private String allowFormat;

    /**
     * 検証処理を初期化する。
     *
     * @param constraintAnnotation 対象プロパティに付与されたアノテーション
     */
    @Override
    public void initialize(YYYYMMDD constraintAnnotation) {
        allowFormat = constraintAnnotation.allowFormat();
    }

    /**
     * 対象の値が {@code allowFormat} で指定するフォーマットに適合しているか検証する。
     *
     * @param value   対象の値
     * @param context バリデーションコンテキスト
     * @return フォーマットに適合している場合 {@code true}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNullOrEmpty(value)) {
            return true;
        }
        try {
            return getParsedDate(value, allowFormat, Locale.getDefault()) != null;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    private static Date getParsedDate(String date, String format, Locale locale) {
        if (isNullOrEmpty(format)) {
            throw new IllegalArgumentException("format mustn't be null or empty. format=" + format);
        } else if (date == null) {
            throw new IllegalArgumentException("date mustn't be null.");
        } else {
            SimpleDateFormat df = new SimpleDateFormat(format, locale);
            df.setLenient(false);
            ParsePosition pos = new ParsePosition(0);
            Date resultDate = df.parse(date, pos);
            if (resultDate == null) {
                return null;
            } else {
                return df.format(resultDate).equals(date) ? resultDate : null;
            }
        }
    }
}
