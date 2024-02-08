package com.akki.questionservice.dao;

import com.akki.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "SELECT q.q_id FROM Question q WHERE q.category= :category ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Integer> getQuestionsForQuizByCategory(String category, Integer limit);
}
