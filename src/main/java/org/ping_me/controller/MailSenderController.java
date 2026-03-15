package org.ping_me.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ping_me.dto.request.SendOtpRequest;
import org.ping_me.dto.response.ApiResponse;
import org.ping_me.service.MailSenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (request == null || request.getToMail() == null || request.getOtp() == null || request.getAuthOtpType() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.<Boolean>builder()
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .errorMessage("Missing required mail payload")
                    .data(false)
                    .build());
        }

        try {
            mailSenderService.sendOtp(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.<Boolean>builder()
                    .errorCode(HttpStatus.ACCEPTED.value())
                    .errorMessage("OTP mail request accepted")
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
            log.error("Failed to queue OTP mail for {}", request.getToMail(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ApiResponse.<Boolean>builder()
                    .errorCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .errorMessage("Failed to queue OTP mail")
                    .data(false)
                    .build());
        }
    }
}
