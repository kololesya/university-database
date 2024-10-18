package com.laba.solvd.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Club {
    private Long id;
    private String name;
    private Long presidentId;
    private List<Student> students;
}
