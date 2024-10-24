package com.laba.solvd;

//import com.laba.solvd.dao.ProfessorRepo;
import com.laba.solvd.model.*;
import com.laba.solvd.service.*;
import com.laba.solvd.service.serviceImpl.*;


import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        IClubService clubService = new ClubServiceImpl();
        IScholarshipService scholarshipService = new ScholarshipServiceImpl();
        IRoomService roomService = new RoomServiceImpl();
        IDormitoryService dormitoryService = new DormitoryServiceImpl();
        IStudentService studentService = new StudentServiceImpl();
        //IStudentService studentService = new StudentServiceImpl();
//
//        clubService.create(new Club(null, "Bridge"));
//        clubService.create(new Club(null, "Soccer"));
//        clubService.create(new Club(null, "Tennis"));
//        clubService.create(new Club(null, "Curling"));
//        clubService.create(new Club(null, "Baseball"));
//
//        System.out.println(clubService.findAll());
//
//        clubService.deleteById(4L);

//        scholarshipService.create(new Scholarship(null, 1500.0, new Timestamp(System.currentTimeMillis())));
//        scholarshipService.create(new Scholarship(null, 800.0, new Timestamp(System.currentTimeMillis())));
//        scholarshipService.create(new Scholarship(null, 1200.0, new Timestamp(System.currentTimeMillis())));
//        scholarshipService.create(new Scholarship(null, 1100.0, new Timestamp(System.currentTimeMillis())));
//        scholarshipService.create(new Scholarship(null, 1000.0, new Timestamp(System.currentTimeMillis())));
//
        System.out.println(scholarshipService.findAll());

//        dormitoryService.create(new Dormitory( null, "Dormitory A", 100, "123 Main St", null));
//        dormitoryService.create(new Dormitory(null, "Dormitory B", 150, "456 Side St", null));
//        dormitoryService.create(new Dormitory(null, "Dormitory C", 200, "789 Elm St", null));
//        dormitoryService.create(new Dormitory(null, "Dormitory D", 120, "321 Oak St", null));
//        dormitoryService.create(new Dormitory(null, "Dormitory E", 90, "654 Pine St", null));
//
//        roomService.create(new Room(null, "101A", 1L));
//        roomService.create(new Room(null, "201B", 1L));
//        roomService.create(new Room(null, "301C", 1L));
//        roomService.create(new Room(null, "401D", 1L));

        System.out.println(roomService.findAll());

        List<Dormitory> dormitories = dormitoryService.findAll();
        Long dormitoryId = dormitories.getFirst().getId();
        List<Room> roomsForDormitory = dormitoryService.findRoomsByDormitoryId(dormitoryId);

//        System.out.println("Rooms in Dormitory A:");
//        for (Room room : roomsForDormitory) {
//            System.out.println("Room ID: " + room.getId() + ", Room Number: " + room.getRoomNumber());
//        }
        //roomService.deleteById(3L);
//
        studentService.create(new Student(null, 1L, "Alice", "Johnson",
                "alice.johnson@example.com", LocalDateTime.now()));

        List<Student> students = studentService.findAll();
        System.out.println("All Students:");
        for (Student student : students) {
            System.out.println(student);
        }

        //Student student = studentService.findById(2L);

        //Scholarship scholarship = scholarshipService.findById(7l);
        studentService.getStudentWithClubInfo(2L);
        studentService.getStudentWithScholarshipInfo(2L);
//        studentService.assignScholarshipToStudent(student.getId(), scholarship.getId());
        //System.out.println(studentService.findById(2L));

        //System.out.println(studentService.findById(3L));
//
//        Student studentToUpdate = studentService.findById(3L);
//        studentToUpdate.setFirstName("UpdatedFirstName");
//        studentToUpdate.setLastName("UpdatedLastName");
//        studentToUpdate.setEmail("updated.email@example.com");
//        studentToUpdate.setUniversityId(2L);
//
//        studentService.update(studentToUpdate);
//
//        System.out.println(connectionPool.getActiveConnectionsCount());
    }
}