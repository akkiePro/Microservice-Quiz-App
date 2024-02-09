package com.akki.quizservice.model;

import lombok.Data;

@Data
public class QuizDto {

    private String category;
    private Integer limit;
    private String title;

}
