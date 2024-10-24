package com.laba.solvd.service;

import com.laba.solvd.model.Club;

import java.util.List;

public interface IClubService {
    void create(Club club);
    void deleteById(Long id);
    Club findById(Long id) throws InterruptedException;
    List<Club> findAll();
    void update(Club club);
}
