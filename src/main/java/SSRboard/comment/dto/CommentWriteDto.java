package SSRboard.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentWriteDto {

    @NotBlank(message = "댓글을 작성해주세요.")
    private String comment;

}
