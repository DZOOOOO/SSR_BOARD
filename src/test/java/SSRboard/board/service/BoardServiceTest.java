package SSRboard.board.service;

import SSRboard.board.domain.Board;
import SSRboard.board.dto.BoardPatchDto;
import SSRboard.board.dto.BoardPostDto;
import SSRboard.board.repository.BoardRepository;
import SSRboard.member.domain.Member;
import SSRboard.member.dto.MemberJoinDto;
import SSRboard.member.repository.MemberRepository;
import SSRboard.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("게시판 글이 제대로 작성이 되는지?")
    void createPost() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto dto = new BoardPostDto("title", "content");
        Board board = boardService.createPost(dto, member);
        assertThat(board.getTitle()).isEqualTo(dto.getTitle());
        assertThat(board.getContent()).isEqualTo(dto.getContent());
        assertThat(boardRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("비로그인시 게시글 작성처리가 제대로 되는지?")
    void noLoginCreatePost() {
        BoardPostDto dto = new BoardPostDto("title", "content");
        assertThrows(IllegalArgumentException.class, () -> boardService.createPost(dto, null));
    }

    @Test
    @DisplayName("데이터를 제대로 넘기지 않았을 경우에 게시글 작성처리가 제대로 되는지?")
    void checkCreatePost() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto titleDto = new BoardPostDto(null, "content");
        BoardPostDto contentDto = new BoardPostDto("title", null);

        assertThrows(IllegalArgumentException.class, () -> boardService.createPost(titleDto, member));
        assertThrows(IllegalArgumentException.class, () -> boardService.createPost(contentDto, member));
    }

    @Test
    @DisplayName("게시글 단건 조회는 제대로 되는지?")
    void findPost() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto boardPostDto = new BoardPostDto("title", "content");
        Board newBoard = boardService.createPost(boardPostDto, member);

        Board findBoard = boardService.findPost(newBoard.getId());
        assertThat(findBoard.getTitle()).isEqualTo(newBoard.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(newBoard.getContent());
    }

    @Test
    @DisplayName("페이징 처리는 제대로 되는지? (한 페이지당 10개의 게시물)")
    void findAllPost() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto boardPostDto = new BoardPostDto("title", "content");

        for (int i = 0; i < 10; i++) {
            boardService.createPost(boardPostDto, member);
        }

        Page<Board> boards = boardService.findAllPost(PageRequest.of(1, 10));
        assertThat(boards.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시글 수정은 제대로 되는지?")
    void updatePost() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto boardPostDto = new BoardPostDto("title", "content");
        Board newBoard = boardService.createPost(boardPostDto, member);

        BoardPatchDto dto = new BoardPatchDto(newBoard.getId(), "updateTitle", "updateContent");
        boardService.updatePost(newBoard.getId(), dto);

        Board updatedBoard = boardService.findPost(newBoard.getId());

        assertThat(updatedBoard.getTitle()).isEqualTo(dto.getTitle());
        assertThat(updatedBoard.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    @DisplayName("데이터를 제대로 넘기지 않았을때 게시글 수정처리가 잘되는지?")
    void checkUpdate() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto boardPostDto = new BoardPostDto("title", "content");
        Board newBoard = boardService.createPost(boardPostDto, member);

        BoardPatchDto titleDto = new BoardPatchDto(newBoard.getId(), null, "updateContent");
        BoardPatchDto contentDto = new BoardPatchDto(newBoard.getId(), "updateTitle", null);
        BoardPatchDto allDto = new BoardPatchDto(newBoard.getId(), null, null);

        assertThrows(IllegalArgumentException.class, () -> boardService.updatePost(newBoard.getId(), titleDto));
        assertThrows(IllegalArgumentException.class, () -> boardService.updatePost(newBoard.getId(), contentDto));
        assertThrows(IllegalArgumentException.class, () -> boardService.updatePost(newBoard.getId(), allDto));
    }

    @Test
    @DisplayName("게시글 삭제는 제대로 되는지?")
    void delete() {
        MemberJoinDto memberJoinDto = new MemberJoinDto("test@test.com", "test", "userA");
        memberService.joinMember(memberJoinDto);
        Member member = memberService.findMember(memberJoinDto.getMemberId());

        BoardPostDto boardPostDto = new BoardPostDto("title", "content");
        Board newBoard = boardService.createPost(boardPostDto, member);

        boardService.delete(newBoard.getId());

        assertThat(boardRepository.count()).isEqualTo(0);
    }
}