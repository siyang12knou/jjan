cd client
flutter build web --release
rmdir /s /q ..\server\src\main\resources\static
mkdir ..\server\src\main\resources\static
xcopy build\web ..\server\src\main\resources\static /E
cd ..\server
.\mvnw clean package -DskipTests=true
