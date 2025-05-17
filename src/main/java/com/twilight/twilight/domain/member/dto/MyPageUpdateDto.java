package com.twilight.twilight.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageUpdateDto {

    @NotBlank(message = "성별은 비어 있을 수 없습니다.")
    private String gender;

    @NotNull(message = "나이는 비어 있을 수 없습니다.")
    private Integer age;

    @Size(max = 3, message = "나를 나타낼 수 있는 항목 3가지를 골라주세요.")
    private List<String> personalities;

    @Size(max = 3, message = "내가 관심있는 항목 3개 골라주세요.")
    private List<String> interests;

}
