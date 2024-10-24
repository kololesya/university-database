//package com.laba.solvd.dao.impl;
//
//import com.laba.solvd.dao.CourseRepo;
//import com.laba.solvd.dao.GenericDao;
//import com.laba.solvd.dao.UniversityDao;
//import com.laba.solvd.model.Department;
//import com.laba.solvd.model.Student;
//import com.laba.solvd.model.University;
//import com.laba.solvd.utils.ConnectionPool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class UniversityRepo implements UniversityDao {
//    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
//    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
//
//    private University mapUniversity(ResultSet rs) throws SQLException {
//        return new University(
//                rs.getLong("id"),
//                rs.getString("name"),
//                rs.getString("location"),
//                rs.getInt("founded_year"),
//                new ArrayList<>(),
//                new ArrayList<>()
//        );
//    }
//
//    @Override
//    public void create(University university) {
//        String query = "INSERT INTO universities (name, location, founded_year) VALUES (?, ?, ?)";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//
//            stmt.setString(1, university.getName());
//            stmt.setString(2, university.getLocation());
//            stmt.setInt(3, university.getFoundedYear());
//
//            stmt.executeUpdate();
//
//            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
//                if (resultSet.next()) {
//                    university.setId(resultSet.getLong(1));
//                }
//            }
//
//        } catch (SQLException e) {
//            logger.error("Error creating university: {}", university, e);
//        }
//    }
//
//    @Override
//    public University findById(Long id) {
//        String query = "SELECT * FROM universities WHERE id = ?";
//        University university = null;
//
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                university = mapUniversity(rs); // Получаем основные данные университета
//                university.setDepartments(getDepartmentsForUniversity(id, connection)); // Получаем департаменты
//                university.setStudents(getStudentsForUniversity(id, connection)); // Получаем студентов
//            }
//        } catch (SQLException e) {
//            logger.error("Error finding university with id: {}", id, e);
//        }
//        return university;
//    }
//
//    @Override
//    public List<University> findAll() {
//        List<University> universities = new ArrayList<>();
//        String query = "SELECT u.*, d.id AS department_id, d.name AS department_name, " +
//                "s.id AS student_id, s.first_name AS student_first_name, " +
//                "s.last_name AS student_last_name, s.email AS student_email " +
//                "FROM universities u " +
//                "LEFT JOIN departments d ON u.id = d.university_id " +
//                "LEFT JOIN students s ON u.id = s.university_id";
//        try (Connection connection = connectionPool.getConnection();
//             Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            while (rs.next()) {
//                Long universityId = rs.getLong("id");
//                University university = mapUniversity(rs); // Получаем основные данные университета
//
//                // Проверяем, если департамент или студент уже существует в списках
//                if (rs.getLong("department_id") != 0) {
//                    Department department = new Department(
//                            rs.getLong("department_id"),
//                            rs.getString("department_name"),
//                            universityId // Указываем ID университета
//                    );
//                    university.getDepartments().add(department);
//                }
//
//                if (rs.getLong("student_id") != 0) {
//                    Student student = new Student(
//                            rs.getLong("student_id"),
//                            rs.getString("student_first_name"),
//                            rs.getString("student_last_name"),
//                            rs.getString("student_email"),
//                            null, // Здесь можно установить дату зачисления, если нужно
//                            universityId // Указываем ID университета
//                    );
//                    university.getStudents().add(student);
//                }
//
//                // Проверяем, есть ли уже университет в списке
//                if (!universities.contains(university)) {
//                    universities.add(university);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error("Error retrieving all universities", e);
//        }
//        return universities;
//    }
//
//    @Override
//    public void update(University university) {
//        String query = "UPDATE universities SET name = ?, location = ?, founded_year = ? WHERE id = ?";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, university.getName());
//            stmt.setString(2, university.getLocation());
//            stmt.setInt(3, university.getFoundedYear());
//            stmt.setLong(4, university.getId());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error updating university: {}", university, e);
//        }
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        String query = "DELETE FROM universities WHERE id = ?";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, id);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error deleting university with id: {}", id, e);
//        }
//    }
//}
