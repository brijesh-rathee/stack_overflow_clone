# Stack Overflow Clone

A full-stack web application mimicking core functionalities of Stack Overflow, built with **Spring Boot** and **Thymeleaf**. It allows users to ask and answer questions, vote on content, tag questions, comment, and manage their profiles.

---

## Features

- **User Management**  
  - Registration, authentication  
  - User profiles and profile updates  

- **Question & Answer System**  
  - Ask, view, edit, delete questions  
  - Provide, edit, delete answers  
  - Accept answers  

- **Voting System**  
  - Upvote/downvote questions and answers  
  - Reputation system  

- **Tagging**  
  - Associate tags with questions  
  - Filter by tags  
  - Follow/unfollow tags  

- **Commenting**  
  - Add comments to questions and answers  

- **Bookmarking**  
  - Bookmark questions  
  - View bookmarked questions  

- **Search & Filtering**  
  - Search by keyword  
  - Filter by tags  
  - Sort questions  

- **Badges**  
  - Awarding of badges  

- **Question Views**  
  - Tracking of question views  

---

## Technologies Used

**Backend:**  
- Spring Boot  
- Spring Data JPA  
- Spring Security  
- Lombok  
- PostgreSQL  
- Maven  
- Cloudinary (Optional, for image uploads)

**Frontend:**  
- Thymeleaf  
- HTML5  
- CSS3  
- JavaScript  

---

## Setup and Installation

### Prerequisites
- Java Development Kit (JDK) 17+
- Apache Maven 3.6.0+
- PostgreSQL Database
- Cloudinary Account (Optional)

### Steps

1. **Clone the Repository**
   ```bash
   git clone <your-repository-url>
   cd stackoverflow-clone
2. **Database Setup**
    Create a PostgreSQL database:
   ```sql
   CREATE DATABASE stackoverflow_clone_db;
   ```
   Configure src/main/resources/application.properties with your database credentials.
3. **Cloudinary Configuration**
    - Add Cloudinary dependencies to pom.xml
    - Add your Cloudinary credentials to application.properties
4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
5. **Access the App**
   - Navigate to http://localhost:8080

---

## Key API Endpoints

| Method | Endpoint                          | Description               |
|--------|-----------------------------------|---------------------------|
| GET    | `/auth/login`                     | Login page                |
| POST   | `/questions/submit`               | Submit a new question     |
| GET    | `/questions/{id}`                 | View a specific question  |
| POST   | `/answers/save/{questionId}`      | Add or update an answer   |
| GET    | `/users/{userId}`                 | View user profile         |
| GET    | `/tags`                           | View all tags             |
| POST   | `/bookmarks/{questionId}/toggle`  | Toggle bookmark           |

---

## Future Enhancements

- Advanced full-text search  
- Real-time notification system  
- Expanded unit and integration test coverage  
- Consider migrating frontend to a modern JavaScript framework  
- More granular access control  

---

## Contributing

1. Fork the repository  
2. Create your feature branch:  
   ```bash
   git checkout -b feature/YourFeature
3. Commit your changes:
   ```
   git commit -m 'Add some feature'
   ```
4. Push to the branch:
   ```
   git push origin feature/YourFeature
   ```
5. Submit a pull request
   








