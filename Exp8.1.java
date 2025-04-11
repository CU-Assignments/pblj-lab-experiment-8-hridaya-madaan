import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class EmployeeServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/company_db";
    private static final String DB_USER = "root"; // replace with your DB username
    private static final String DB_PASSWORD = ""; // replace with your DB password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        // Get the employee ID from the request (if any)
        String empId = request.getParameter("emp_id");

        // Initialize the database connection and output writer
        PrintWriter out = response.getWriter();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare SQL query based on whether an ID was provided
            String query;
            if (empId != null && !empId.isEmpty()) {
                query = "SELECT * FROM employees WHERE emp_id = ?";
            } else {
                query = "SELECT * FROM employees";
            }

            PreparedStatement ps = conn.prepareStatement(query);

            if (empId != null && !empId.isEmpty()) {
                ps.setInt(1, Integer.parseInt(empId));
            }

            rs = ps.executeQuery();

            // Display employee details
            out.println("<html><body>");
            out.println("<h2>Employee List</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Employee ID</th><th>Name</th><th>Email</th><th>Department</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("emp_id");
                String name = rs.getString("emp_name");
                String email = rs.getString("emp_email");
                String department = rs.getString("emp_department");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("<td>" + department + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<html><body><h2>Error accessing database.</h2></body></html>");
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

