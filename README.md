# **ClinicFlow**

## **Description of the problem**

Considering a hospital context, we have a system that focuses on the management of medical appointments, sending automatic reminders, as well as the history of consultations.   
In this system we have three levels of access: doctor and nurse, who manage the consultations and their history and patients, who can view the consultations related to the patient himself.

## **Objective of the project**

Based on this system, the objective of this project is to develop a simplified and modular back-end that works in a scalable way with services divided into modules.   
Ensuring an integration between appointment and notification services, all with a focus on security and asynchronous communication between these services. The query history service will not be implemented in this project.

## **Description of the Architecture**

The project has a modularization in two services: scheduling and notification, both following the hospital context. In the scheduling service we use the implementation of different levels of access to maintain limitations according to doctor, nurse and patient profiles.   
In the notification service we use the implementation of e-mail notification to the patient with information regarding queries associated with your e-mail, in this service we use only the e-mail information to carry out the communication. 

## **Technologies used**

What was used by both services: Java 21; Spring Boot 3.5.9; Maven; Spring Web; Spring Data JPA; Spring AMQP (RabbitMQ); Spring Boot DevTools; PostgreSQL 17; JPA/ Hibernate. What was used only by the scheduling service: Spring Security; Spring Validation; JWT and Jackson Databind.  
What was used only in the notification service: Spring Web; Spring Mail; Thymeleaf. What was used thinking about the infrastructure and devops: Docker; Docker Compose; RabbitMq 3; Git; Github.

## **Project configuration**

* Local prerequisites  
* First of all, make sure you have installed:  
* Docker  
* Docker Compose  
* JDK compatible with your services (in this case, 21\)  
* Maven or Gradle  
* Postman (or similar tool for request testing)

## **Design structure**

The structure of the project is this:

├── docker-compose.yml  
├── schedule/  
│   ├── Dockerfile  
│   └── src/main/resources/init.sql  
├── notification/  
│   └── Dockerfile

Each service has its Dockerfile configured to build the Spring Boot application. Each service has its own PostgreSQL database. 

## **Build and up of containers**

Clone the repository

\`\`\`bash  
git clone https://github.com/breudes/clinicflow.git  
cd clinicflow  
\`\`\`

At the root of the project:

`docker compose build` 
`docker compose up`

Or, if you prefer:

`docker compose up --build`

**Observation**: remember to change the MAIL\_USER and MAIL\_PASSWORD on the docker-compose file.

**Logical order of operation**

1. PostgreSQL (schedule)  
2. PostgreSQL (notification)  
3. RabbitMQ  
4. Schedule Service (producer)  
5. Notification Service (consumer)

## **Examples of request and response**

The examples for the scheduling service can be found in this collection in Postman:[https://api.postman.com/collections/47077132-d03c598d-bbd0-4316-95c9-fade6b50a697?access\_key=PMAT-01KEHV5C95MD2K0FKQ4WQR5GR1](https://api.postman.com/collections/47077132-d03c598d-bbd0-4316-95c9-fade6b50a697?access_key=PMAT-01KEHV5C95MD2K0FKQ4WQR5GR1).   
Remembering that the passwords referring to the 3 test users present in the init.sql file in the schedule folder are respectively: 123456, doctor123 and nurse123, via bcrypt encryption and cost factor 10 (rounds/cost factor).

## **Authentication & Authorization**

The schedule service uses JWT (JSON Web Token) for authentication. When logging in, the user receives a token that must be sent in the Authorization header for each protected request:

```makefile
Authorization: Bearer <token>
```

## **Author**

- Brenda Alexandra de Souza Silva  
- LinkedIn/Github: breudes

## **License**

This project is licensed under the MIT License. Feel free to use and modify it.
