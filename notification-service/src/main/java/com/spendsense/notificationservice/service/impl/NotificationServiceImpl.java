package com.spendsense.notificationservice.service.impl;
import com.spendsense.notificationservice.config.JwtService;
import com.spendsense.notificationservice.client.BehaviorServiceClient;
import com.spendsense.notificationservice.client.UserServiceClient;
import com.spendsense.notificationservice.dto.BehaviorResponse;
import com.spendsense.notificationservice.dto.NotificationResponse;
import com.spendsense.notificationservice.dto.UserResponse;
import com.spendsense.notificationservice.service.EmailService;
import com.spendsense.notificationservice.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl
        implements NotificationService {

    private final BehaviorServiceClient behaviorServiceClient;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;
    private final JwtService jwtService;

    public NotificationServiceImpl(
            BehaviorServiceClient behaviorServiceClient,
            UserServiceClient userServiceClient,
            EmailService emailService,
            JwtService jwtService) {

        this.behaviorServiceClient =
                behaviorServiceClient;

        this.userServiceClient =
                userServiceClient;

        this.emailService =
                emailService;

        this.jwtService =
                jwtService;
    }

    @Override
    public NotificationResponse generateNotification(
            String authorizationHeader) {

        // 1. Get user's behavior
        BehaviorResponse behavior =
                behaviorServiceClient.getBehavior(
                        authorizationHeader
                );

        double percentage =
                behavior.getDominantCategoryPercentage();

        String category =
                behavior.getDominantCategory();

        // 2. Check alert rule
        if (percentage >= 70
                && category != null) {

            // 3. Generate crazy message
            String message =
                    generateCategoryRoast(
                            category,
                            percentage
                    );

            // 4. Get user ID from JWT
            String token =
                    authorizationHeader.substring(7);

            Long userId =
                    jwtService.extractUserId(token);

            // 5. Get user's email from User Service
            UserResponse user =
                    userServiceClient.getUser(
                            userId,
                            authorizationHeader
                    );

            // 6. Send email
            emailService.sendEmail(
                    user.getEmail(),
                    "🔥 SpendSense Alert",
                    message
            );

            // 7. Return response
            return new NotificationResponse(
                    true,
                    "CATEGORY_CONCENTRATION",
                    message
            );
        }

        return new NotificationResponse(
                false,
                "NONE",
                "No alert for now. Your spending looks reasonably distributed."
        );
    }

    private String generateCategoryRoast(
            String category,
            double percentage) {

        if (percentage >= 90) {

            return String.format(
                    "🔥 %.0f%% of your spending is %s. "
                            + "Your wallet has officially become "
                            + "a %s employee. 😂",
                    percentage,
                    category,
                    category
            );
        }

        if (percentage >= 80) {

            return String.format(
                    "🔥 %.0f%% of your spending is %s. "
                            + "At this point, %s isn't a category — "
                            + "it's a lifestyle. 😂",
                    percentage,
                    category,
                    category
            );
        }

        return String.format(
                "⚠️ %.0f%% of your spending is %s. "
                        + "Your wallet might want you to explore "
                        + "some other categories. 😅",
                percentage,
                category
        );
    }


}