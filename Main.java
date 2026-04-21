import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentDAO    studentDAO    = new StudentDAO();
        CourseDAO     courseDAO     = new CourseDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

        while (true) {
            System.out.println("\n+=========================================+");
            System.out.println("|     MB INSTITUTE MANAGEMENT SYSTEM      |");
            System.out.println("+=========================================+");
            System.out.println("|  1. Add Student                         |");
            System.out.println("|  2. View Students                       |");
            System.out.println("|  3. Update Student                      |");
            System.out.println("|  4. Delete Student                      |");
            System.out.println("+-----------------------------------------+");
            System.out.println("|  5. Add Course                          |");
            System.out.println("|  6. View Courses                        |");
            System.out.println("+-----------------------------------------+");
            System.out.println("|  7. Enroll Student in Course            |");
            System.out.println("|  8. View Enrollments                    |");
            System.out.println("+-----------------------------------------+");
            System.out.println("|  9. Exit                                |");
            System.out.println("+=========================================+");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number 1-9.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline after every nextInt()

            switch (choice) {

                // ------------------------------------------------
                // 1. ADD STUDENT
                // ------------------------------------------------
                case 1: {
                    System.out.println("\n--- Add New Student ---");

                    System.out.print("Full Name         : ");
                    String name = sc.nextLine().trim();

                    System.out.print("Mobile Number     : ");
                    String mobile = sc.nextLine().trim();

                    System.out.print("Email (optional)  : ");
                    String email = sc.nextLine().trim();

                    System.out.print("Address (optional): ");
                    String address = sc.nextLine().trim();

                    System.out.print("Date of Birth (YYYY-MM-DD, optional): ");
                    String dob = sc.nextLine().trim();

                    System.out.print("Gender (Male/Female/Other): ");
                    String gender = sc.nextLine().trim();

                    if (name.isEmpty() || mobile.isEmpty() || gender.isEmpty()) {
                        System.out.println("✘  Name, Mobile and Gender are required.");
                        break;
                    }
                    if (!mobile.matches("\\d{10,15}")) {
                        System.out.println("✘  Mobile must be 10-15 digits.");
                        break;
                    }
                    if (!gender.equalsIgnoreCase("Male") &&
                            !gender.equalsIgnoreCase("Female") &&
                            !gender.equalsIgnoreCase("Other")) {
                        System.out.println("✘  Gender must be Male, Female, or Other.");
                        break;
                    }

                    // Capitalize gender for DB ENUM
                    gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
                    studentDAO.addStudent(name, mobile, email, address, dob, gender);
                    break;
                }

                // ------------------------------------------------
                // 2. VIEW STUDENTS
                // ------------------------------------------------
                case 2:
                    studentDAO.viewStudents();
                    break;

                // ------------------------------------------------
                // 3. UPDATE STUDENT
                // ------------------------------------------------
                case 3: {
                    System.out.println("\n--- Update Student ---");
                    System.out.print("Enter Student ID  : ");
                    if (!sc.hasNextInt()) { sc.nextLine(); System.out.println("Invalid ID."); break; }
                    int uid = sc.nextInt(); sc.nextLine();

                    System.out.print("New Full Name         : ");
                    String name = sc.nextLine().trim();

                    System.out.print("New Mobile Number     : ");
                    String mobile = sc.nextLine().trim();

                    System.out.print("New Email (optional)  : ");
                    String email = sc.nextLine().trim();

                    System.out.print("New Address (optional): ");
                    String address = sc.nextLine().trim();

                    System.out.print("New DOB (YYYY-MM-DD, optional): ");
                    String dob = sc.nextLine().trim();

                    System.out.print("New Gender (Male/Female/Other): ");
                    String gender = sc.nextLine().trim();

                    if (name.isEmpty() || mobile.isEmpty() || gender.isEmpty()) {
                        System.out.println("✘  Name, Mobile and Gender are required.");
                        break;
                    }
                    gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
                    studentDAO.updateStudent(uid, name, mobile, email, address, dob, gender);
                    break;
                }

                // ------------------------------------------------
                // 4. DELETE STUDENT
                // ------------------------------------------------
                case 4: {
                    System.out.print("Enter Student ID to delete: ");
                    if (!sc.hasNextInt()) { sc.nextLine(); System.out.println("Invalid ID."); break; }
                    int delId = sc.nextInt(); sc.nextLine();
                    studentDAO.deleteStudent(delId);
                    break;
                }

                // ------------------------------------------------
                // 5. ADD COURSE
                // ------------------------------------------------
                case 5: {
                    System.out.println("\n--- Add New Course ---");

                    System.out.print("Course Name             : ");
                    String courseName = sc.nextLine().trim();

                    System.out.print("Fees (₹)                : ");
                    double fees = 0;
                    try { fees = Double.parseDouble(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println("✘  Invalid fees amount."); break; }

                    System.out.print("Max Discount (%)        : ");
                    double maxDiscount = 0;
                    try { maxDiscount = Double.parseDouble(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println("✘  Invalid discount value."); break; }

                    System.out.print("Duration (e.g. 3 Months): ");
                    String duration = sc.nextLine().trim();

                    System.out.print("Description (optional)  : ");
                    String description = sc.nextLine().trim();

                    if (courseName.isEmpty() || duration.isEmpty()) {
                        System.out.println("✘  Course Name and Duration are required.");
                        break;
                    }
                    if (fees <= 0) {
                        System.out.println("✘  Fees must be greater than 0.");
                        break;
                    }
                    if (maxDiscount < 0 || maxDiscount > 100) {
                        System.out.println("✘  Discount must be between 0 and 100.");
                        break;
                    }
                    courseDAO.addCourse(courseName, fees, maxDiscount, duration, description);
                    break;
                }

                // ------------------------------------------------
                // 6. VIEW COURSES
                // ------------------------------------------------
                case 6:
                    courseDAO.viewCourses();
                    break;

                // ------------------------------------------------
                // 7. ENROLL STUDENT
                // ------------------------------------------------
                case 7: {
                    System.out.println("\n--- Enroll Student in Course ---");

                    // Show reference lists
                    studentDAO.viewStudents();
                    courseDAO.viewCourses();

                    System.out.print("Enter Student ID   : ");
                    if (!sc.hasNextInt()) { sc.nextLine(); System.out.println("Invalid ID."); break; }
                    int sid = sc.nextInt(); sc.nextLine();

                    System.out.print("Enter Course ID    : ");
                    if (!sc.hasNextInt()) { sc.nextLine(); System.out.println("Invalid ID."); break; }
                    int cid = sc.nextInt(); sc.nextLine();

                    System.out.print("Discount to give (%): ");
                    double discount = 0;
                    try { discount = Double.parseDouble(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println("✘  Invalid discount."); break; }

                    System.out.print("Fees Paid (₹)       : ");
                    double feesPaid = 0;
                    try { feesPaid = Double.parseDouble(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println("✘  Invalid fees amount."); break; }

                    System.out.print("Payment Status (Paid/Partial/Pending): ");
                    String status = sc.nextLine().trim();
                    if (!status.equalsIgnoreCase("Paid") &&
                            !status.equalsIgnoreCase("Partial") &&
                            !status.equalsIgnoreCase("Pending")) {
                        System.out.println("✘  Status must be Paid, Partial, or Pending.");
                        break;
                    }
                    // Capitalize for DB ENUM
                    status = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();

                    enrollmentDAO.enrollStudent(sid, cid, discount, feesPaid, status);
                    break;
                }

                // ------------------------------------------------
                // 8. VIEW ENROLLMENTS
                // ------------------------------------------------
                case 8:
                    enrollmentDAO.viewEnrollments();
                    break;

                // ------------------------------------------------
                // 9. EXIT
                // ------------------------------------------------
                case 9:
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("✘  Invalid choice. Please enter a number between 1 and 9.");
            }
        }
    }
}