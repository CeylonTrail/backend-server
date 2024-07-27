package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.entity.UserEntity;

public interface MailService {
    void sendMail(String to, String subject, String body);
    void travellerActivationMail(UserEntity user);
    void serviceProviderActivationMail(UserEntity user, String serviceName);
    void forgetPasswordOtpMail(UserEntity user);
    void passwordResetSuccessMail(UserEntity user);
}
