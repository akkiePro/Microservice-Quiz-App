package com.akki.quizservice.controller;

import com.akki.quizservice.model.QuestionWrapper;
import com.akki.quizservice.model.QuizDto;
import com.akki.quizservice.model.Response;
import com.akki.quizservice.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    private QuizService service;

    @PostMapping("generate")
    public ResponseEntity<String> generateQuestionIdsForQuizQuestions(@RequestBody QuizDto dto) {
        return service.getQuestionIds(dto.getCategory(), dto.getLimit(), dto.getTitle());
    }

    @GetMapping("getQuiz/{quizId}")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(@PathVariable Integer quizId) {
        return service.getQuestionsByIds(quizId);
    }

    @GetMapping("getScore")
    public ResponseEntity<String> getScore(@RequestBody List<Response> responses){
        return service.getScore(responses);
    }

}
