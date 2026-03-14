package org.ping_me.service;

import org.ping_me.dto.request.SendOtpRequest;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 15/01/2026, Thursday
 **/
public interface MailSenderService {
    void sendOtp(SendOtpRequest request);
}
