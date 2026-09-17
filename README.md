
# SpendSense 💰

SpendSense is an AI-powered expense tracking and spending analysis platform built using a microservices architecture.

The main idea of SpendSense is simple: instead of manually entering transaction details such as amount, merchant, category, payment method, and transaction type, the user can provide a natural-language message. The system uses AI to identify whether the message is related to a financial transaction and, if it is, extracts the required transaction details and stores them in the database.

The stored transaction data is then analyzed to understand spending patterns and user behavior. Based on predefined conditions, the Notification Service can send email alerts to the user.

## 🚀 Features

- User registration and login
- JWT-based authentication
- Natural-language transaction input
- AI-based transaction detection
- AI-based transaction detail extraction
- Automatic transaction categorization
- Transaction storage in MySQL
- User-specific transaction management
- Spending analytics
- Spending behavior analysis
- Condition-based email notifications
- Microservices architecture
- Service discovery using Eureka
- Service-to-service communication using OpenFeign
- Centralized API routing using Spring Cloud Gateway
- Gemini API integration
- Groq API integration
- Spring Mail integration

## 🏗️ Architecture

```text
                         Client / Postman
                                |
                                v
                       API Gateway :8080
                                |
                                v
                       Eureka Server :8761
                                |
        +-----------------------+-----------------------+
        |                       |                       |
        v                       v                       v
   User Service         Transaction Service        Other Services
      :8083                    :8081
                                  |
                                  | OpenFeign
                                  v
                              AI Service
                                :8082
                                  |
                                  v
                       Check Transaction Message
                                  |
                         +--------+--------+
                         |                 |
                        NO                YES
                         |                 |
                         v                 v
                      Ignore       Extract Details
                                           |
                                           v
                                  Transaction Database
                                           |
                                           v
                                  Analytics Service
                                       :8084
                                           |
                                           v
                                   Behavior Service
                                       :8085
                                           |
                                           v
                                  Notification Service
                                       :8086
                                           |
                                           v
                                        Email
```

## 🧩 Microservices

| Service | Port | Responsibility |
|---|---:|---|
| Eureka Server | 8761 | Service discovery |
| API Gateway | 8080 | Centralized API routing |
| Transaction Service | 8081 | Transaction processing and storage |
| AI Service | 8082 | Transaction detection and extraction |
| User Service | 8083 | User registration, login and authentication |
| Analytics Service | 8084 | Spending analysis |
| Behavior Service | 8085 | Spending behavior analysis |
| Notification Service | 8086 | Condition-based email notifications |

## 🔄 How SpendSense Works

### 1. User Sends a Message

The user provides a transaction message in natural language.

Example:

```text
Paid 500 to Swiggy using UPI
```

The message is sent to the Transaction Service.

### 2. AI Checks Whether It Is a Transaction

The Transaction Service sends the message to the AI Service.

The AI Service determines whether the message is related to a financial transaction.

Example:

```text
Input:
Paid 500 to Swiggy using UPI

Result:
Transaction related → YES
```

If the message is not related to a transaction, it is not stored.

Example:

```text
Input:
What is the weather today?

Result:
Transaction related → NO
```

### 3. AI Extracts Transaction Details

If the message is identified as a transaction, the AI Service extracts the available transaction information.

Example:

```text
Message:
Paid 500 to Swiggy using UPI

Extracted Information:

Amount         → 500
Type           → EXPENSE
Merchant       → Swiggy
Category       → FOOD
Payment Method → UPI
Transaction Date → Available when provided
```

### 4. Transaction is Stored

The extracted information is returned to the Transaction Service.

The Transaction Service stores the transaction in MySQL.

Example:

```json
{
  "amount": 500,
  "type": "EXPENSE",
  "merchant": "Swiggy",
  "category": "FOOD",
  "paymentMethod": "UPI",
  "transactionDate": "2026-09-10T21:03:02",
  "rawMessage": "Paid 500 to Swiggy using UPI"
}
```

The original message is also stored as `rawMessage`.

## 👤 User Service

The User Service manages user-related functionality.

Responsibilities:

- User registration
- User login
- Password management
- JWT generation
- Authentication
- User information

Authentication flow:

```text
User
 |
 | Login
 v
User Service
 |
 | JWT Token
 v
Client
 |
 | Authorization: Bearer <JWT>
 v
Protected APIs
```

The authenticated user's ID is used to associate transactions with the correct user.

## 💳 Transaction Service

The Transaction Service is responsible for transaction processing and storage.

Responsibilities:

- Receive transaction messages
- Communicate with AI Service
- Determine whether the message represents a transaction
- Store valid transactions
- Retrieve transactions
- Retrieve user-specific transactions
- Update transactions
- Delete transactions

Main transaction flow:

```text
Client
   |
   v
Transaction Service
   |
   | OpenFeign
   v
AI Service
   |
   | Extract transaction details
   v
Transaction Service
   |
   v
MySQL
```

## 🤖 AI Service

The AI Service is responsible for understanding natural-language messages.

It uses:

- Gemini API
- Groq API

The AI Service determines whether a message is transaction-related and extracts transaction information when applicable.

The extracted attributes include:

- Amount
- Transaction type
- Merchant
- Category
- Payment method
- Transaction date

Example:

```text
Input:
Paid 500 to Swiggy using UPI

Output:

Amount         → 500
Type           → EXPENSE
Merchant       → Swiggy
Category       → FOOD
Payment Method → UPI
```

## 📊 Analytics Service

The Analytics Service analyzes the user's stored transactions.

It calculates spending-related information such as:

- Total expenses
- Total number of transactions
- Average expense
- Category-wise expenses
- Top spending category
- Amount spent in the top category

Example:

