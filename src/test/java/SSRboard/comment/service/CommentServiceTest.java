package SSRboard.comment.service;

import SSRboard.board.domain.Board;
import SSRboard.board.dto.BoardPostDto;
import SSRboard.board.repository.BoardRepository;
import SSRboard.board.service.BoardService;
import SSRboard.comment.domain.Comment;
import SSRboard.comment.repository.CommentRepository;
import SSRboard.member.domain.Member;
import SSRboard.member.dto.MemberJoinDto;
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
    MemberService memberService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        boardRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성이 제대로 되는가?")
    void commentWrite() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto dto = new BoardPostDto("title", "content");
        Board board = boardService.createPost(dto, member);

        commentService.commentWrite(board.getId(), member, "commentA");

        assertThat(commentRepository.count()).isEqualTo(1);
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(null, member, "commentA"));
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(board.getId(), null, "commentA"));
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(board.getId(), member, null));
        assertThrows(IllegalArgumentException.class, () -> commentService.commentWrite(null, null, null));
    }

    @Test
    @DisplayName("댓글 리스트는 제대로 가져오는가?")
    void findCommentList() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto dto = new BoardPostDto("title", "content");
        Board board = boardService.createPost(dto, member);

        for (int i = 0; i < 10; i++) commentService.commentWrite(board.getId(), member, "commentA" + i);
        List<Comment> commentList = commentService.findCommentList(board.getId());

        assertThat(commentRepository.count()).isEqualTo(10);
        assertThat(commentList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시물이 없는 경우 댓글도 모두 삭제가 되는가?")
    void caseCadeComment() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto dto = new BoardPostDto("title", "content");
        Board board = boardService.createPost(dto, member);

        // 댓글 작성
        commentService.commentWrite(board.getId(), member, "commentA");

        // 게시물 삭제
        boardService.delete(board.getId());

        assertThat(commentRepository.count()).isEqualTo(0);
    }


    @Test
    @DisplayName("댓글 삭제는 제대로 되는가?")
    void deleteComment() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto dto = new BoardPostDto("title", "content");
        Board board = boardService.createPost(dto, member);

        Comment newComment = commentService.commentWrite(board.getId(), member, "commentA");
        Comment findComment = commentService.findComment(newComment.getId(), member);
        commentService.deleteComment(findComment.getId());

        assertThat(commentRepository.count()).isEqualTo(0);
    }
}