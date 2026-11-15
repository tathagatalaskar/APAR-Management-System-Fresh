# Deployment guide

## Local
Run `docker compose up --build`, then open `http://localhost:5173`. Compose starts PostgreSQL, Spring Boot API and Nginx-served Vite build. `/api` is proxied to the backend. Demo seed is enabled only in the local compose configuration.

## Hosted
Architecture: Vercel (React/Vite) → Railway (Spring Boot API) → Railway PostgreSQL. Vercel does not host Spring Boot or PostgreSQL.

### Backend service
- Build context: repository root
- Dockerfile: `backend/Dockerfile`
- Health path: `/api/health`
- Set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `APP_JWT_SECRET` (random 48+ byte secret), `APP_CORS_ORIGINS` (exact Vercel origin), `APAR_LOAD_DEMO_SEED=false`, `APAR_REMINDERS_ENABLED=true`.
- Ensure DB user can create schema/tables or apply `db/init/01_schema.sql` before API startup.
- Provision first authority securely using `POST /api/auth/bootstrap-authority` only when no authority exists, then disable/remove bootstrap access by setting `APP_BOOTSTRAP_ENABLED=false` and redeploy.

### Vercel
- Root directory `.`
- Framework: Vite
- Install command `npm install --prefix frontend`
- Build command `npm --prefix frontend run build`
- Output directory `frontend/dist`
- Environment variable (Preview + Production): `VITE_API_BASE_URL=https://YOUR-BACKEND/api`
- Add the exact Vercel origin to backend `APP_CORS_ORIGINS`.

Do not put backend secrets in Vercel. Redeploy after changing build-time Vite variables.

### First authority provisioning (hosted)
With `APP_BOOTSTRAP_ENABLED=true` and no authority account yet, make one request from a trusted terminal (use your own strong password):

```bash
curl -X POST "$API_BASE/auth/bootstrap-authority" \
  -H 'Content-Type: application/json' \
  -d '{"email":"authority@your-office.gov.in","password":"REPLACE_WITH_A_LONG_RANDOM_PASSWORD"}'
```

After the first authority is created, set `APP_BOOTSTRAP_ENABLED=false` and redeploy. The bootstrap endpoint also refuses to create a second authority.
