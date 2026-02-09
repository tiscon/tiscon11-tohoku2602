package com.tiscon11.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tiscon11.domain.InsuranceOrder;
import com.tiscon11.form.UserOrderForm;
import com.tiscon11.service.EstimateResult;
import com.tiscon11.service.EstimateService;

/**
 * 保険見積もりのコントローラークラス。
 *
 * @author TIS Taro
 */

@Controller
public class EstimateController {

    /** 見積もりサービス（料金計算等に使用） */
    @Autowired
    private EstimateService estimateService;

    /**
     * トップ画面を表示する。
     *
     * @return 遷移先画面ファイル名（トップ画面）
     */
    @GetMapping("")
    String top() {
        return "top"; // トップ画面表示を指示
    }

    /**
     * "/start"にGETリクエストが送信されたときのエンドポイント。
     * 画面表示に必要な情報を用意し、入力画面に遷移する。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先画面ファイル名（入力画面）
     */
    @GetMapping("start")
    String start(Model model) {

        // 初期表示用に、空の入力フォームを用意
        model.addAttribute("userOrderForm", emptyForm());

        // プルダウンとラジオボタンの選択肢に表示するラベルを設定
        setOptionLabels(model);

        return "input"; // 入力画面表示を指示
    }

    /**
     * "/confirm"にPOSTリクエストが送信されたときのエンドポイント。
     * 画面表示に必要な情報を用意し、確認画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先画面ファイル名（確認画面）
     */
    @PostMapping("confirm")
    String confirm(@ModelAttribute UserOrderForm userOrderForm, Model model) {

        // 入力画面のプルダウンとラジオボタンで選択された項目についてラベルを設定
        setSelectedOptionLabels(model, userOrderForm);

        return "confirm"; // 確認画面表示を指示
    }

