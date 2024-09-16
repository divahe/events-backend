
### README
#### Overview
This application is designed for demo purposes only and should not be considered secure. It lacks HTTPS encryption and has known vulnerabilities in the backend. The application is composed of code from two repositories:

- https://github.com/divahe/events-backend.git
- https://github.com/divahe/events-frontend.git

### Backend
The backend project is developed in Java using the Spring Boot framework and is served by Tomcat on port 8080.

#### Configuration
The backend requires additional configuration in the application.properties file. Replace the demo values with actual values for following environment variables:

```sh
administration.username=admin@gmail.com
administration.password=password123
application.security.jwt.secret-key=<your-secret-key>
application.security.jwt.expiration=86400000  # 1 day
application.security.jwt.refresh-token.expiration=604800000  # 7 days
```

Note: JWT is configured in HS256 format in JwtService.java.

### Frontend
The frontend project is written in TypeScript using the Vue 3 framework and utilizes Vite for build and development. The project includes linter and Prettier dependencies to aid in development.

### Dependencies

[ Docker ](https://docs.docker.com/get-started/get-docker/)

[ Git client ](https://git-scm.com/downloads)

[ Java v 21 ](https://www.oracle.com/java/technologies/downloads/#java21)

[ Node.js v 20 ](https://nodejs.org/en/download/package-manager)

### Setup for deployment

- Backend: Tomcat on port 8080
- Frontend: Nginx on port 80, should be mapped to host port 5173 to avoid CORS issues. Another option is to reconfigure Spring Security settings.
- Ensure Docker Desktop is running to build images.

To build an image for either the frontend or backend, use the following command in the folder where Dockerfile is located:


```sh
docker build -t <image-name> .
```
For example, to build the frontend image:

```sh
docker build -t events-frontend .
```
Deploy uses Linux-based images (Alpine), which was chosen for the relatively smaller size. For building images for other platforms, refer to the <br>
[ multi-platform Docker build guide. ](https://docs.docker.com/build/building/multi-platform/) <br>
Example command for a Windows platform:

```sh
docker buildx build -t events-frontend --platform windows/amd64 .
```

Create a repository on DockerHub for both backend and frontend.
- Tag the image:
```sh
docker tag <local-image> <dockerhub-username>/<repository-name>
```
Push the image to your DockerHub repository:
```sh
docker push <dockerhub-username>/<repository-name>
```
By default, the tag “latest” is used if no specific tag is provided.

### Local Development Setup
#### Frontend
Recommended IDE: Visual Studio Code.<br>
Recommended browser is Brave.<br>
Frontend uses Vite server on port 5173, which is available on the browser localhost.<br>

After cloning the frontend repository, install dependencies from the project folder:
```sh
npm install
```
Start the development server:
```sh
npm run dev
```
#### Backend:
Recommended IDEA: IntelliJ.<br>
Set the Java version to 21. IntelliJ will notify if it is not set.<br>
Set Java version in IntelliJ: File > Project Structure... > Project<br>
Select Java 21 or add it if not present.<br>

