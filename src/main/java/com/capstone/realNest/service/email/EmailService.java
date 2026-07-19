package com.capstone.realNest.service.email;

import com.capstone.realNest.entity.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendPasswordResetEmail(
            User user,
            String resetLink
    ) {

        try {
            Context context = new Context();

            context.setVariable(
                    "name",
                    user.getName()
            );

            context.setVariable(
                    "resetLink",
                    resetLink
            );

            String htmlContent =
                    templateEngine.process(
                            "emails/reset-password",
                            context
                    );

            MimeMessage message =
                    javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setTo(user.getEmail());

            helper.setSubject(
                    "Reset Your RealNest Password"
            );

            helper.setText(
                    htmlContent,
                    true
            );

            javaMailSender.send(message);

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "Failed to send password reset email.",
                    exception
            );
        }
    }
}