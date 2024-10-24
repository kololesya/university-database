package com.laba.solvd.dao;

import com.laba.solvd.model.Room;

import java.util.List;

public interface RoomDao extends GenericDao<Room>{
    List<Room> findByDormitoryId(Long dormitoryId);
}
