//package com.laba.solvd.dao;
//
//import com.laba.solvd.model.Professor;
//import com.laba.solvd.model.University;
//import com.laba.solvd.utils.ConnectionPool;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class ProfessorRepo implements GenericDao<Professor>{
//    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
//    private static final Logger logger = LoggerFactory.getLogger(CourseRepo.class.getName());
//
//    private Professor mapProfessor(ResultSet rs) throws SQLException {
//        return new Professor(
//                rs.getLong("id"),
//                rs.getString("first_name"),
//                rs.getString("last_name"),
//                rs.getString("email"),
//                rs.getDouble("salary"),
//                rs.getLong("department_id"),
//                rs.getLong("university_id")
//        );
//    }
//}
