package com.twilight.twilight.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberRequestDto {

    @NotBlank(message = "이름은 비어 있을 수 없습니다.")
    private String memberName;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
    private String email;

    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "나이는 비어 있을 수 없습니다.")
    private Integer age;

    @NotBlank(message = "성별은 비어 있을 수 없습니다.")
    private String gender;

    @Size(max = 3, message = "나의 성격은 3개 골라주세요.")
    private List<String> personalities;

    @Size(max = 3, message = "나의 취미는 3개 골라주세요.")
    private List<String> interests;

}
