package com.laba.solvd.dao;

import com.laba.solvd.model.Scholarship;

public interface ScholarshipDao extends GenericDao<Scholarship> {
    Scholarship findByStudentId(Long studentId);
}
