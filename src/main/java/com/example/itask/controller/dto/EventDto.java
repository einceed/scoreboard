package com.example.itask.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ScoreDto> scores;

    public EventDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
