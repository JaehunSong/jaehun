package com.study.jaehun.controller;

import com.study.jaehun.dto.CreateDeveloper;
import com.study.jaehun.dto.DeveloperDetailDto;
import com.study.jaehun.dto.DeveloperDto;
import com.study.jaehun.dto.EditDeveloper;
import com.study.jaehun.service.DmakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {
    private final DmakerService dmakerService;

    // 조회 메소드
    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dmakerService.getAllDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable String memberId
    ) {
        log.info("GET /developers HTTP/1.1");

        return dmakerService.getDeveloperDetail(memberId);
    }

    // 생성 메소드
    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid @RequestBody CreateDeveloper.Request request
            ) {
        log.info("request : {}",request);

        return dmakerService.createDeveloper(request);
    }

    // 수정 메소드
    @PutMapping("/developers/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
            // Json으로 request를 받아서 처리
    ) {
        log.info("PUT /developers HTTP/1.1");

        return dmakerService.editDeveloper(memberId, request);
    }

    // 삭제 메소드
    @DeleteMapping("/developers/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
            @PathVariable String memberId
    ) {
        log.info("PUT /developers HTTP/1.1");

        return dmakerService.deleteDeveloper(memberId);
    }

}

