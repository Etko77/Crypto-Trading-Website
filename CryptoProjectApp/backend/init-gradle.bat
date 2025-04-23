@echo off
echo Initializing Gradle wrapper...

:: Create gradle wrapper directory if it doesn't exist
if not exist "gradle\wrapper" mkdir "gradle\wrapper"

:: Download gradle-wrapper.jar
powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle\wrapper\gradle-wrapper.jar'"

echo Gradle wrapper initialized successfully!
echo You can now run: gradlew.bat build 