package com.study.jaehun.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Service
public class WebPageService {
    private static String KOREA_COVID_DATAS_URL = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13";

    @Scheduled(cron = "10 * * * * *")
    public void getKoreaCovidDatas() throws IOException {

        Document doc = Jsoup.connect(KOREA_COVID_DATAS_URL).get();
        log.info(doc.toString());

    }
}
