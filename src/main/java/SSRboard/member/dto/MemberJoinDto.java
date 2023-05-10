package SSRboard.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinDto {

    @Email
    @NotBlank(message = "정확하게 입력해주세요.")
    private String memberId;

    @NotBlank(message = "정확하게 입력해주세요.")
    private String password;

    @Length(min = 1, max = 10)
    @NotBlank(message = "정확하게 입력해주세요.")
    private String nickName;

}
