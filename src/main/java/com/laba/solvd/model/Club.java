package com.laba.solvd.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Club {
    private Long id;
    private String name;
    private Long presidentId;
}
