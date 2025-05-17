package com.twilight.twilight.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageMemberInfoDto {

    private Long id;

    private Integer age;

    private String gender;

    private List<String> memberPersonalityList;

    private List<String> memberInterestList;

}
