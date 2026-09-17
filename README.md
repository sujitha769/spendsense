# SpendSense 💰

SpendSense is an AI-powered expense tracking and spending analysis platform built using a microservices architecture. The application allows users to submit transaction messages in natural language, automatically extracts transaction information using AI, stores the transaction securely, and provides spending analytics and behavioral insights.

## 🚀 Features

- User registration and login
- JWT-based authentication
- Secure transaction management
- AI-powered transaction extraction
- Automatic transaction classification
- Natural language transaction processing
- Service-to-service communication using OpenFeign
- Service discovery using Eureka
- Centralized request routing using API Gateway
- Spending analytics
- Spending behavior analysis
- Email notification support
- MySQL database integration
- Separate databases for different services

## 🏗️ System Architecture

```text
                         Client / Postman
                               |
                               v
                       API Gateway :8080
                               |
                               v
                       Eureka Server :8761
                               |
          +--------------------+----------------------+
          |          |         |          |           |
          v          v         v          v           v
       User       Transaction  AI      Analytics    Behavior
      :8083         :8081     :8082      :8084       :8085
                     |                      |
                     |                      v
                     |                  Behavior
                     |                    :8085
                     |
                     v
                   MySQL

                         Notification Service
                                :8086
```

## 🧩 Microservices

| Service | Port | Responsibility |
|---|---:|---|
| Eureka Server | 8761 | Service discovery |
| API Gateway | 8080 | Centralized API routing |
| User Service | 8083 | User registration, login and JWT authentication |
| Transaction Service | 8081 | Transaction creation and management |
| AI Service | 8082 | AI-based transaction extraction |
| Analytics Service | 8084 | Spending analytics |
| Behavior Service | 8085 | Spending behavior analysis |
| Notification Service | 8086 | Email notifications |

## 🛠️ Technology Stack

### Programming Languages
- Java
- JavaScript

### Backend
- Spring Boot
- Spring Data JPA
- Spring Security
- REST APIs
- Microservices

### Spring Cloud
- Spring Cloud Gateway
- Eureka Server
- OpenFeign
- Spring Cloud LoadBalancer

### Database
- MySQL

### Authentication
- JWT
- Password Encoding

### AI Integration
- Gemini API
- Groq API

### Email
- Spring Mail

### Development Tools
- Maven
- Docker
- Postman
- GitHub
- IntelliJ IDEA
- VS Code

## 🔄 Main Transaction Flow

A user can send a natural language message instead of manually entering every transaction field.

Example:

> Paid 500 to Swiggy using UPI

The request follows this flow:

```text
Client
  |
  v
API Gateway
  |
  v
Transaction Service
  |
  | OpenFeign
  v
AI Service
  |
  | Gemini / Groq
  v
Transaction Extraction
  |
  v
Transaction Service
  |
  v
MySQL
```

The AI service extracts information from the transaction message.

Example response:

```json
{
  "amount": 500,
  "type": "EXPENSE",
  "merchant": "Swiggy",
  "category": "FOOD",
  "paymentMethod": "UPI",
  "transactionDate": null,
  "isTransaction": true
}
```

The Transaction Service then converts the extracted information into a transaction record and stores it in MySQL.

## 👤 User Service

The User Service is responsible for:

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

## 💳 Transaction Service

The Transaction Service manages user transactions.

Main responsibilities:

- Create transactions
- Retrieve transactions
- Retrieve user-specific transactions
- Update transactions
- Delete transactions
- Communicate with AI Service
- Store transaction information

Example transaction data:

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

## 🤖 AI Service

The AI Service processes natural language transaction messages.

It uses:

- Gemini API
- Groq API

