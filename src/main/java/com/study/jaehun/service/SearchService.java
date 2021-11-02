package com.study.jaehun.service;


import com.study.jaehun.entity.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
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

    public SearchService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }
    public void buildSearchIndex() throws InterruptedException {
        SearchSession searchSession = Search.session( entityManager );;
        searchSession.massIndexer()
                .start()
                .thenRun(() -> {
                    log.info( "Mass indexing succeeded!" );
                })
                .exceptionally( throwable -> {
                    log.error( "Mass indexing failed!", throwable );
                    return null;
                } );
    }

    public List<WebPage> searchWebpage(String keyword) {
        // 쿼리 준비 및 실행 workflow
        // 1. Entitymanager에서 SearchSession 가져오기
        SearchSession searchSession = Search.session( entityManager );
        // WebPage entity에 mapping된 index에서 search query를 시작
        SearchResult<WebPage> result = searchSession.search(WebPage.class)
                .where( f -> f.bool()
                        .filter(f.exists().field( "brand" ))
                        .must(f.match()
                                .field("brand").boost(2f)
                                .fields( "title", "description" )
                                .matching(keyword)
                                .fuzzy(1)))
                .sort(SearchSortFactory::score)
                .fetch( 20);
        long totalHitCount = result.total().hitCount();

        log.info("히트 카운트"+totalHitCount);
        log.info("result : "+result.hits().get(0).getDescription());
        return result.hits();
    }

    public void searchWebpageTest(String keyword) {
        List<WebPage> webPages = searchWebpage(keyword);
        log.info("객체 크기 : "+ webPages.size());
        for(WebPage webPage : webPages) {
            log.info("\n제목 : {}\n가격 : {}\n이미지링크 : {}\n설명 : {}\n상세 : {}\n브랜드 : {}\n---------------------------------\n"
                    , webPage.getTitle()
                    , webPage.getPrice()
                    , webPage.getImg()
                    , webPage.getDescription()
                    , webPage.getDetail()
                    , webPage.getBrand());
        }
        log.info("==========================   종료   =========================");
    }
}