    /**
     * "/estimate"にPOSTリクエストが送信されたときのエンドポイント。
     * 概算見積もり画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param result        精査結果
     * @param model         遷移先に連携するデータ
     * @return 遷移先画面ファイル名（概算見積もり結果画面、入力エラー時は確認画面）
     */
    @PostMapping(value = "estimate", params = "proceed")
    String estimate(@Validated UserOrderForm userOrderForm, BindingResult result, Model model) {

        // 入力画面のプルダウンとラジオボタンで選択された項目についてラベルを設定
        setSelectedOptionLabels(model, userOrderForm);

        if (result.hasErrors()) {
            // 入力エラーがある場合は、確認画面に遷移する。
            model.addAttribute("errors", result.getAllErrors());
            return "confirm"; // 確認画面表示を指示
        }

        // 誕生日
        LocalDate dateOfBirth = LocalDate.parse(userOrderForm.dateOfBirth(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 年齢が範囲内であるか確認する
        if (!estimateService.isAgeValid(dateOfBirth)) {
            // エラーの場合、Formの生年月日の項目にFieldErrorを追加
            result.addError(new FieldError("userOrderForm", "dateOfBirth",
                    "年齢は20歳以上100歳以下である必要があります"));
            model.addAttribute("errors", result.getAllErrors());
            return "confirm"; // 確認画面表示を指示
        }

        // 保険種別
        int insuranceType = Integer.parseInt(userOrderForm.insuranceType());

        // 誕生日と保険種別をもとに、保険料（年額）を算出する
        EstimateResult estimateResult = estimateService.calculateInsuranceFee(insuranceType, dateOfBirth);
        model.addAttribute("estimateResult", estimateResult);

        return "result"; // 概算見積もり結果画面表示を指示
    }

    /**
     * 入力画面に戻る。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先画面ファイル名（入力画面）
     */
    @PostMapping(value = "estimate", params = "backToInput")
    String backToInput(UserOrderForm userOrderForm, Model model) {

        // プルダウンとラジオボタンの選択肢に表示するラベルを設定
        setOptionLabels(model);

        return "input"; // 入力画面表示を指示
    }

    /**
     * "/order"にPOSTリクエストが送信されたときのエンドポイント。
     * 見積もり依頼をデータベースに登録し、申し込み完了画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param result        精査結果
     * @param model         遷移先に連携するデータ
     * @return 遷移先画面ファイル名（申し込み完了画面、入力エラー時は確認画面）
     */
    @PostMapping(value = "order", params = "proceed")
    String order(@Validated UserOrderForm userOrderForm, BindingResult result, Model model) {

        if (result.hasErrors()) {
            // 入力エラーがある場合は、確認画面に遷移する。
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("insuranceName", fetchInsuranceName(userOrderForm.insuranceType()));
            return "confirm"; // 確認画面表示を指示
        }

        // 誕生日
        LocalDate dateOfBirth = LocalDate.parse(userOrderForm.dateOfBirth(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 年齢が範囲内であるか確認する
        if (!estimateService.isAgeValid(dateOfBirth)) {
            // エラーの場合、Formの生年月日の項目にFieldErrorを追加
            result.addError(new FieldError("userOrderForm", "dateOfBirth",
                    "年齢は20歳以上100歳以下である必要があります"));
            model.addAttribute("errors", result.getAllErrors());
            return "confirm"; // 確認画面表示を指示
        }

        // データベースに見積もり依頼を登録する。
        InsuranceOrder insuranceOrder = new InsuranceOrder(
                null, // 受付番号はデータベース登録時に自動採番されるためnullを設定
                Integer.parseInt(userOrderForm.insuranceType()),
                userOrderForm.kanjiName(),
                userOrderForm.kanaName(),
                userOrderForm.dateOfBirth(),
                userOrderForm.address(),
                userOrderForm.tel(),
                userOrderForm.email(),
                Integer.parseInt(userOrderForm.marriedType()),
                Integer.parseInt(userOrderForm.jobType()),
                Integer.parseInt(userOrderForm.income()),
                Integer.parseInt(userOrderForm.treatedType()),
                userOrderForm.medicalHistory());
        estimateService.registerOrder(insuranceOrder);

        return "complete"; // 申し込み完了画面表示を指示
    }

    /**
     * 確認画面に戻る。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先画面ファイル名（確認画面）
     */
    @PostMapping(value = "order", params = "backToConfirm")
    String backToConfirm(UserOrderForm userOrderForm, Model model) {

        // 入力画面のプルダウンとラジオボタンで選択された項目についてラベルを設定
        setSelectedOptionLabels(model, userOrderForm);

        return "confirm"; // 確認画面表示を指示
    }

    /**
     * 保険種別に対応する保険名を取得する。
     *
     * @param insuranceType ユーザーが指定した保険種別
     * @return 保険名
     */
    private String fetchInsuranceName(String insuranceType) {
        return estimateService.findInsuranceName(Integer.parseInt(insuranceType));
    }

    /**
     * 配偶者有無種別に対応する配偶者有無名を取得する。
     *
     * @param marriedType ユーザーが指定した配偶者有無種別
     * @return 配偶者有無名
     */
    private String fetchMarriedName(String marriedType) {
        return estimateService.findMarriedName(Integer.parseInt(marriedType));
    }

    /**
     * 職業種別に対応する職業名を取得する。
     *
     * @param jobType ユーザーが指定した職業種別
     * @return 職業名
     */
    private String fetchJobName(String jobType) {
        return estimateService.findJobName(Integer.parseInt(jobType));
    }

    /**
     * 病歴有無種別に対応する病歴有無名を取得する。
     *
     * @param treatedType ユーザーが指定した病歴有無種別
     * @return 病歴有無名
     */
    private String fetchTreatedName(String treatedType) {
        return estimateService.findTreatedName(Integer.parseInt(treatedType));
    }

    /**
     * 初期表示用（ユーザーがまだ入力していない状態）にフォームを生成する。
     *
     * @return 空のフォーム
     */
    private UserOrderForm emptyForm() {
        return new UserOrderForm(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                String.valueOf(estimateService.getMarriedTypes().get(0).marriedType()),
                String.valueOf(estimateService.getJobTypes().get(0).jobType()),
                "",
                String.valueOf(estimateService.getTreatedTypes().get(0).treatedType()),
                "");
    }

    /**
     * 選択肢に表示するラベルを設定する。
     *
     * @param model 遷移先に連携するデータ
     */
    private void setOptionLabels(Model model) {
        // 全ての保険種別をプルダウン表示用に用意
        model.addAttribute("insurances", estimateService.getInsurances());
        // 全ての配偶者有無をラジオボタン表示用に用意
        model.addAttribute("marriedTypes", estimateService.getMarriedTypes());
        // 全ての職業をラジオボタン表示用に用意
        model.addAttribute("jobTypes", estimateService.getJobTypes());
        // 全ての病歴有無をラジオボタン表示用に用意
        model.addAttribute("treatedTypes", estimateService.getTreatedTypes());
    }

    /**
     * 選択された項目のラベルを取得し設定する。
     *
     * @param model 遷移先に連携するデータ
     */
    private void setSelectedOptionLabels(Model model, UserOrderForm userOrderForm) {
        // 選択された保険種別に対応する保険名を取得
        String insuranceName = fetchInsuranceName(userOrderForm.insuranceType());
        model.addAttribute("insuranceName", insuranceName);

        // 選択された配偶者有無種別に対応する配偶者有無名を取得
        String marriedName = fetchMarriedName(userOrderForm.marriedType());
        model.addAttribute("marriedName", marriedName);

        // 選択された職業種別に対応する職業名を取得
        String jobName = fetchJobName(userOrderForm.jobType());
        model.addAttribute("jobName", jobName);

        // 選択された病歴有無種別に対応する病歴有無名を取得
        String treatedName = fetchTreatedName(userOrderForm.treatedType());
        model.addAttribute("treatedName", treatedName);
    }

}
