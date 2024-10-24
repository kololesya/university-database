package com.laba.solvd.service.serviceImpl;

import com.laba.solvd.dao.ClubDao;
import com.laba.solvd.dao.impl.ClubRepo;
import com.laba.solvd.model.Club;
import com.laba.solvd.service.IClubService;

import java.util.List;

public class ClubServiceImpl implements IClubService {
    private final ClubDao clubRepo;

    public ClubServiceImpl() {
        this.clubRepo = new ClubRepo();
    }

    @Override
    public void create(Club club) {
        clubRepo.create(club);
    }

    @Override
    public void deleteById(Long id) {
        clubRepo.deleteById(id);
    }

    @Override
    public Club findById(Long id) throws InterruptedException {
        return clubRepo.findById(id);
    }

    @Override
    public List<Club> findAll() {
        return clubRepo.findAll();
    }

    @Override
    public void update(Club club) {
        clubRepo.update(club);
    }
}