```text
Total Expenses     → ₹4,500
Total Transactions → 8
Average Expense    → ₹562.50
Top Category       → FOOD
```

The Analytics Service retrieves transaction data from the Transaction Service using OpenFeign.

## 🧠 Behavior Service

The Behavior Service uses the analytics data to understand the user's spending behavior.

It identifies information such as:

- Dominant spending category
- Percentage of spending in the dominant category
- Spending frequency
- Spending pattern
- Spending insights

Example:

```json
{
  "dominantCategory": "SHOPPING",
  "dominantCategoryPercentage": 70.0,
  "spendingFrequency": "Low transaction frequency",
  "spendingPattern": "Highly concentrated spending"
}
```

The Behavior Service communicates with the Analytics Service using OpenFeign.

## 📧 Notification Service

The Notification Service is responsible for sending email notifications when predefined spending conditions are reached.

The flow is:

```text
Analytics Service
       |
       v
Behavior Service
       |
       v
Check Condition
       |
   +---+---+
   |       |
  NO      YES
   |       |
   v       v
 No Mail  Send Email
```

The Notification Service communicates with:

- Behavior Service
- User Service

Email delivery is implemented using Spring Mail.

## 🔗 Service-to-Service Communication

SpendSense currently uses synchronous REST-based communication between microservices through OpenFeign.

```text
Transaction Service
        |
        v
    AI Service

Analytics Service
        |
        v
 Transaction Service

Behavior Service
        |
        v
 Analytics Service

Notification Service
        |
        +----> Behavior Service
        |
        +----> User Service
```

## 🔎 Eureka Service Discovery

Eureka Server acts as the service registry.

Each microservice registers itself with Eureka so that other services can discover it.

```text
                    Eureka Server
                       :8761
                          |
        +-----------------+-----------------+
        |                 |                 |
        v                 v                 v
 Transaction          AI Service       User Service
  Service
```

This avoids manually hardcoding service locations between microservices.

## 🌐 API Gateway

The API Gateway provides a single entry point for client requests.

```text
Client
  |
  v
API Gateway :8080
  |
  +----> User Service
  |
  +----> Transaction Service
  |
  +----> AI Service
  |
  +----> Analytics Service
  |
  +----> Behavior Service
  |
  +----> Notification Service
```

Example:

```text
POST http://localhost:8080/api/transactions
```

The Gateway routes the request to the appropriate service.

## 🗄️ Database Architecture

SpendSense uses separate MySQL databases for different services.

```text
User Service
     |
     v
spendsense_user


Transaction Service
     |
     v
spendsense_transaction
```

The User Service manages user-related data, while the Transaction Service manages transaction-related data.

## 📁 Project Structure

```text
SpendSense/
│
├── eureka-server/
│
├── api-gateway/
│
├── user-service/
│
├── transaction-service/
│
├── ai-service/
│
├── analytics-service/
│
├── behavior-service/
│
├── notification-service/
│
├── .gitignore
└── README.md
```

Each service is an independent Spring Boot application.

## 🧪 API Testing

The backend APIs are currently tested using Postman.

Example transaction request:

```http
POST http://localhost:8080/api/transactions
```

Request body:

```json
{
  "message": "Paid 500 to Swiggy using UPI"
}
```

The request is processed by the Transaction Service, which communicates with the AI Service to determine whether the message represents a transaction and, if valid, extracts the transaction details.

## 🔐 Environment Variables

Sensitive credentials are provided through environment variables.

```text
GEMINI_API_KEY
GROQ_API_KEY
MAIL_USERNAME
MAIL_PASSWORD
```

Sensitive credentials should not be committed to GitHub.

## 🚧 Current Implementation

The current version of SpendSense uses synchronous API-based communication between services.

Transaction messages are currently provided manually through Postman for testing.

The current workflow is:

```text
User / Postman
      |
      v
Transaction Service
      |
      v
AI Service
      |
      v
Transaction Database
      |
      v
Analytics Service
      |
      v
Behavior Service
      |
      v
Notification Service
      |
      v
Email
```

The system currently processes transaction messages manually. Automatic reading of real financial notifications has not yet been implemented.

## 🔮 Future Enhancements

The current implementation can be extended with:

- Apache Kafka for asynchronous event-driven communication
- Automatic reading and processing of financial notifications
- Event-driven transaction processing
- Reduced dependency on synchronous service-to-service communication
- Advanced spending insights
- More intelligent notification rules
- Frontend application
- Production deployment
- Additional analytics and reporting

## 🎯 Project Objective

The objective of SpendSense is to build an intelligent expense tracking system that reduces manual transaction entry.

Instead of manually entering:

```text
Amount
Merchant
Category
Payment Method
Transaction Type
Date
```

the user can simply provide:

```text
Paid 500 to Swiggy using UPI
```

SpendSense then:

```text
Natural Language Message
          |
          v
     AI Detection
          |
          v
 Transaction Extraction
          |
          v
   Store in Database
          |
          v
 Spending Analytics
          |
          v
 Behavior Analysis
          |
          v
 Condition Evaluation
          |
          v
 Email Notification
```

The current backend implementation establishes this workflow using Java, Spring Boot, Spring Cloud, MySQL, JWT, OpenFeign, Eureka, Gemini, Groq and Spring Mail.

## 📌 Project Information

**Project Name:** SpendSense

**Description:** AI-Powered Expense Tracking & Spending Analysis

**Architecture:** Microservices

**Backend:** Java + Spring Boot

**Database:** MySQL

**Authentication:** JWT

**Service Communication:** OpenFeign

**Service Discovery:** Eureka

**API Gateway:** Spring Cloud Gateway

**AI Integration:** Gemini API + Groq API

**Email:** Spring Mail

**API Testing:** Postman

**Version Control:** GitHub
````
