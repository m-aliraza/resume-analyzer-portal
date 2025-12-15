# Resume Analyzer & Recruitment Management Portal

## 💼 Project Overview
A comprehensive backend system for a recruitment portal built with **Spring Boot** and **SQL Server**. The application facilitates the entire hiring process, from company and job creation to candidate application and resume parsing.

### Core Features
*   **Role-Based Access Control (RBAC):** Secure access for Admins, Recruiters, and Candidates.
*   **Authentication:** JWT-based stateless authentication with BCrypt password hashing.
*   **Candidate Workflow:** Profile management, Resume Upload (PDF/DOCX parsing via Apache Tika), and Job Application.
*   **Recruiter Workflow:** Job Posting, Applicant Review, Filtering (by Skills, GPA), and Status Updates.
*   **Admin Workflow:** Company and Recruiter management.
*   **Resume Parsing:** Automated extraction of text content from uploaded resumes without AI/ML dependencies.

---

## 📦 Technology Stack
*   **Language:** Java 17
*   **Framework:** Spring Boot 3.2+ (Web, Security, Data JPA)
*   **Database:** Microsoft SQL Server (Express)
*   **ORM:** Hibernate / Spring Data JPA
*   **Security:** Spring Security + JWT (JSON Web Tokens)
*   **Tools:** Maven, Lombok, Apache Tika (Document Parsing)
*   **Frontend:** Simple HTML/CSS/JS (for demonstration)

---

## 🧱 Database Schema (15 Entities)
The system is built on a normalized relational schema including:
*   **Auth:** `User`, `Role`, `UserRole`
*   **Organization:** `Company`, `RecruiterProfile`, `JobPosition`
*   **Candidate:** `CandidateProfile`, `Resume`, `Education`, `Experience`, `Skill`, `CandidateSkill`
*   **Process:** `Application`, `ApplicationStatusHistory`
*   **System:** `AuditLog`

---

## 🚀 How to Run

### Prerequisites
1.  **Java 17** installed.
2.  **SQL Server** (Express or Developer) running locally.
3.  **Maven** (optional, wrapper included).

### 1. Database Setup
1.  Create a database named `ResumePortalDB` in SQL Server.
2.  Enable **SQL Server Authentication** (Mixed Mode).
3.  Create a user `db_user` with password `Password123!` and grant `db_owner` on `ResumePortalDB`.
    *   *Note: Credentials can be changed in `src/main/resources/application.properties`.*

### 2. Build and Run
Open a terminal in the project root:

```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

### 3. Demo Data (Auto-Seeded)
On first startup, the `DataSeeder` automatically creates:
*   **Admin:** `admin` / `password123`
*   **Recruiter:** `recruiter` / `password123`
*   **Candidate:** `candidate` / `password123`
*   **Sample Company:** "Tech Solutions Inc."
*   **Sample Job:** "Junior Java Developer"

---

## 🌐 API Endpoints

### Authentication
*   `POST /api/auth/register` - Register new user
*   `POST /api/auth/login` - Login and receive JWT

### Company & Jobs
*   `POST /api/companies` - Create Company (Admin)
*   `POST /api/companies/{id}/jobs` - Post Job (Recruiter)
*   `GET /api/companies/jobs/active` - List all active jobs

### Candidates
*   `POST /api/candidates/profile` - Create/Update Profile
*   `POST /api/candidates/resume` - Upload Resume (Multipart File)

### Applications
*   `POST /api/applications/apply` - Apply to a job
*   `GET /api/applications/job/{id}/applicants` - View applicants (with filtering)
*   `PATCH /api/applications/{id}/status` - Update application status

---

## 🧪 Frontend Demo
Navigate to `http://localhost:8080` to access the simple web interface.
1.  **Login** with one of the seeded accounts.
2.  **Dashboard** adapts based on your role.
    *   **Candidate:** View jobs, Apply, Upload Resume.
    *   **Recruiter:** Post Jobs, Review Applicants.
    *   **Admin:** Create Companies.
