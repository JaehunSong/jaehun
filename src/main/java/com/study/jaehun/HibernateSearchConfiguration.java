package com.study.jaehun;

import com.study.jaehun.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@RequiredArgsConstructor
public class HibernateSearchConfiguration{
        @PersistenceContext(type = PersistenceContextType.EXTENDED)
        private EntityManager entityManager;

        @Bean
        SearchService searchService() throws InterruptedException {
            SearchService searchService = new SearchService(entityManager);
            searchService.buildSearchIndex();
            return searchService;
        }
}
