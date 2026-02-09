package com.tiscon11.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailSenderServiceTest {

    @Autowired
    private MailSenderService mailSenderService;

    /**
     * メール送信のテスト。
     * メールサーバを起動した状態でテストすること。
     * 送信結果の確認は手動で行うこと。
     */
    @Test
    public void testSendMail() {

        String mailAddress = "test@example.com";
        String title = "メール送信のテストです";
        String content = """
                メール送信のテストです。

                本文です。

                お知らせです。
                本日は晴天なり。
                """;

        mailSenderService.sendMail(mailAddress, title, content);

    }
}
