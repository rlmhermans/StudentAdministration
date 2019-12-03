import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String connectionString = "jdbc:sqlserver://localhost; databaseName=StudentAdministration; user=sa;password=.8aEzihW;";

    public Database() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver was not found.");
        }
    }

    public Student get(int id) {
        String name = "";

        try {
            Connection conn = DriverManager.getConnection(connectionString);
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Student WHERE id=?;");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                name = result.getString("name");
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("Error while getting student.");
        }

        return new Student(id, name);
    }

    public List<Student> getAll() {
        ArrayList<Student> students = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(connectionString);
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Student;");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                students.add(new Student(id, name));
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("Error while getting all students.");
        }

        return students;
    }

    public boolean create(String name) {
        boolean result = false;

        try {
            Connection conn = DriverManager.getConnection(connectionString);
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Student (name) VALUES (?);");
            statement.setString(1, name);
            statement.execute();
            result = true;
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error while creating student.");
        }

        return result;
    }

    public boolean update(Student student) {
        boolean result = false;

        try {
            Connection conn = DriverManager.getConnection(connectionString);
            PreparedStatement statement = conn.prepareStatement("UPDATE Student SET name = ? WHERE id = ?;");
            statement.setString(1, student.getName());
            statement.setInt(2, student.getId());
            statement.execute();
            result = true;
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error while updating student.");
        }

        return result;
    }

    public boolean remove(Student student) {
        boolean result = false;

        try {
            Connection conn = DriverManager.getConnection(connectionString);
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Student WHERE id = ?;");
            statement.setInt(1, student.getId());
            statement.execute();
            result = true;
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error while deleting student.");
        }

        return result;
    }
}