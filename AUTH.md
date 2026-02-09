Auth examples
=============

This file explains how to authenticate with the built-in test user and how to use the JWT to call protected endpoints.

Test user
---------
- username: user
- password: password

Important: The application must be running with Java 17 (pom.xml targets Java 17). See BUILD.md for setup instructions.

1) Obtain a token
-----------------
PowerShell:

```powershell
$body = @{ username = "user"; password = "password" } | ConvertTo-Json
$response = Invoke-RestMethod -Method Post -Uri http://localhost:8082/auth/login -Body $body -ContentType 'application/json'
$response.token
```

curl:

```bash
curl -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'
```

Response:

```json
{ "token": "eyJhbGciOi..." }
```

2) Call a protected endpoint
----------------------------
Replace <token> with the token value returned above.

PowerShell:

```powershell
Invoke-RestMethod -Uri http://localhost:8082/transactions -Headers @{ Authorization = "Bearer <token>" }
```

curl:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8082/transactions
```

3) Troubleshooting
------------------
- If you get 401 Unauthorized when calling /transactions, verify the token is present in the Authorization header and not expired.
- Make sure to set a secure `jwt.secret` value in `src/main/resources/application.properties` (32+ bytes). The project will fail at startup if it's missing or too short.
- Ensure you're running the app with Java 17 (see BUILD.md).

4) Debug token endpoint
-----------------------
For development you can enable a debug token endpoint that returns a JWT without authenticating credentials. This is controlled by `auth.debug=true` in `application.properties` (disabled by default in production).

PowerShell:

```powershell
$response = Invoke-RestMethod -Method Get -Uri "http://localhost:8082/auth/debug-token?username=user"
$response.token
```

curl:

```bash
curl "http://localhost:8082/auth/debug-token?username=user"
```

Response (debug-token):

```json
{ "token": "eyJhbGciOi...", "preview": "eyJhbGciOi..." }
```

Note: The production login response contains additional metadata:

```json
{
  "token": "eyJhbGciOi...",
  "type": "Bearer",
  "username": "user",
  "roles": ["ROLE_USER"]
}
```
