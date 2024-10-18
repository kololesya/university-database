//package com.laba.solvd.dao;
//
//import com.laba.solvd.model.University;
//import com.laba.solvd.utils.ConnectionPool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class UniversityRepo implements GenericDao<University>{
//    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
//    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
//
//    private University mapUniversity(ResultSet rs) throws SQLException {
//        return new University(
//                rs.getLong("id"),
//                rs.getString("name"),
//                rs.getString("location"),
//                rs.getInt("founded_year")
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
//            ResultSet resultSet = stmt.getGeneratedKeys();
//            if (resultSet.next()) {
//                university.setId(resultSet.getLong(1));
//            }
//        } catch (SQLException e) {
//            logger.error("Error creating course: " + university, e);
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
//            logger.error("Error deleting university with ID: " + id, e);
//        }
//    }
//
//    @Override
//    public University findById(Long id) {
//        String query = "SELECT * FROM universities WHERE id = ?";
//        try (Connection connection = connectionPool.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return mapUniversity(rs);
//            }
//        } catch (SQLException e) {
//            logger.error("Error finding university with ID: " + id, e);
//        }
//        return null;
//    }
//
//    @Override
//    public List<University> findAll() {
//        String query = "SELECT * FROM universities";
//        List<University> universities = new ArrayList<>();
//        try (Connection connection = connectionPool.getConnection();
//             Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            while (rs.next()) {
//                universities.add(mapUniversity(rs));
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
//            logger.error("Error updating university: " + university, e);
//        }
//    }
//}