The service determines whether the provided message represents a transaction and extracts important transaction attributes.

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
Amount       → 500
Type         → EXPENSE
Merchant     → Swiggy
Category     → FOOD
Payment      → UPI
```

## 📊 Analytics Service

The Analytics Service retrieves the authenticated user's transactions and calculates spending-related metrics.

It provides information such as:

- Total expenses
- Total transactions
- Average expense
- Category-wise expenses
- Top spending category
- Amount spent in the top category

Example:

```json
{
  "totalExpense": 4500,
  "totalTransactions": 8,
  "averageExpense": 562.50,
  "topCategory": "FOOD"
}
```

## 🧠 Behavior Service

The Behavior Service uses analytics information to identify spending patterns.

It can determine:

- Dominant spending category
- Percentage of spending in a category
- Spending frequency
- Spending concentration
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

## 📧 Notification Service

The Notification Service is responsible for sending email notifications based on spending behavior.

It communicates with:

```text
Notification Service
       |
       +----> Behavior Service
       |
       +----> User Service
       |
       v
   Spring Mail
       |
       v
     Email
```

Email credentials are configured using environment variables rather than storing them directly in source code.

## 🔗 Microservice Communication

SpendSense uses OpenFeign for communication between microservices.

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

### Eureka Service Discovery

Eureka acts as the service registry.

Instead of manually storing the location of every service, services register themselves with Eureka.

```text
                 Eureka Server
                    :8761
                       |
        +--------------+--------------+
        |              |              |
        v              v              v
 Transaction       AI Service     User Service
   Service
```

This allows services to discover each other dynamically.

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

The Gateway forwards the request to the Transaction Service.

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

This keeps service-specific data separated and follows the microservices principle of independent data ownership.

## 📁 Project Structure

```text
SpendSense/
│
├── eureka-server/
│   └── src/
│
├── api-gateway/
│   └── src/
│
├── user-service/
│   └── src/
│
├── transaction-service/
│   └── src/
│
├── ai-service/
│   └── src/
│
├── analytics-service/
│   └── src/
│
├── behavior-service/
│   └── src/
│
├── notification-service/
│   └── src/
│
├── .gitignore
└── README.md
```

Each service is maintained as an independent Spring Boot application.

## 🧪 API Testing

Postman is used to test the backend APIs.

Example:

```http
POST http://localhost:8080/api/transactions
```

Request body:

```json
{
  "message": "Paid 500 to Swiggy using UPI"
}
```

The request is processed by the Transaction Service, which communicates with the AI Service to extract the transaction information.

## ▶️ Running the Project

### Start Eureka Server

```bash
cd eureka-server
.\mvnw.cmd spring-boot:run
```

Eureka Dashboard:

```text
http://localhost:8761
```

### Start the Services

Start the following Spring Boot applications:

```text
Eureka Server         → 8761
API Gateway           → 8080
Transaction Service   → 8081
AI Service            → 8082
User Service          → 8083
Analytics Service     → 8084
Behavior Service      → 8085
Notification Service  → 8086
```

After starting the services, they register with Eureka.

## 🔐 Environment Variables

Sensitive credentials should be provided through environment variables.

Example:

```text
GEMINI_API_KEY
GROQ_API_KEY
MAIL_USERNAME
MAIL_PASSWORD
```

These values should not be committed to GitHub.

## 🔮 Future Improvements

Planned improvements include:

- Apache Kafka for event-driven communication
- Asynchronous transaction processing
- Automatic transaction detection from financial notifications
- Advanced spending insights
- Improved notification intelligence
- Docker Compose deployment
- Production-ready configuration
- Enhanced exception handling
- More advanced analytics and reports

## 📌 Project Goal

The main goal of SpendSense is to reduce the manual effort involved in tracking expenses.

Instead of manually entering:

```text
Amount
Merchant
Category
Payment Method
Transaction Type
Date
```

the user can provide a simple natural-language message:

```text
Paid 500 to Swiggy using UPI
```

The AI service extracts the required information and the backend stores it as a structured transaction.

## 👩‍💻 Project Information

**Project Name:** SpendSense

**Description:** AI-Powered Expense Tracking & Spending Analysis

**Architecture:** Microservices

**Backend:** Java + Spring Boot

**Database:** MySQL

**Authentication:** JWT

**Service Communication:** OpenFeign

**Service Discovery:** Eureka

**Gateway:** Spring Cloud Gateway

**AI:** Gemini API + Groq API

**Email:** Spring Mail

**Testing:** Postman

**Version Control:** GitHub
