package com.study.jaehun.dto;

import com.study.jaehun.entity.Candidate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDto {
    private String url;
    private int depth;

    public static CandidateDto fromEntity(Candidate candidate){
        return CandidateDto.builder()
                .url(candidate.getUrl())
                .depth(candidate.getDepth())
                .build();
    }
}

