package com.study.jaehun.repository;

import com.study.jaehun.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByUrl(String url);

    @Query(value = "select min(id) from Candidate", nativeQuery=true)
    Long getMinId();

}
