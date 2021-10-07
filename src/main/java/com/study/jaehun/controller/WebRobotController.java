package com.study.jaehun.controller;

import com.study.jaehun.ApiTranslator;
import com.study.jaehun.service.SearchService;
import com.study.jaehun.service.WebRobotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.regex.PatternSyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebRobotController {
    private final WebRobotService webRobotService;
    private final SearchService searchService;
    @GetMapping("/test/{keyword}")
    public void test(@PathVariable String keyword) throws IOException {
        try {
            if(keyword.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
                // 한글이 포함된 문자열
                // 파파고 api로 json 형식으로 결과 반환
                ApiTranslator translator = new ApiTranslator(keyword);
                String trans = translator.getResult();

                // Json 형식 데이터에서 원하는 값을 가져옴
                JSONObject jObject = new JSONObject(trans).getJSONObject("message").getJSONObject("result");
                String result = jObject.getString("translatedText");

                // 특수문자가 섞어있는지 확인하여 있다면 치환
                result = StringReplace(result);
                log.info(result);
                searchService.searchWebpageTest(result);
            } else {
                // 한글이 포함되지 않은 문자열
                searchService.searchWebpageTest(keyword);
            }
        } catch (PatternSyntaxException e) {
            // 정규식에 오류가 있는 경우에 대한 처리
            System.err.println("An Exception Occured");
            e.printStackTrace();
        }

    }

    public static String StringReplace(String str){
        String match = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
        str = str.replaceAll(match, "");
        return str;
    }
}