package com.akki.questionservice.service;


import com.akki.questionservice.dao.QuestionRepository;
import com.akki.questionservice.exception.NotFoundException;
import com.akki.questionservice.model.Question;
import com.akki.questionservice.model.QuestionWrapper;
import com.akki.questionservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repo;

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            Question savedQuestion = repo.save(question);
            return new ResponseEntity<>("saved record in DB.\n" + savedQuestion, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("failed persisting in DB", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Question>> fetchAllQuestions() {
        try{
            return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Question>> fetchAllQuestionsByCategory(String category) {
        try{
            return new ResponseEntity<>(repo.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> updateQuestion(Integer id, Question question) {
        try {
            Question existingQuestion = repo.findById(id).orElseThrow(() -> new NotFoundException("Question not found"));
            existingQuestion.setQuestionTitle(question.getQuestionTitle());
            existingQuestion.setOption1(question.getOption1());
            existingQuestion.setOption2(question.getOption2());
            existingQuestion.setOption3(question.getOption3());
            existingQuestion.setOption4(question.getOption4());
            existingQuestion.setCorrectAnswer(question.getCorrectAnswer());
            existingQuestion.setCategory(question.getCategory());
            existingQuestion.setDifficultyLevel(question.getDifficultyLevel());

            repo.save(existingQuestion);
            return new ResponseEntity<>("updated record in DB.\n" + existingQuestion, HttpStatus.OK);
        } catch (NotFoundException nfe) {
            return new ResponseEntity<>(nfe.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("failed persisting in DB", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            Question record = repo.findById(id).orElseThrow(() -> new NotFoundException("Question not found.\nqId= " + id + " not found."));
            repo.delete(record);
            return new ResponseEntity<>("record for qId: " + id + " removed.\n" + record, HttpStatus.OK);
        } catch (NotFoundException nfe) {
            return new ResponseEntity<>(nfe.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Integer>> getAskedQuestionIdsByCategory(String category, Integer limit) {
        try {
            List<Integer> questionsForQuizByCategory = repo.getQuestionsForQuizByCategory(category, limit);
            return new ResponseEntity<>(questionsForQuizByCategory, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(List<Integer> questionIds) {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        try {
            List<Question> questions = repo.findAllById(questionIds);
            questionWrappers = questions.stream().map(q -> new QuestionWrapper(q.getQId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4())).collect(Collectors.toList());
            return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(questionWrappers, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> getScore(List<Response> responses) {
        int score = 0;
        try {
            for (Response r : responses) {
                Question question = repo.findById(r.getRId()).orElseThrow(() -> new NotFoundException("Question not found.\nrId= " + r.getRId() + " not found."));
                if (r.getResponse().equals(question.getCorrectAnswer()))
                    score++;
            }
            return new ResponseEntity<>(String.valueOf(score), HttpStatus.OK);
        } catch (NotFoundException nfe) {
            return new ResponseEntity<>(nfe.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
