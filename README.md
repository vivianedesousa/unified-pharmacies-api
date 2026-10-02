# Unified Pharmacies API

API developed in the context of the challenge **“Innovation for optimizing healthcare services in the SUS”**, focused on the **Brazilian Popular Pharmacy Program (Programa Farmácia Popular do Brasil)**.

## About the project

The **Unified Pharmacies API** aims to facilitate the search for medications when the prescribed product is not available at the pharmacy initially selected by the patient.

The MVP focuses on the municipality of **Serra/ES**, initially targeting **diabetes medications**.

The API allows the registration of patients, pharmacies, medications, EAN codes, prescriptions, requests, and medication availability information. When the medication is not available at the initially selected pharmacy, the pharmacist can consult other **pharmacies registered on the platform** with reported availability and select an alternative unit.

At the end of the analysis, the patient receives the request result by email and, when the medication is available, information about the medication, pharmacy, and address.

> The project is intended as a complementary solution and does not replace the official channels of the Brazilian Ministry of Health.

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Web MVC
- PostgreSQL
- Spring Validation
- Lombok
- JUnit
- Mockito
- Unit Testing
- Maven
- Mailtrap — Java SDK + Email Sandbox
- Spring Security
- JWT
- Docker
- Swagger / OpenAPI

## Tools

- Git
- GitHub
- Maven
- Docker
- Docker Compose
- pgAdmin 4
- Swagger / OpenAPI
- Mailtrap

## Architecture

The project follows a **layered architecture**, with responsibilities separated into specific packages.

```text
src/main/java/com/farmacies/unifiedpharmacies
├── auth
├── config
├── controller
├── dto
├── enums
├── exception
├── mailtrap
├── model
├── repository
├── security
└── service
|_ test

##

Package Responsibilities
Package	Responsibility
auth	Authentication and login functionality
config	Application and framework configuration
controller	REST API endpoints and HTTP request handling
dto	Data Transfer Objects used for requests and responses
enums	Application enumerations and status definitions
exception	Custom exceptions and centralized error handling
mailtrap	Integration with Mailtrap for email delivery
model	Domain entities and database models
repository	Data persistence and database access
security	JWT authentication and access control
service	Business rules and application logic
Main Features

The API provides functionalities for:

Patient registration and management
Pharmacy registration and management
Medication registration and consultation
Medication EAN code management
Medication availability management
Prescription management
Request creation and tracking
Medication availability analysis
Alternative pharmacy selection
Email notification to patients
Main Availability Flow

The main business flow works as follows:

Patient
   ↓
Request creation
   ↓
Pharmacy selection
   ↓
Prescription
   ↓
Pharmacist analysis
   ↓
Medication / EAN identification
   ↓
Availability check
   ↓
┌─────────────────────────────────┐
│ Available at initial pharmacy?  │
└─────────────────────────────────┘
          ↓ Yes          ↓ No
          ↓               ↓
Initial pharmacy     Search other
availability         registered pharmacies
                         ↓
                  Alternative pharmacy
                         ↓
                    Email notification

When the medication is not available at the initially selected pharmacy, the pharmacist can consult other pharmacies registered on the platform and select an alternative pharmacy with reported availability.

The availability used by the MVP represents the quantity reported and registered by the pharmacies on the platform. It does not represent real-time inventory.

Security

The API uses Spring Security + JWT for authentication and authorization.

Access Roles
SYSTEM_ADMIN
PHARMACY_MANAGER
PHARMACIST
PATIENT

Each role has specific permissions according to the functionality being accessed.

Passwords are protected using BCrypt.

Authentication Endpoint
POST /api/v1/auth/login

Authenticated requests use the JWT token through the following header:

Authorization: Bearer <token>
API Documentation

The API uses Swagger / OpenAPI for technical documentation and endpoint testing.

Swagger provides:

API endpoint visualization
HTTP method information
Request parameters
Request and response models
Endpoint testing through the Swagger interface
Swagger UI
http://localhost:8080/swagger-ui.html
OpenAPI Specification
http://localhost:8080/v3/api-docs
Validation and Exception Handling

The application uses Spring Validation to validate request data.

Validation includes:

Required fields
Email format
Invalid values
Business rules
Data consistency

Application exceptions are handled centrally through a GlobalExceptionHandler.

Tests

Unit tests were developed using:

JUnit
Mockito

To execute the tests:

mvn test
Database

The application uses PostgreSQL for data persistence.

The persistence layer is implemented using:

Spring Data JPA
Hibernate
PostgreSQL
Email Integration

The project uses Mailtrap to validate the email notification flow in a test environment.

The integration uses:

Mailtrap Java SDK
Mailtrap Email Sandbox

After the request analysis, the system sends the result to the patient by email.

When the medication is available, the message may include:

Medication: [medication name]

Pharmacy: [pharmacy name]

Address: [pharmacy address]

Result: Medication available for pickup.
Docker

The application can be executed using Docker Compose.

The environment includes:

Unified Pharmacies API
PostgreSQL 16
pgAdmin 4
Services and Ports
Service	Port
API	8080
PostgreSQL	5433
pgAdmin 4	5051
Start the application
docker compose up --build
Check running containers
docker compose ps
Stop the services
docker compose down
Environment Variables

Sensitive configuration is provided through environment variables.

Example:

MAILTRAP_TOKEN=
MAILTRAP_INBOX_ID=
MAILTRAP_FROM_EMAIL=
JWT_SECRET=
POSTGRES_PASSWORD=
PGADMIN_PASSWORD=

The .env file should not be committed to the repository.

A .env.example file can be used to document the required environment variables without exposing sensitive values.

How to Run the Project
Prerequisites
Git
Docker
Docker Compose
Clone the repository
git clone https://github.com/vivianedesousa/unified-pharmacies-api.git
cd unified-pharmacies-api
Configure environment variables

Create a .env file and configure the required variables.

Start the application
docker compose up --build

After starting the containers:

API

http://localhost:8080

Swagger UI

http://localhost:8080/swagger-ui.html

pgAdmin 4

http://localhost:5051
MVP Scope

Municipality: Serra/ES

Program: Brazilian Popular Pharmacy Program

Initial category: Diabetes medications

The medication and EAN information used in the MVP are based on the official medication list provided by the Brazilian Ministry of Health.

Official References
Brazilian Popular Pharmacy Program

https://www.gov.br/saude/pt-br/composicao/sectics/farmacia-popular

Establishment Search

https://infoms.saude.gov.br/extensions/SEIDIGI_DEMAS_PFPB_ENDERECOS/index.html

Medication and EAN List

https://www.gov.br/saude/pt-br/composicao/sectics/farmacia-popular/codigos-de-barras/2026/lista-de-medicamentos-ean-junho-2026.pdf/@@download/file

Future Improvements

Possible future improvements include:

Expansion to other medication categories
Expansion to other municipalities and states
More frequent availability updates
Integration with official data sources
Digital prescription upload and secure storage
Automated prescription analysis
Additional notification channels
Frontend application
Audit and traceability improvements
Monitoring and observability
Preparation for production environments and larger scale
Project Evolution

The MVP was developed with a controlled initial scope to validate the main business flow.

MVP
Serra/ES
   +
Diabetes
   ↓
Expansion
   ↓
Other medication categories
   +
Other municipalities
   +
Other states
Academic Context

Project developed in the context of the FIAP Postgraduate Program, as part of the challenge:

“Innovation for optimizing healthcare services in the SUS”

Repository

https://github.com/vivianedesousa/unified-pharmacies-api


