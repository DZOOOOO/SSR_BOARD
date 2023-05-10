package SSRboard.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDto {

    @NotBlank(message = "이메일 형식에 맞게 작성해주세요.")
    @Email(message = "이메일 형식에 맞게 작성해주세요.")
    private String memberId;

    @NotBlank(message = "정확하게 입력해주세요.")
    private String password;
}
