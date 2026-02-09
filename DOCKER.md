# Running TransactionAPI with Docker (uses JDK 17)

This repository targets Java 17. If you don't want to install a local JDK 17, you can use Docker to build and run the app.

Prerequisites
- Docker installed and running

Build and run
1) Build the Docker image (from project root):

```powershell
docker build -t transaction-api:local .
```

2) Run the container (port 8082 exposed by the image):

```powershell
docker run --rm -p 8082:8082 --name transaction-api transaction-api:local
```

3) Test login with curl (once the app is up):

```bash
curl -X POST 'http://localhost:8082/auth/login' -H 'Content-Type: application/json' -d '{"username":"user","password":"password"}'
```

Expected response:
```json
{
  "token": "eyJhbGciOi...",
  "type": "Bearer",
  "username": "user",
  "roles": ["ROLE_USER"]
}
```

Notes
- The Docker build uses a Maven builder image with Temurin 17 so the compile will succeed even if your host Java is older.
- The Docker image builds the artifact and then runs it on a JRE 17 runtime.

