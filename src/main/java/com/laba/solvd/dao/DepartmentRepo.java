//package com.laba.solvd.dao;
//
//import com.laba.solvd.model.Department;
//import com.laba.solvd.utils.ConnectionPool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DepartmentRepo implements GenericDao<Department>{
//    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
//    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
//
//    private Department mapDepartment(ResultSet rs) throws SQLException {
//        return new Department(
//                rs.getLong("id"),
//                rs.getString("name"),
//                rs.getLong("university_id")
//        );
//    }
//
//    @Override
//    public void create(Department department) {
//        String query = "INSERT INTO departments (name, university_id) VALUES (?, ?)";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//
//            stmt.setString(1, department.getName());
//            stmt.setLong(2, department.getUniversityId());
//
//            stmt.executeUpdate();
//
//            ResultSet resultSet = stmt.getGeneratedKeys();
//            if (resultSet.next()) {
//                department.setId(resultSet.getLong(1));
//            }
//        } catch (SQLException e) {
//            logger.error("Error creating course: " + department, e);
//        }
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        String query = "DELETE FROM departments WHERE id = ?";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, id);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error deleting department with ID: " + id, e);
//        }
//    }
//
//    @Override
//    public Department findById(Long id) {
//        String query = "SELECT * FROM departments WHERE id = ?";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return mapDepartment(rs);
//            }
//        } catch (SQLException e) {
//            logger.error("Error finding department with ID: " + id, e);
//        }
//        return null;
//    }
//
//    @Override
//    public List<Department> findAll() {
//        String query = "SELECT * FROM departments";
//        List<Department> departments = new ArrayList<>();
//        try (Connection connection = connectionPool.getConnection();
//             Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            while (rs.next()) {
//                departments.add(mapDepartment(rs));
//            }
//        } catch (SQLException e) {
//            logger.error("Error retrieving all departments", e);
//        }
//        return departments;
//    }
//
//    @Override
//    public void update(Department department) {
//        String query = "UPDATE courses SET name = ?, university_id = ? WHERE id = ?";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, department.getName());
//            stmt.setLong(2, department.getUniversityId());
//            stmt.setLong(3, department.getId());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error("Error updating course: " + department, e);
//        }
//    }
//}
