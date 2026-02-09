# run-docker.ps1
# Build and run the TransactionAPI Docker image and perform a quick smoke test (PowerShell).
# Usage: Open an elevated PowerShell in the project root and run: .\run-docker.ps1

param(
    [switch]$Rebuild
)

function Check-Command($cmd) {
    $err = & where.exe $cmd 2>$null
    return $LASTEXITCODE -eq 0
}

if (-not (Check-Command docker)) {
    Write-Error "docker is not installed or not on PATH. Please install Docker Desktop and try again."
    exit 1
}

$imageName = 'transaction-api:local'

if ($Rebuild) {
    Write-Host "Building Docker image $imageName..."
    docker build -t $imageName .
    if ($LASTEXITCODE -ne 0) { Write-Error "Docker build failed"; exit 2 }
}

# If image doesn't exist, build it
$exists = docker images -q $imageName
if (-not $exists) {
    Write-Host "Image not found locally. Building..."
    docker build -t $imageName .
    if ($LASTEXITCODE -ne 0) { Write-Error "Docker build failed"; exit 2 }
}

# Run container
Write-Host "Running container from image $imageName on port 8082..."
# stop any previous container
$old = docker ps -aq --filter "name=transaction-api"
if ($old) { docker rm -f $old | Out-Null }

docker run -d --name transaction-api -p 8082:8082 $imageName
if ($LASTEXITCODE -ne 0) { Write-Error "Failed to start container"; exit 3 }

Write-Host "Waiting up to 30s for the app to start..."
$started = $false
for ($i=0; $i -lt 30; $i++) {
    Start-Sleep -Seconds 1
    try {
        $r = Invoke-WebRequest -Uri http://localhost:8082/actuator/health -UseBasicParsing -ErrorAction SilentlyContinue
        if ($r -and $r.StatusCode -eq 200) { $started = $true; break }
    } catch { }
}

if (-not $started) {
    Write-Warning "App did not respond on /actuator/health within 30 seconds. Check container logs: docker logs transaction-api"
    exit 4
}

Write-Host "App is up. Performing a smoke test (login)..."
$body = '{"username":"user","password":"password"}'
try {
    $resp = Invoke-RestMethod -Method Post -Uri http://localhost:8082/auth/login -Body $body -ContentType 'application/json' -ErrorAction Stop
    Write-Host "Login response:`n" ($resp | ConvertTo-Json -Depth 4)
} catch {
    Write-Error "Login request failed: $_"
}

Write-Host "To stop and remove the container, run: docker rm -f transaction-api"

