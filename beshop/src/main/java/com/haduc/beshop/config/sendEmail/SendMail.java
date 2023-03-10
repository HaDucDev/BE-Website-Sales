package com.haduc.beshop.config.sendEmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendMail {


    public String sendMailWithText(String sub, String content, String to) {
        try {
            MimeMessage message = getJavaMailSender().createMimeMessage();
            boolean multipart = true;
            MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSubject(sub);
            helper.setTo(to);
            getJavaMailSender().send(message);
        }catch (Exception e) {
            return "Send failed";
        }
        return "Send successfully";
    }



    @Value("${sping.email}")
    private String email;

    @Value("${spring.pass}")
    private String password;

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "false");
        return mailSender;
    }


}
