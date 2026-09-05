package com.spendsense.aiservice.service.impl;

import com.spendsense.aiservice.dto.TransactionExtractionRequest;
import com.spendsense.aiservice.dto.TransactionExtractionResponse;
import com.spendsense.aiservice.service.TransactionExtractionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class TransactionExtractionServiceImpl
        implements TransactionExtractionService {

    private final RestClient geminiClient;
    private final RestClient grokClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${grok.api.key}")
    private String grokApiKey;

    @Value("${grok.model}")
    private String grokModel;

    public TransactionExtractionServiceImpl(
            @Value("${gemini.api.url}") String geminiApiUrl,
            @Value("${grok.api.url}") String grokApiUrl,
            ObjectMapper objectMapper) {

        this.geminiClient = RestClient.builder()
                .baseUrl(geminiApiUrl)
                .build();

        this.grokClient = RestClient.builder()
                .baseUrl(grokApiUrl)
                .build();

        this.objectMapper = objectMapper;
    }

    private String buildPrompt(String message) {
        return """
                You are a financial transaction extraction system.

                Your job is to determine whether the notification represents
                a real financial transaction and, if it does, extract the
                transaction details.

                Return ONLY valid JSON.

                Required JSON format:

                {
                  "isTransaction": true or false,
                  "amount": number or null,
                  "type": "EXPENSE" or "INCOME" or null,
                  "merchant": string or null,
                  "category": "BILLS" or "EDUCATION" or "ENTERTAINMENT"
                              or "FOOD" or "GROCERIES" or "HEALTH"
                              or "OTHER" or "RENT" or "SHOPPING"
                              or "TRAVEL" or null,
                  "paymentMethod": "BANK_TRANSFER" or "CASH"
                                  or "CREDIT_CARD" or "DEBIT_CARD"
                                  or "OTHER" or "UPI" or null,
                  "transactionDate": "yyyy-MM-ddTHH:mm:ss" or null
                }

                Rules:

                1. Set isTransaction=true only when the message represents
                   an actual completed financial transaction.

                2. Set isTransaction=false for OTPs, advertisements,
                   promotional messages, delivery updates, login alerts,
                   security alerts, welcome messages, and other messages
                   that do not represent a financial transaction.

                3. Debited, paid, spent, purchased or withdrawn means
                   EXPENSE.

                4. Credited, received or deposited means INCOME.

                5. Identify the merchant whenever possible.

                6. Identify the most appropriate category.

                7. Identify the payment method when available.

                8. If payment method is not mentioned, use OTHER.

                9. If merchant cannot be identified, use null.

                10. If category cannot be confidently identified,
                    use OTHER.

                11. Convert the transaction date and time to ISO format.

                12. For non-transaction messages, return:
                    isTransaction=false
                    amount=null
                    type=null
                    merchant=null
                    category=null
                    paymentMethod=null
                    transactionDate=null

                13. Never treat an OTP, account number, reference number,
                    balance, phone number or other random number as
                    a transaction amount.

                14. Do not include explanations.

                15. Return JSON only.

                Notification/SMS:

                """ + message;
    }

    @Override
    public TransactionExtractionResponse extractTransaction(
            TransactionExtractionRequest request) {

        String prompt = buildPrompt(request.getMessage());

        String extractedJson;

        try {
            extractedJson = callGemini(prompt);
        } catch (RestClientException geminiEx) {
            // Gemini failed (quota, downtime, etc.) — fall back to Groq
            extractedJson = callGrok(prompt);
        }

        return parseResponse(extractedJson);
    }

    private String callGemini(String prompt) {

        Map<String, Object> requestBody = Map.of(
                "model", "gemini-3.6-flash",
                "input", prompt
        );

        String geminiResponse = geminiClient.post()
                .header("x-goog-api-key", geminiApiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);

        JsonNode root = objectMapper.readTree(geminiResponse);
        JsonNode steps = root.path("steps");

        for (JsonNode step : steps) {
            if ("model_output".equals(step.path("type").asText())) {
                for (JsonNode contentItem : step.path("content")) {
                    if ("text".equals(contentItem.path("type").asText())) {
                        return contentItem.path("text").asText();
                    }
                }
            }
        }

        throw new RuntimeException("Gemini did not return transaction JSON");
    }

    private String callGrok(String prompt) {
        System.out.println("GROQ MODEL: " + grokModel);

        Map<String, Object> requestBody = Map.of(
                "model", grokModel,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        String grokResponse = grokClient.post()
                .header("Authorization", "Bearer " + grokApiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);

        JsonNode root = objectMapper.readTree(grokResponse);

        JsonNode contentNode = root
                .path("choices")
                .get(0)
                .path("message")
                .path("content");

        if (contentNode.isMissingNode() || contentNode.asText().isBlank()) {
            throw new RuntimeException(
                    "Groq did not return transaction JSON. Raw response: "
                            + grokResponse
            );
        }

        return contentNode.asText();
    }

    private TransactionExtractionResponse parseResponse(String extractedJson) {

        try {

            if (extractedJson == null || extractedJson.isBlank()) {
                throw new RuntimeException(
                        "No transaction JSON returned by either provider"
                );
            }

            extractedJson = extractedJson
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            return objectMapper.readValue(
                    extractedJson,
                    TransactionExtractionResponse.class
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse AI response: " + extractedJson, e
            );
        }
    }
}