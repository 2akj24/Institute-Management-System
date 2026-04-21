import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CourseDAO {

    // -------------------------------------------------------
    // ADD COURSE
    // -------------------------------------------------------
    public void addCourse(String courseName, double fees,
                          double maxDiscount, String duration, String description) {
        String sql = "INSERT INTO course (course_name, fees, max_discount, duration, description) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseName);
            ps.setDouble(2, fees);
            ps.setDouble(3, maxDiscount);
            ps.setString(4, duration);
            ps.setString(5, description.isEmpty() ? null : description);

            ps.executeUpdate();
            System.out.println("✔  Course Added Successfully.");

        } catch (Exception e) {
            System.err.println("✘  Error adding course: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // VIEW ALL COURSES
    // -------------------------------------------------------
    public void viewCourses() {
        String sql = "SELECT course_id, course_name, fees, max_discount, duration, description " +
                "FROM course ORDER BY course_id";
        try (Connection con = DBConnection.getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            System.out.println("\n+----+---------------------------+----------+----------+-----------+");
            System.out.printf( "| %-2s | %-25s | %-8s | %-8s | %-9s |%n",
                    "ID", "Course Name", "Fees", "Max Dis%", "Duration");
            System.out.println("+----+---------------------------+----------+----------+-----------+");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("| %-2d | %-25s | %8.2f | %7.2f%% | %-9s |%n",
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getDouble("fees"),
                        rs.getDouble("max_discount"),
                        rs.getString("duration")
                );
                String desc = rs.getString("description");
                if (desc != null && !desc.isEmpty()) {
                    System.out.printf("|    | Desc: %-55s |%n", desc);
                }
                System.out.println("+----+---------------------------+----------+----------+-----------+");
            }
            if (!found) System.out.println("| No courses found.                                              |");

        } catch (Exception e) {
            System.err.println("✘  Error viewing courses: " + e.getMessage());
        }
    }
}