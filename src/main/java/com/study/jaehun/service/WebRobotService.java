package com.study.jaehun.service;

import com.study.jaehun.dto.CandidateDto;
import com.study.jaehun.dto.DeveloperDetailDto;
import com.study.jaehun.dto.WebRobotUrl;
import com.study.jaehun.entity.Candidate;
import com.study.jaehun.entity.WebPage;
import com.study.jaehun.exception.DMakerException;
import com.study.jaehun.repository.CandidateRepository;
import com.study.jaehun.repository.WebRobotRepository;
import com.study.jaehun.webrobot.EHHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static com.study.jaehun.exception.DMakerErrorCode.NO_DEVELOPER;

@Service
@Slf4j
@RequiredArgsConstructor // 자동으로 Repository를 injection
public class WebRobotService {
    private final WebRobotRepository webRobotRepository;
    private final CandidateRepository candidateRepository;
    @Async
    @Transactional
    @Scheduled(fixedRateString = "10000", initialDelay = 10000)
    public WebRobotUrl.Response getUrlDetail() throws IOException {
        CandidateDto candidateDto = getCandidate();
        int depth = candidateDto.getDepth();
        String url = candidateDto.getUrl();
        Document doc = getDoc(url);
        String title = doc.title();
        Elements atags = doc.select("a[href]");
        ArrayList<String> links = getLinks(atags);
        for (String link : links) {
            Candidate candi = candidateRepository.findByUrl(link).orElse(null);
            if (candi == null){
                AddCandidate(link,depth+1);
            }
        }
        if (webRobotRepository.findByUrl(url).orElse(null) == null){
            WebPage webPage = WebPage.builder()
                    .url(url)
                    .title(EHHelper.EmitTagAndSpacialCh(title))
                    .description(EHHelper.EmitTagAndSpacialCh(doc.text()))
                    .mcnt(0)
                    .build();
            webRobotRepository.save(webPage);
            return WebRobotUrl.Response.fromEntity(webPage);
        } else{
            getUrlDetail();
        }
        return null;
    }


    @Transactional
    public void AddCandidate(String url, int depth){
        Candidate candidate = Candidate.builder()
                .url(url)
                .depth(depth)
                .build();
        candidateRepository.save(candidate);
    }


    public void removeCandidate(String url) throws IOException {
        Candidate candidate = candidateRepository.findByUrl(url)
                .orElseThrow(()-> new IOException());
        candidateRepository.delete(candidate);
    }

    @Transactional
    public CandidateDto getCandidate() throws IOException {
        String url = null;
        Candidate candidate = candidateRepository.findById(candidateRepository.getMinId()).orElse(null);
        if (candidate != null){
            url = candidate.getUrl();
            System.out.println("캔디데이트 가져오기 완료");
        }
        CandidateDto candidateDto =  candidateRepository.findByUrl(url) // Entity 가져오기
                .map(CandidateDto::fromEntity) // Entity를 Dto로 매핑
                .orElseThrow(()-> new IOException()); // 실패했다면 exception 발생
        candidateRepository.delete(candidate);
        System.out.println("캔디데이트 삭제 완료");
        return  candidateDto;
    }

    public static Document getDoc(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        return doc;

    }


    public ArrayList<String> getLinks(Elements atags){
        ArrayList<String> link = new ArrayList<String>();

        for (Element atag : atags
             ) {
            if (!atag.hasAttr("download")) {
                String atag2;
                try {
                    atag2 = atag.attr("abs:href");
                } catch (Exception e) {
                    continue;
                }
                link.add(ExtractionUrl(atag2));

            }
        }

        return link;
    }

    public String ExtractionUrl(String url) {

        int num = url.indexOf("#");
        if (num != -1) {
            url = url.substring(0, num);
        }
        num = url.indexOf("?");
        if (num != -1) {
            url = url.substring(0, num);
        }
        return url;
    }

}
