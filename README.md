# 🏫 Institute Management System

A Java console-based application to manage Students, Courses, and Enrollments using **JDBC + MySQL**.

## 📋 Features
- Add / View / Update / Delete Students
- Add / View Courses with fees and discount info
- Enroll students in courses with payment tracking
- Duplicate enrollment prevention
- Input validation throughout

## 🛠️ Tech Stack
- Java (JDBC)
- MySQL
- MySQL Connector/J

## ⚙️ Setup Instructions
1. Import `institute_schema.sql` into MySQL
2. Update credentials in `DBConnection.java`
3. Compile:
   javac -cp .;mysql-connector-j-x.x.x.jar *.java
4. Run:
   java -cp .;mysql-connector-j-x.x.x.jar Main

## 🗄️ Database Schema
- `student` — ID, Name, Mobile, Email, Address, DOB, Gender
- `course` — ID, Name, Fees, Max Discount, Duration, Description
- `enrollment` — ID, Student, Course, Discount Given, Fees Paid, Payment Status
