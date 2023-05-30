package com.example.SUSTechNote.util;

import com.example.SUSTechNote.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailServiceHelper {

    /**
     * 获取配置文件中的发件人邮箱
     */
    @Value("${spring.mail.username}")
    private String mailFrom;

    private final MailService mailService;

    public EmailServiceHelper(MailService mailService) {
        this.mailService = mailService;
    }

    public boolean sendSimpleMail(String mailFromNick, String mailTo, String cc, String subject, String content) {
        String state = mailService.sendSimpleMail(mailFrom, mailFromNick, mailTo, cc, subject, content);
        return "success".equals(state);
    }
}
