package com.study.jaehun.service;


import com.study.jaehun.entity.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import org.apache.lucene.search.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;


    public void buildSearchIndex() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }

    public List<WebPage> searchWebpage(String keyword) {
        // 쿼리 준비 및 실행 workflow
        // 1. JPA FullTextEntityManager, QueryBuilder 가져오기
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(WebPage.class)
                .get();
        // 2. Hibernate Query DSL을 통해 Lucene Query 생성
        // keyword() : 하나의 특정 단어를 찾고 있음을 지정
        // onField() : Lucene에게 찾을 위치를 알려줌
        // matching() : 무엇을 찾을지 알려줌
        Query query = queryBuilder.keyword()
                .onFields("title", "description")
                .matching(keyword)
                .createQuery();
        // 3. Lucene Query를 Hibernate Query로 래핑
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, WebPage.class);
        // 4. Query 실행
        return (List<WebPage>) fullTextQuery.getResultList();
    }

    public void searchWebpageTest(String keyword) {
        List<WebPage> webPages = searchWebpage(keyword);
        for(WebPage webPage : webPages) {
            log.info("\n제목 : {}\n가격 : {}\n---------------------------------\n"
                    , webPage.getTitle()
                    , webPage.getPrice());
        }
    }
}