This project requires Java 17 and Maven to build.

Why: The project uses Spring Boot 3.x and the Maven compiler is configured with <release>17</release> in pom.xml.

Quick steps (Windows PowerShell):

1) Install a JDK 17 (Adoptium Temurin or Azul or Oracle). Example Adoptium:
   - https://adoptium.net
   - Download and run the MSI for Windows x64 (Temurin 17). Install to e.g. C:\Program Files\Java\jdk-17

2) Set JAVA_HOME for your PowerShell session and update PATH (temporary, current terminal only):

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'
$env:PATH = $env:JAVA_HOME + '\\bin;' + $env:PATH
java -version
mvn -v
```

3) Build the project (skip tests to speed up):

```powershell
mvn -DskipTests clean package
```

4) Run tests:

```powershell
mvn test
```

Notes:
- To permanently set JAVA_HOME on Windows, set it in System Properties -> Environment Variables.
- If you use IntelliJ IDEA: set the Project SDK to the installed JDK 17 and reload the Maven project.
- If you need me to update the repo (e.g., add a Maven wrapper or lower Java target), tell me which option you prefer.
