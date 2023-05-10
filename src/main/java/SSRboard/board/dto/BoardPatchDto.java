package SSRboard.board.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPatchDto {

    private Long boardId;

    @NotBlank(message = "공백없이 입력해주세요.")
    private String title;

    @NotBlank(message = "공백없이 입력해주세요.")
    private String content;

}
