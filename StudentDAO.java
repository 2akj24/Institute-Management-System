import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentDAO {

    // -------------------------------------------------------
    // ADD STUDENT
    // -------------------------------------------------------
    public void addStudent(String name, String mobile, String email,
                           String address, String dob, String gender) {
        String sql = "INSERT INTO student (name, mobile, email, address, date_of_birth, gender) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setString(3, email.isEmpty()   ? null : email);
            ps.setString(4, address.isEmpty() ? null : address);
            ps.setString(5, dob.isEmpty()     ? null : dob);   // format: YYYY-MM-DD
            ps.setString(6, gender);

            ps.executeUpdate();
            System.out.println("✔  Student Added Successfully.");

        } catch (Exception e) {
            System.err.println("✘  Error adding student: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // VIEW ALL STUDENTS
    // -------------------------------------------------------
    public void viewStudents() {
        String sql = "SELECT student_id, name, mobile, email, address, date_of_birth, gender " +
                "FROM student ORDER BY student_id";
        try (Connection con = DBConnection.getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            System.out.println("\n+----+----------------------+---------------+----------------------------+");
            System.out.printf( "| %-2s | %-20s | %-13s | %-26s |%n", "ID", "Name", "Mobile", "Email");
            System.out.println("+----+----------------------+---------------+----------------------------+");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("| %-2d | %-20s | %-13s | %-26s |%n",
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("mobile"),
                        rs.getString("email") != null ? rs.getString("email") : "-"
                );
                System.out.printf("|    | DOB: %-16s | Gender: %-5s | Address: %-17s |%n",
                        rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toString() : "-",
                        rs.getString("gender")      != null ? rs.getString("gender") : "-",
                        rs.getString("address")     != null ? rs.getString("address") : "-"
                );
                System.out.println("+----+----------------------+---------------+----------------------------+");
            }
            if (!found) System.out.println("| No student records found.                                       |");

        } catch (Exception e) {
            System.err.println("✘  Error viewing students: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // UPDATE STUDENT
    // -------------------------------------------------------
    public void updateStudent(int id, String name, String mobile,
                              String email, String address, String dob, String gender) {
        String sql = "UPDATE student SET name=?, mobile=?, email=?, address=?, " +
                "date_of_birth=?, gender=? WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setString(3, email.isEmpty()   ? null : email);
            ps.setString(4, address.isEmpty() ? null : address);
            ps.setString(5, dob.isEmpty()     ? null : dob);
            ps.setString(6, gender);
            ps.setInt(7, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                    ? "✔  Student Updated Successfully."
                    : "✘  No student found with ID: " + id);

        } catch (Exception e) {
            System.err.println("✘  Error updating student: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // DELETE STUDENT
    // -------------------------------------------------------
    public void deleteStudent(int id) {
        String sql = "DELETE FROM student WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0
                    ? "✔  Student Deleted Successfully."
                    : "✘  No student found with ID: " + id);

        } catch (Exception e) {
            System.err.println("✘  Error deleting student: " + e.getMessage());
        }
    }
}