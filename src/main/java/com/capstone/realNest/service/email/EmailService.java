package com.capstone.realNest.service.email;

import com.capstone.realNest.entity.User;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private final Resend resend;
    private final SpringTemplateEngine templateEngine;
    private final String fromEmail;

    public EmailService(
            SpringTemplateEngine templateEngine,
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from-email}") String fromEmail
    ) {
        this.resend = new Resend(apiKey);
        this.templateEngine = templateEngine;
        this.fromEmail = fromEmail;
    }

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

            CreateEmailOptions email =
                    CreateEmailOptions.builder()
                            .from(
                                    "RealNest <"
                                            + fromEmail
                                            + ">"
                            )
                            .to(user.getEmail())
                            .subject(
                                    "Reset Your RealNest Password"
                            )
                            .html(htmlContent)
                            .build();

            resend.emails().send(email);

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "Failed to send password reset email.",
                    exception
            );
        }
    }
}
