import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class EnrollmentDAO {

    // -------------------------------------------------------
    // ENROLL STUDENT
    // -------------------------------------------------------
    public void enrollStudent(int studentId, int courseId,
                              double discountGiven, double feesPaid, String paymentStatus) {

        // Validate discount does not exceed course max_discount
        String checkDiscountSql = "SELECT max_discount, fees FROM course WHERE course_id=?";
        String checkDupSql      = "SELECT COUNT(*) FROM enrollment WHERE student_id=? AND course_id=?";
        String insertSql        = "INSERT INTO enrollment " +
                "(student_id, course_id, enrolled_date, discount_given, fees_paid, payment_status) " +
                "VALUES (?, ?, CURRENT_DATE, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            // 1) Check course exists and get max_discount + fees
            double maxDiscount = 0, courseFees = 0;
            try (PreparedStatement ps = con.prepareStatement(checkDiscountSql)) {
                ps.setInt(1, courseId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("✘  Course not found with ID: " + courseId);
                        return;
                    }
                    maxDiscount = rs.getDouble("max_discount");
                    courseFees  = rs.getDouble("fees");
                }
            }

            if (discountGiven > maxDiscount) {
                System.out.printf("✘  Discount %.2f%% exceeds max allowed %.2f%% for this course.%n",
                        discountGiven, maxDiscount);
                return;
            }

            double effectiveFees = courseFees - (courseFees * discountGiven / 100);
            if (feesPaid > effectiveFees) {
                System.out.printf("✘  Fees paid %.2f exceeds effective fees %.2f.%n", feesPaid, effectiveFees);
                return;
            }

            // 2) Check duplicate enrollment
            try (PreparedStatement ps = con.prepareStatement(checkDupSql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("✘  Student is already enrolled in this course.");
                        return;
                    }
                }
            }

            // 3) Insert enrollment
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDouble(3, discountGiven);
                ps.setDouble(4, feesPaid);
                ps.setString(5, paymentStatus);
                ps.executeUpdate();
            }

            System.out.printf("✔  Enrollment Done. Effective Fees: ₹%.2f | Paid: ₹%.2f | Status: %s%n",
                    effectiveFees, feesPaid, paymentStatus);

        } catch (Exception e) {
            System.err.println("✘  Error enrolling student: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // VIEW ALL ENROLLMENTS
    // -------------------------------------------------------
    public void viewEnrollments() {
        String sql =
                "SELECT e.enrollment_id, s.student_id, s.name AS student_name, s.mobile, " +
                        "       c.course_id, c.course_name, c.fees AS original_fees, " +
                        "       e.discount_given, e.fees_paid, e.payment_status, e.enrolled_date " +
                        "FROM enrollment e " +
                        "JOIN student s ON e.student_id = s.student_id " +
                        "JOIN course  c ON e.course_id  = c.course_id " +
                        "ORDER BY e.enrollment_id";

        try (Connection con = DBConnection.getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            System.out.println("\n+------+----------------------+---------------+---------------------------+");
            System.out.printf( "| %-4s | %-20s | %-13s | %-25s |%n",
                    "EnID", "Student Name", "Mobile", "Course");
            System.out.println("|      | Orig.Fees  | Discount | Paid       | Status  | Date       |");
            System.out.println("+------+----------------------+---------------+---------------------------+");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("| %-4d | %-20s | %-13s | %-25s |%n",
                        rs.getInt("enrollment_id"),
                        rs.getString("student_name"),
                        rs.getString("mobile"),
                        rs.getString("course_name")
                );
                System.out.printf("|      | %10.2f | %7.2f%% | %10.2f | %-7s | %-10s |%n",
                        rs.getDouble("original_fees"),
                        rs.getDouble("discount_given"),
                        rs.getDouble("fees_paid"),
                        rs.getString("payment_status"),
                        rs.getDate("enrolled_date").toString()
                );
                System.out.println("+------+----------------------+---------------+---------------------------+");
            }
            if (!found) System.out.println("| No enrollment records found.                                   |");

        } catch (Exception e) {
            System.err.println("✘  Error viewing enrollments: " + e.getMessage());
        }
    }
}