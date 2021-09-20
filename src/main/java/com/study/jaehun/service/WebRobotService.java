package com.study.jaehun.service;

import com.study.jaehun.dto.WebRobotUrl;
import com.study.jaehun.entity.WebPage;
import com.study.jaehun.repository.WebRobotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor // 자동으로 Repository를 injection
public class WebRobotService {
    private final WebRobotRepository webRobotRepository;

    @Transactional
    public WebRobotUrl.Response getUrlDetail(WebRobotUrl.Request request) throws IOException {
        String test1 = getDescription(request.getUrl());
        WebPage webPage = WebPage.builder()
                .url(request.getUrl())
                .description(test1)
                .build();
        webRobotRepository.save(webPage);
        return WebRobotUrl.Response.fromEntity(webPage);
    }

    public static String getDescription(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        return doc.toString();

    }
}
