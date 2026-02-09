# run-local.ps1
# Verify JDK 17 is available, build the project and run with Maven (PowerShell)
# Usage: Open PowerShell in project root and run: .\run-local.ps1

# Check java version
Write-Host "java -version"
java -version

# Check mvn
Write-Host "mvn -v"
mvn -v

if ($LASTEXITCODE -ne 0) {
    Write-Warning "One of java or mvn failed above; ensure JDK 17 is installed and JAVA_HOME is set."
}

# Build
Write-Host "Running mvn clean package (skipping tests)..."
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) { Write-Error "Maven build failed"; exit 1 }

# Run
Write-Host "Starting application (mvn spring-boot:run)..."
mvn spring-boot:run

