# APAR Management System — NIC Tripura

Fresh implementation from the supplied project report. Stack: React 18 + Vite + Tailwind, Spring Boot 3.3 / Java 17, Spring Security, JWT, BCrypt, Spring JDBC, PostgreSQL 16 (`tran` schema), Docker Compose and Nginx.

## Run locally
Prerequisite: Docker Desktop / Docker Engine with Compose.

```bash
docker compose up --build
```
Open http://localhost:5173. For local classroom demo only, set `APAR_LOAD_DEMO_SEED=true` (already set in compose). Demo logins: `authority@nic.in` and `employee@nic.in`, password `Demo@12345`. Fictional demo data only.

Reset demo database: `docker compose down -v` then `docker compose up --build`.

## Deployment
Vercel hosts the frontend only. Deploy Spring Boot and PostgreSQL separately (e.g. Railway). See `DEPLOYMENT_GUIDE.md`. Set Vercel `VITE_API_BASE_URL` to the deployed API base ending in `/api`. Never expose DB credentials or JWT secrets in frontend variables.

## Scope
Employee registration/login; authority employee CRUD and directory filters; targeted/multi/department/all notifications; authority-requested APAR submissions; annual uniqueness; grading, remarks, flags and reason-based resubmission; unread notifications; audit and revision history; 10-second dashboard polling. WebSockets, email password reset, PDF export, SSO and mobile app are deferred as in the report.

## Important
This source package is freshly authored and includes local demo seeding. Run the `COLLEGE_DEMO_CHECKLIST.md` against the target deployment before presenting it as production-validated. The CI workflow compiles the frontend and runs Maven verification; live end-to-end testing must be done in the deployment environment. Do not use demo credentials or fictional records in a real deployment.
