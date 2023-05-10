package SSRboard.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentUpdateDto {

    private Long commentId;

    @NotBlank(message = "댓글을 작성해주세요.")
    private String comment;

    @Builder
    public CommentUpdateDto(Long commentId, String comment) {
        this.commentId = commentId;
        this.comment = comment;
    }
}
