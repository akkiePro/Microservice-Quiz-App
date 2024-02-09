package com.akki.quizservice.feign;

import com.akki.quizservice.model.QuestionWrapper;
import com.akki.quizservice.model.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("QUESTION-SERVICE")
public interface QuestionInterface {

    @GetMapping("question/generate")
    public ResponseEntity<List<Integer>> getAskedQuestionIdsByCategory(@RequestParam String category, @RequestParam Integer limit);

    @PostMapping("question/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(@RequestBody List<Integer> questionIds);

    @PostMapping("question/getScore")
    public ResponseEntity<String> getScore(@RequestBody List<Response> responses);

}
