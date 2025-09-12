package com.groupplanmanagerbe.domain.mail.service;

import com.groupplanmanagerbe.global.common.enums.ApiErrorCode;
import com.groupplanmanagerbe.global.exception.custom.EmailException;
import com.groupplanmanagerbe.global.message.MessageResolver;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String username;
    private final TemplateEngine templateEngine;
    private final MessageResolver messageResolver;
    private final RedisTemplate<String, String> redisTemplate;

    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTE = 30;

    @Transactional
    public void sendCodeToEmail(String email) {
        String code = generateCode();
        createAndSendMail(email, code);
        saveInfoToRedis(email, code);
    }

    @Transactional
    public void verifyCode(String email, String code) {
        //작성예정
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int min = (int) Math.pow(10, CODE_LENGTH - 1);      // 100000
        int max = (int) Math.pow(10, CODE_LENGTH) - 1;      // 999999
        int code = random.nextInt(max - min + 1) + min;     // min ~ max 범위

        return String.valueOf(code);
    }

    private void createAndSendMail(String email, String code) {
        Context context = new Context();
        context.setVariable("verificationCode", code);
        context.setVariable("expirationMinutes", CODE_EXPIRATION_MINUTE);

        String lang = LocaleContextHolder.getLocale().getLanguage();
        String templateName = "email/verification_" + lang;

        String htmlContent = templateEngine.process(templateName, context);
        String title = messageResolver.get("email.title");

        sendEmail(email, title, htmlContent);
    }

//    private void sendEmail(String toEmail, String title, String content) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(toEmail);
//            helper.setFrom(userMail);
//            helper.setSubject(title);
//            helper.setText(content, true);
//            helper.setReplyTo(userMail);
//
//            javaMailSender.send(message);
//        } catch (MessagingException e) {
//            log.error("메일 메시지 생성 실패", e);
//            throw new EmailException(ApiErrorCode.EMAIL_CREATION_FAILED);
//        } catch (RuntimeException e) {
//            log.error("메일 전송 실패: {}", e.getMessage());
//            throw new EmailException(ApiErrorCode.EMAIL_SEND_FAILED);
//        }
//    }


    private void sendEmail(String toEmail, String title, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            log.info("유저네임: {}", username);
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(content, true);
            helper.setReplyTo(username);

            javaMailSender.send(message);
            log.info("이메일 전송 성공: {}", toEmail);

        } catch (MessagingException e) {
            log.error("메일 메시지 생성/전송 실패 - 받는사람: {}, 에러: {}", toEmail, e.getMessage(), e);
            throw new EmailException(ApiErrorCode.EMAIL_CREATION_FAILED);
        } catch (MailAuthenticationException e) {
            log.error("SMTP 인증 실패: {}", e.getMessage(), e);
            throw new EmailException(ApiErrorCode.EMAIL_SEND_FAILED);
        } catch (MailSendException e) {
            log.error("메일 전송 실패: {}", e.getMessage(), e);
            throw new EmailException(ApiErrorCode.EMAIL_SEND_FAILED);
        } catch (Exception e) {
            log.error("예상치 못한 에러: {}", e.getMessage(), e);
            throw new EmailException(ApiErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private void saveInfoToRedis(String email, String code) {
        String codeKey = "email:code:" + email;
        String resendKey = "email:resend:" + email;

        if (redisTemplate.hasKey(resendKey)) {
            throw new EmailException(ApiErrorCode.EMAIL_SEND_TOO_FREQUENT);
        }
        redisTemplate.opsForValue().set(resendKey, "LOCK", Duration.ofMinutes(1));

        redisTemplate.opsForValue().set(codeKey, code, Duration.ofMinutes(CODE_EXPIRATION_MINUTE));
    }
}
