package com.laba.solvd.service;

import com.laba.solvd.model.Room;

import java.util.List;

public interface IRoomService {
    void create(Room room);
    void deleteById(Long id);
    Room findById(Long id) throws InterruptedException;
    List<Room> findAll();
    void update(Room room);
    List<Room> findByDormitoryId(Long dormitoryId);
}
