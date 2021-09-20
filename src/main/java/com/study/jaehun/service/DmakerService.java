package com.study.jaehun.service;

import com.study.jaehun.dto.CreateDeveloper;
import com.study.jaehun.dto.DeveloperDetailDto;
import com.study.jaehun.dto.DeveloperDto;
import com.study.jaehun.dto.EditDeveloper;
import com.study.jaehun.entity.Developer;
import com.study.jaehun.entity.RetiredDeveloper;
import com.study.jaehun.exception.DMakerException;
import com.study.jaehun.repository.DeveloperRepository;
import com.study.jaehun.repository.RetiredDeveloperRepository;
import com.study.jaehun.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.study.jaehun.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor // 자동으로 Repository를 injection
public class DmakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId){
        return developerRepository.findByMemberId(memberId) // Entity 가져오기
                .map(DeveloperDetailDto::fromEntity) // Entity를 Dto로 매핑
                .orElseThrow(()-> new DMakerException(NO_DEVELOPER)); // 실패했다면 exception 발생
    }

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        // 요청받은 내용으로 developer 생성
        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        validateDeveloperLevel( request.getDeveloperLevel(), request.getExperienceYears());
        // java 11 기능 바로 확인된 memberId가 있다면 중복되었다고 Exception 발생시키기
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateDeveloperLevel( request.getDeveloperLevel(), request.getExperienceYears());
        Developer developer = developerRepository.findByMemberId(memberId) // Entity 가져오기
                .orElseThrow(()-> new DMakerException(NO_DEVELOPER)); // 실패했다면 exception 발생
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }


    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if (developerLevel == DeveloperLevel.JUNGNIOR &&
                (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(()-> new DMakerException(NO_DEVELOPER));
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        developerRepository.delete(developer);
        return DeveloperDetailDto.fromEntity(developer);
    }
}
