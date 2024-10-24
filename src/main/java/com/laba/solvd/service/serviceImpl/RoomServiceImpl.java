package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.RoomDao;
import com.laba.solvd.dao.impl.RoomRepo;
import com.laba.solvd.model.Room;
import com.laba.solvd.service.IRoomService;

import java.util.List;

public class RoomServiceImpl implements IRoomService {
    private final RoomDao roomRepo;

    public RoomServiceImpl(){
        this.roomRepo = new RoomRepo();
    }

    @Override
    public void create(Room room) {
        roomRepo.create(room);
    }

    @Override
    public void deleteById(Long id) {
        roomRepo.deleteById(id);
    }

    @Override
    public Room findById(Long id) throws InterruptedException {
        return roomRepo.findById(id);
    }

    @Override
    public List<Room> findAll() {
        return roomRepo.findAll();
    }

    @Override
    public void update(Room room) {
        roomRepo.update(room);
    }

    @Override
    public List<Room> findByDormitoryId(Long dormitoryId) {
        return roomRepo.findByDormitoryId(dormitoryId);
    }
}
