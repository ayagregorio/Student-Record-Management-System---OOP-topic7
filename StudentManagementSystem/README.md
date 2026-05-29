# Student Record Management System
JavaFX + JDBC + PostgreSQL CRUD Application

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL (running locally on port 5432)
- VS Code with Extension Pack for Java

---

## 1. Database Setup

Open pgAdmin or psql and run:

```sql
CREATE DATABASE studentdb;

\c studentdb

CREATE TABLE students (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100),
    course     VARCHAR(50),
    year_level VARCHAR(20)
);
```

---

## 2. Configure Database Password

Open `src/main/java/com/student/DBConnection.java` and update:

```java
private static final String PASSWORD = "your_password";  // ← change this
```

---

## 3. Run the Application

### Option A – VS Code Terminal
```bash
mvn javafx:run
```

### Option B – VS Code Run Button
- Open `MainApp.java`
- Click the ▶ Run button (requires Extension Pack for Java)

---

## Project Structure

```
StudentManagementSystem/
├── pom.xml
└── src/main/
    ├── java/
    │   ├── module-info.java
    │   └── com/student/
    │       ├── MainApp.java        ← Entry point
    │       ├── Controller.java     ← CRUD logic
    │       ├── DBConnection.java   ← PostgreSQL connection
    │       ├── Student.java        ← Model (TableView binding)
    │       └── YearLevel.java      ← Enum (ChoiceBox values)
    └── resources/com/student/
        ├── main.fxml               ← UI layout
        └── style.css               ← Styling
```

---

## Features
- ✅ Add new student records
- ✅ View all records in a styled TableView
- ✅ Click a row to auto-fill fields for editing
- ✅ Update selected student
- ✅ Delete selected student (with confirmation dialog)
- ✅ Clear/reset all input fields
- ✅ Input validation (no empty fields allowed)
- ✅ Status messages for feedback
