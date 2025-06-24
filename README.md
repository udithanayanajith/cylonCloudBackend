💸 Loan Service API
===================

A mini-loan system backend built with Spring Boot + JDBC (no JPA) + MySQL.

🔗 Project GitHub
-----------------

[https://github.com/udithanayanajith/cylonCloudBackend.git](https://github.com/udithanayanajith/cylonCloudBackend.git)

🚀 Features
-----------

*   Register a loan for a customer
*   Auto-generate loan schedule (weekly/monthly)
*   Register payments for loan schedules
*   Generate detailed financial reports

📦 Tech Stack
-------------

*   **Java:** 17+
*   **Spring Boot:** 3.x
*   **Database:** MySQL
*   **Persistence:** JDBC Template (no JPA)
*   **Build Tool:** Maven

📁 Project Structure
--------------------

com.uditha.loan\_service
├── controller       // REST APIs
├── service          // Business logic
├── repository       // SQL logic via JdbcTemplate
└── models           // DTOs for requests/responses


⚙️ Setup Instructions
---------------------

1.  Clone the repo: `git clone https://github.com/udithanayanajith/cylonCloudBackend.git`
2.  Create a MySQL database (e.g., `loan_db`)
3.  Run the SQL schema provided
4.  Edit `application.properties` with your DB config:

spring.datasource.url=jdbc:mysql://localhost:3306/loan\_db
spring.datasource.username=root
spring.datasource.password=your\_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


📫 Author
---------

Made with 💙 by **Uditha Nayanajith**