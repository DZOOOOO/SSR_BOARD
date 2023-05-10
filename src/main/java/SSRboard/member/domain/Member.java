package SSRboard.member.domain;

import SSRboard.board.domain.Board;
import SSRboard.comment.domain.Comment;
import SSRboard.config.entity.EntityStruct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends EntityStruct {

    // 이메일 형식
    @Email
    @Column(name = "MEMBER_ID", unique = true, nullable = false)
    private String memberId;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NICKNAME", nullable = false, length = 10)
    private String nickName;

    @OneToMany(mappedBy = "member")
    private List<Board> boardList;

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList;

    @Builder
    public Member(String memberId, String password, String nickName) {
        this.memberId = memberId;
        this.password = password;
        this.nickName = nickName;
    }

    public void addBoard(Board board) {
        this.boardList.add(board);

        // 무한루프 방지
        if (board.getMember() != this) {
            board.setMember(this);
        }
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);

        // 무한루프 방지
        if (comment.getMember() != this) {
            comment.setMember(this);
        }
    }
}
