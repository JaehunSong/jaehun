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
    @GetMapping("/test")
    public String test() throws IOException {
        return webRobotService.getPrice("https://tres-bien.com/dries-van-noten-heyze-pr-printed-top-black-212-021199-3221-900-fw21");
    }
}