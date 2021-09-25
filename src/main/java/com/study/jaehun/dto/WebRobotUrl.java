package com.study.jaehun.dto;

import com.study.jaehun.entity.WebPage;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class WebRobotUrl {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Request{
        @NotNull
        private String url;
        @NotNull
        private String description;
        @NotNull
        private String title;
        @NotNull
        private Integer mcnt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String url;
        private String title;
        private String description;
        private Integer mcnt;
        public static WebRobotUrl.Response fromEntity(WebPage webPage){
            return Response.builder()
                    .url(webPage.getUrl())
                    .title(webPage.getTitle())
                    .description(webPage.getDescription())
                    .mcnt(webPage.getMcnt())
                    .build();
        }
    }
}
