package SSRboard.comment.domain;

import SSRboard.board.domain.Board;
import SSRboard.config.entity.EntityStruct;
import SSRboard.member.domain.Member;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends EntityStruct {

    @Column(name = "CONTENT")
    private String content;

    // MEMBER_ID --> FK
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    // BOARD_ID --> FK
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public Comment(String content, Member member, Board board) {
        this.content = content;
        this.member = member;
        this.board = board;
    }

    // 댓글 수정 메서드
    public void updateComment(String content) {
        this.content = content;
    }

    public void setMember(Member member) {
        this.member = member;

        // 무한루프 방지
        if (!member.getCommentList().contains(this)) {
            member.getCommentList().add(this);
        }
    }

    public void setBoard(Board board) {
        this.board = board;

        // 무한루프 방지
        if (!board.getCommentList().contains(this)) {
            board.getCommentList().add(this);
        }
    }
}
