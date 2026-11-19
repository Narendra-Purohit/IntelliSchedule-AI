# IntelliSchedule-AI Setup Guide

## Prerequisites
- **JDK 21** or later
- **Python 3.11** or later
- **MySQL 8.0**
- **Docker & Docker Compose** (Optional for containerized run)

## Environment Variables
Create a root `.env` file with the following variables:
```env
DB_PASSWORD=your_mysql_password
GEMINI_API_KEY=your_gemini_api_key
JWT_SECRET=your_jwt_256_bit_secret_key
```

## Running with Docker Compose
```bash
docker-compose up --build
```
