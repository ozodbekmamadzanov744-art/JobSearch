package kg.attractor.jobsearch.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendResetPasswordEmail(String toEmail, String resetLink) throws MessagingException, UnsupportedEncodingException;
}
