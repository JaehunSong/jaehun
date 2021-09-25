package com.study.jaehun.controller;

import com.study.jaehun.dto.CreateDeveloper;
import com.study.jaehun.dto.WebRobotUrl;
import com.study.jaehun.service.WebRobotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebRobotController {
    private final WebRobotService webRobotService;

    // 생성 메소드
    @GetMapping("/testsearch")
    public WebRobotUrl.Response urlTest() throws IOException {
        return webRobotService.getUrlDetail();
    }

    @GetMapping("/addseedsite")
    public void Addsite(){
        webRobotService.AddCandidate("https://tres-bien.com/",0);
    }

}