package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.DormitoryDao;
import com.laba.solvd.dao.impl.DormitoryRepo;
import com.laba.solvd.model.Dormitory;
import com.laba.solvd.model.Room;
import com.laba.solvd.service.IDormitoryService;
import com.laba.solvd.service.IRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DormitoryServiceImpl implements IDormitoryService {
    private static final Logger logger = LoggerFactory.getLogger(DormitoryServiceImpl.class);
    private final DormitoryDao dormitoryRepo;
    private final IRoomService roomService;

    public DormitoryServiceImpl(){
        this.dormitoryRepo = new DormitoryRepo();
        this.roomService = new RoomServiceImpl();
    }

    @Override
    public void create(Dormitory dormitory) {
        dormitoryRepo.create(dormitory);
    }

    @Override
    public void deleteById(Long id) {
        dormitoryRepo.deleteById(id);
    }

    @Override
    public Dormitory findById(Long id) throws InterruptedException {
        Dormitory dormitory = dormitoryRepo.findById(id);
        if (dormitory != null) {
            List<Room> rooms = roomService.findByDormitoryId(dormitory.getId());
            dormitory.setRooms(rooms);
        }
        return dormitory;
    }

    @Override
    public List<Dormitory> findAll() {
        return dormitoryRepo.findAll();
    }

    @Override
    public void update(Dormitory dormitory) {
        dormitoryRepo.update(dormitory);
    }

    @Override
    public List<Room> findRoomsByDormitoryId(Long dormitoryId) {
        return roomService.findByDormitoryId(dormitoryId);
    }
}
