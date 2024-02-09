package com.akki.quizservice.service;

import com.akki.quizservice.exception.NotFoundException;
import com.akki.quizservice.feign.QuestionInterface;
import com.akki.quizservice.model.QuestionWrapper;
import com.akki.quizservice.model.Quiz;
import com.akki.quizservice.model.Response;
import com.akki.quizservice.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository repo;
    
    @Autowired
    private QuestionInterface questionInterface;

    public ResponseEntity<String> getQuestionIds(String category, Integer limit, String title) {
        try {
            List<Integer> quizQuestionIds = questionInterface.getAskedQuestionIdsByCategory(category, limit).getBody();

            Quiz quiz = new Quiz();
            quiz.setQuizTitle(title);
            quiz.setQIds(quizQuestionIds);

            Quiz quizData = repo.save(quiz);

            return new ResponseEntity<>("success.\n" + quizData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(Integer quizId) {
        try {
            Quiz quiz = repo.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz not found by quizId = " + quizId));
            return questionInterface.getQuestionsByIds(quiz.getQIds());
        } catch (NotFoundException nfe) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> getScore(List<Response> responses) {
        try {
            return questionInterface.getScore(responses);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
