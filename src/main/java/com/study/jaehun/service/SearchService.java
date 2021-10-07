package com.study.jaehun.service;


import com.study.jaehun.entity.WebPage;
import lombok.extern.slf4j.Slf4j;

import org.apache.lucene.search.Explanation;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchQuery;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;



    public List<WebPage> searchWebpage(String keyword) {
        // 쿼리 준비 및 실행 workflow
        // 1. Entitymanager에서 SearchSession 가져오기
        SearchSession searchSession = Search.session( entityManager );
        // WebPage entity에 mapping된 index에서 search query를 시작
        SearchResult<WebPage> result = searchSession.search(WebPage.class)
                .where( f -> f.match()
                        .fields( "title", "description" )
                        .matching(keyword)
                        .fuzzy(1))
                .fetch( 20 );
        long totalHitCount = result.total().hitCount();

        log.info(String.valueOf(totalHitCount));

        return result.hits();
    }

    public void searchWebpageTest(String keyword) {
        List<WebPage> webPages = searchWebpage(keyword);
        log.info("객체 크기 : "+ webPages.size());
        for(WebPage webPage : webPages) {
            log.info("\n제목 : {}\n가격 : {}\n---------------------------------\n"
                    , webPage.getTitle()
                    , webPage.getPrice());
        }
        log.info("==========================   종료   =========================");
    }
}