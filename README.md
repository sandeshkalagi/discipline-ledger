# Discipline Ledger

Trading Journal and Capital Tracker web application.

## Stack

- Frontend: HTML, CSS, JavaScript
- Backend: Java Spring Boot
- Database: MySQL

## Project Structure

- `backend/` Spring Boot REST API
- `frontend/` static pages

## Backend Run

1. Create MySQL database and tables using `backend/src/main/resources/schema.sql`.
2. Update DB username/password in `backend/src/main/resources/application.properties`.
3. Run:
   - `cd backend`
   - `mvn spring-boot:run`

## Frontend Run

Open `frontend/dashboard.html` in browser.  
If you use VS Code, Live Server is recommended.

## First APIs

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/dashboard`
- `GET /api/settings`
- `PUT /api/settings`
- `GET /api/rules`
- `PUT /api/rules`
- `GET /api/statistics`
- `POST /api/trades`
- `GET /api/trades`
- `GET /api/trades/{id}`
- `PUT /api/trades/{id}`
- `DELETE /api/trades/{id}`
- `POST /api/journal-entries`
- `GET /api/journal-entries`
- `GET /api/journal-entries/{id}`
- `PUT /api/journal-entries/{id}`
- `DELETE /api/journal-entries/{id}`
