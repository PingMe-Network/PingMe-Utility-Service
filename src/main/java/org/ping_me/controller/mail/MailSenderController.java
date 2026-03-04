package org.ping_me.controller.mail;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ping_me.dto.request.SendOtpRequest;
import org.ping_me.dto.response.ApiResponse;
import org.ping_me.service.mail.MailSenderService;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 15/01/2026, Thursday
 **/
@RestController
@Slf4j
@RequestMapping("mail-management/api/v1/mails")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailSenderController {
    MailSenderService mailSenderService;

    @PostMapping("/admin-otp-verification")
    ResponseEntity<ApiResponse<Boolean>> sendOtpToAdmin(@RequestBody SendOtpRequest request) {
        if (request == null || request.getToMail() == null || request.getOtp() == null || request.getOtpType() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.<Boolean>builder()
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .errorMessage("Missing required mail payload")
                    .data(false)
                    .build());
        }

        try {
            mailSenderService.sendOtp(request);
            return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                    .errorCode(HttpStatus.OK.value())
                    .errorMessage(HttpStatus.OK.name())
                    .data(true)
                    .build());
        } catch (IllegalArgumentException e) {
            log.warn("Rejected OTP mail request for {}: {}", request.getToMail(), e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.<Boolean>builder()
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .errorMessage(e.getMessage())
                    .data(false)
                    .build());
        } catch (Exception e) {
            log.error("Failed to send OTP mail to {}", request.getToMail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<Boolean>builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorMessage("Failed to send OTP mail")
                    .data(false)
                    .build());
        }
    }
}
