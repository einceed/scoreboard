package com.example.itask.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class ScoreDto {
    private String score;
    private Integer minute;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long eventId;
}
