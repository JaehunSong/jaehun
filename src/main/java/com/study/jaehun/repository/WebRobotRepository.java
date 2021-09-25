package com.study.jaehun.repository;

import com.study.jaehun.entity.Candidate;
import com.study.jaehun.entity.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebRobotRepository extends JpaRepository<WebPage, Long> {
    Optional<WebPage> findByUrl(String url);
}
