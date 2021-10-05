package com.study.jaehun.service;


import com.study.jaehun.entity.WebPage;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
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



    public List<WebPage> searchWebpage(String keyword) {
        // 쿼리 준비 및 실행 workflow
        // 1. Entitymanager에서 SearchSession 가져오기
        SearchSession searchSession = Search.session( entityManager );
        // WebPage entity에 mapping된 index에서 search query를 시작
        SearchResult<WebPage> result = searchSession.search(WebPage.class)
                .where( f -> f.match()
                        .fields( "title", "description" )
                        .matching(keyword))
                .fetch( 20 );
        long totalHitCount = result.total().hitCount();

        // 2. Hibernate Query DSL을 통해 Lucene Query 생성
        // keyword() : 하나의 특정 단어를 찾고 있음을 지정
        // onField() : Lucene에게 찾을 위치를 알려줌
        // matching() : 무엇을 찾을지 알려줌
        // 3. Lucene Query를 Hibernate Query로 래핑
        // 4. Query 실행
        return result.hits();
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