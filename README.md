User Registration

Fields: Name, Age, Date of Birth, Contact Number, Country, State, City

Validations:

Name: No numbers or special characters

Age: 0 < age <= 80

DOB: Valid date

Contact: Only numbers with + and - allowed

Drop-downs: populated from backend

✅ Login

Dummy password generated at registration

Authenticates against backend

✅ Product CRUD

Fields: Name, Unit Price, Category, HSN Code

Validations:

Name: No numbers or special characters

Unit Price: Decimal only

Category: Populated drop-down (e.g., book, fruit)

HSN Code: Numbers only

🧰 Tech Stack
💻 Java 17+

☕ Spring Boot 3.x

🗃️ H2 in-memory database

🔒 Spring Security

🖥️ JavaFX 17+

📦 Maven

🔷 Setup Instructions
📁 Backend
1️⃣ Navigate to the backend/ folder
2️⃣ Build and run:

bash
Copy
Edit
cd backend
mvn spring-boot:run
Backend runs at: http://localhost:8080

📁 Frontend
1️⃣ Navigate to the frontend/ folder
2️⃣ Build and run:

bash

cd frontend
mvn javafx:run
Frontend opens the login/registration screen.

📄 Available APIs
User APIs:
Method	Endpoint	Description
POST	/api/users/register	Register a new user
POST	/api/users/login	Login with name + password
GET	/api/users/states	Get list of states
GET	/api/users/cities	Get list of cities by state
