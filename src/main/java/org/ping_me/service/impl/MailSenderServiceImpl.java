package org.ping_me.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.ping_me.dto.request.SendOtpRequest;
import org.ping_me.service.MailSenderService;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 15/01/2026, Thursday
 **/
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailSenderServiceImpl implements MailSenderService {

    TemplateEngine templateEngine;
    JavaMailSender javaMailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String mailSender;

    @NonFinal
    @Value("${spring.mail.timeout}")
    String timeout;

    @Override
    @Async("mailTaskExecutor")
    public void sendOtp(SendOtpRequest request) {
        validateRequest(request);

        try {
            Context context = new Context();
            context.setVariable("otp", request.getOtp());
            context.setVariable("expiry", timeout);
            context.setVariable("recipient", request.getToMail());

            String template = resolveTemplate(request);
            String htmlContent = templateEngine.process(template, context);
            MimeMessage mime = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);
            helper.setFrom(mailSender);
            helper.setTo(request.getToMail());
            helper.setSubject("OTP Verification");
            helper.setText(htmlContent, true);
            javaMailSender.send(mime);
            log.info("OTP mail sent successfully to {}", request.getToMail());
        } catch (MessagingException e) {
            log.error("Mail send failed while preparing message for {}", request.getToMail(), e);
        } catch (RuntimeException e) {
            log.error("Mail send failed for {}", request.getToMail(), e);
        }
    }

    private void validateRequest(SendOtpRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (request.getToMail() == null || request.getToMail().isBlank()) {
            throw new IllegalArgumentException("Recipient email is required");
        }
        if (request.getOtp() == null || request.getOtp().isBlank()) {
            throw new IllegalArgumentException("OTP is required");
        }
        if (request.getOtpType() == null) {
            throw new IllegalArgumentException("OTP type is required");
        }
    }

    private String resolveTemplate(SendOtpRequest request) {
        return switch (request.getOtpType()) {
            case ADMIN_VERIFICATION -> "mail/admin-otp-verification.html";
            case USER_FORGET_PASSWORD -> "mail/forget-password-otp-verification.html";
            case ACCOUNT_ACTIVATION -> "mail/active-account-otp-verification.html";
        };
    }
}
