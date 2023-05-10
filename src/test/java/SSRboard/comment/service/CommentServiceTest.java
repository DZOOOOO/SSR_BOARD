package SSRboard.comment.service;

import SSRboard.board.domain.Board;
import SSRboard.board.repository.BoardRepository;
import SSRboard.board.service.BoardService;
import SSRboard.comment.domain.Comment;
import SSRboard.comment.repository.CommentRepository;
import SSRboard.member.domain.Member;
import SSRboard.member.repository.MemberRepository;
import SSRboard.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        memberRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성이 제대로 되는가?")
    void commentWrite() {
        Member member = Member.builder()
                .memberId("test@test.com")
                .password("test")
                .nickName("userA")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();
        boardRepository.save(board);

        commentService.commentWrite(1L, member, "commentA");

        assertThat(commentRepository.count()).isEqualTo(1);
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(null, member, "commentA"));
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(1L, null, "commentA"));
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(1L, member, null));
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(null, null, null));
    }

    @Test
    @DisplayName("댓글 리스트는 제대로 가져오는가?")
    void findCommentList() {
        Member member = Member.builder()
                .memberId("test@test.com")
                .password("test")
                .nickName("userA")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();
        boardRepository.save(board);

        for (int i = 0; i < 10; i++) commentService.commentWrite(1L, member, "commentA" + i);
        List<Comment> commentList = commentService.findCommentList(1L);

        assertThat(commentRepository.count()).isEqualTo(10);
        assertThat(commentList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시물이 없는 경우 댓글도 모두 삭제가 되는가?")
    void caseCadeComment() {
        Member member = Member.builder()
                .memberId("test@test.com")
                .password("test")
                .nickName("userA")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();
        boardRepository.save(board);

        // 댓글 작성
        commentService.commentWrite(1L, member, "commentA");

        // 게시물 삭제
        boardService.delete(1L);

        assertThat(commentRepository.count()).isEqualTo(0);
    }


    @Test
    @DisplayName("댓글 삭제는 제대로 되는가?")
    void deleteComment() {
        Member member = Member.builder()
                .memberId("test@test.com")
                .password("test")
                .nickName("userA")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();
        boardRepository.save(board);

        commentService.commentWrite(1L, member, "commentA");
        commentService.deleteComment(1L);

        assertThat(commentRepository.count()).isEqualTo(0);
    }
}