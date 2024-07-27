package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceIMPL implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException("Error sending mail", e);
        }
    }

    @Override
    public void forgetPasswordOtpMail(UserEntity user) {
        String subject = "CeylonTrail - Password Reset OTP";
        String body = String.format("""
                        Dear %s,

                        We received a request to reset your password.
                        Your OTP for resetting the password is: %s

                        This OTP is valid for a limited time only. Please use it within 10 minutes.

                        If you did not request this, please ignore this email.

                        Best regards,
                        CeylonTrail Team
                        """,
                user.getFirstname(), user.getForgetPasswordOtp());

        this.sendMail(user.getEmail(), subject, body);
    }

    @Override
    public void passwordResetSuccessMail(UserEntity user) {
        String subject = "CeylonTrail - Password Reset Success";
        String body = String.format("""
                        Dear %s,

                        Your password has been successfully reset. If you did not initiate this change, please contact support immediately.

                        Best regards,
                        CeylonTrail Team
                        """,
                user.getFirstname());

        this.sendMail(user.getEmail(), subject, body);
    }
}
