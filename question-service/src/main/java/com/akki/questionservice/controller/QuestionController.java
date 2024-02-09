package com.akki.questionservice.controller;

import com.akki.questionservice.model.Question;
import com.akki.questionservice.model.QuestionWrapper;
import com.akki.questionservice.model.Response;
import com.akki.questionservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("question")
@RestController
public class QuestionController {

    @Autowired
    private QuestionService service;

    @Autowired
    private Environment environment;

    @PostMapping("addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return service.addQuestion(question);
    }

    @GetMapping("getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return service.fetchAllQuestions();
    }

    @GetMapping("category/{cat}")
    public ResponseEntity<List<Question>> getAllQuestionsByCategory(@PathVariable("cat") String category) {
        return service.fetchAllQuestionsByCategory(category);
    }

    @PutMapping("update/{qId}")
    public ResponseEntity<String> updateQuestion(@PathVariable("qId") Integer id, @RequestBody Question question) {
        return service.updateQuestion(id, question);
    }

    @DeleteMapping("delete/{qId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("qId") Integer id) {
        return service.deleteQuestion(id);
    }

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getAskedQuestionIdsByCategory(@RequestParam String category, @RequestParam Integer limit) {
        return service.getAskedQuestionIdsByCategory(category, limit);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(@RequestBody List<Integer> questionIds) {
        System.out.println(environment.getProperty("local.server.port"));   // to view port number if multiple instances of any service are running.
        return service.getQuestionsByIds(questionIds);
    }

    @PostMapping("getScore")
    public ResponseEntity<String> getScore(@RequestBody List<Response> responses) {
        return service.getScore(responses);
    }

}
