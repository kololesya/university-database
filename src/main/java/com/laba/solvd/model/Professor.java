package com.laba.solvd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Double salary;
    private Long departmentId;
    private Long universityId;
}
