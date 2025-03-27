# movie-app
# 🎬 Movie App

This project is a fully-functional movie application developed using Spring Boot, Hibernate, PostgreSQL, and Angular. The system supports user authentication with JWT and provides distinct functionalities for regular users and administrators.

---

## ✨ Features

1. **User Management**: Users can sign up, log in, and navigate pages based on their role. They can search for specific movies, view full details, and submit individual ratings. Each user only sees their own ratings.
2. **Admin Dashboard**: Admins can view all movies in the local database, search movies from the OMDB API, view details, add new movies by title, and delete movies by IMDb ID.
3. **Pagination**: Movies are displayed in a paginated list for easier navigation.
4. **Search Bar**: Users can search movies by title.
5. **Ratings**: Customers can rate movies independently. Ratings are stored per user and average ratings are calculated for reference.

---

## 🧰 Technology Stack

- **Backend**: Spring Boot, Hibernate, PostgreSQL  
- **Frontend**: Angular, Bootstrap  
- **Security**: Spring Security, JWT Authentication  
- **External API**: OMDB API

---

## 🚀 Steps for Running the Application

### 🖥️ Backend (Spring Boot)

```bash
git clone https://github.com/reemelshinawy/movie-app.git
cd movie-app
```

#### Setup PostgreSQL Database

```sql
CREATE DATABASE moviedb;
```

#### Configure `application.properties`

```properties
server.port=9090
spring.datasource.url=jdbc:postgresql://localhost:5432/moviedb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create
```

#### Install Dependencies & Run the App

```bash
mvn clean install
```

Open in browser: `http://localhost:9090`

---

### 🌐 Frontend (Angular)

```bash
cd MOVIE_DASHBOARD
npm install
ng serve --open
```

Open in browser: `http://localhost:4200`

---

## 🔗 API Endpoints

### 🔐 Auth Controller

- `POST /api/auth/signup` → Register a new user  
- `POST /api/auth/login` → Log in and receive JWT

### 🛡️ Admin Controller

- `GET /api/admin/search?title=spiderman` → Search for movie in OMDB  
- `POST /api/admin/movies?imdbID=tt0371823` → Add movie by imdbID  
- `GET /api/admin/movies` → List all movies  
- `DELETE /api/admin/movies/{id}` → Delete movie by ID





### 👥 User Controller

- `GET /api/user/movies` → List all movies  
- `GET /api/user/movies/{id}` → Get movie by ID  
- `GET /api/user/movies/search?title=guardian` → Search movies by title  
- `POST /api/user/movies/{id}/rate?rating=9` → Rate a specific movie

---
## 📽️ Demo

Watch a short demo of the Movie App in action:  
[Click here to view on Google Drive](https://drive.google.com/file/d/1-s_Yh42OMX-ukod2NjlSFRy9GkahW3RQ/view?usp=sharing)
