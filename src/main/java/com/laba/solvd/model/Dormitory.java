package com.laba.solvd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dormitory {
    private Long id;
    private String name;
    private Integer capacity;
    private String address;
    private List<Room> rooms;
}
