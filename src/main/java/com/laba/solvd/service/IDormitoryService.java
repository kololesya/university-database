package com.laba.solvd.service;

import com.laba.solvd.model.Dormitory;
import com.laba.solvd.model.Room;

import java.util.List;

public interface IDormitoryService {
    void create(Dormitory dormitory);
    void deleteById(Long id);
    Dormitory findById(Long id) throws InterruptedException;
    List<Dormitory> findAll();
    void update(Dormitory dormitory);

    List<Room> findRoomsByDormitoryId(Long dormitoryId);
}
