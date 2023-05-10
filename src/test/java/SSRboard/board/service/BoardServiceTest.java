package SSRboard.board.service;

import SSRboard.board.domain.Board;
import SSRboard.board.dto.BoardPatchDto;
import SSRboard.board.dto.BoardPostDto;
import SSRboard.board.repository.BoardRepository;
import SSRboard.member.domain.Member;
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
        Member newMember = Member.builder()
                .memberId("member@test.com")
                .password("test")
                .nickName("userA")
                .build();
        memberRepository.save(newMember);

        Member member = memberRepository.findById(1L).orElse(null);
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
        Member newMember = Member.builder()
                .memberId("member@test.com")
                .password("test")
                .nickName("userA")
                .build();
        memberRepository.save(newMember);

        Member member = memberRepository.findById(1L).orElse(null);
        BoardPostDto titleDto = new BoardPostDto(null, "content");
        BoardPostDto contentDto = new BoardPostDto("title", null);

        assertThrows(IllegalArgumentException.class, () -> boardService.createPost(titleDto, member));
        assertThrows(IllegalArgumentException.class, () -> boardService.createPost(contentDto, member));
    }

    @Test
    @DisplayName("게시글 단건 조회는 제대로 되는지?")
    void findPost() {
        Board newBoard = Board.builder()
                .title("title")
                .content("content")
                .build();
        boardRepository.save(newBoard);

        Board findBoard = boardService.findPost(1L);
        assertThat(findBoard.getTitle()).isEqualTo(newBoard.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(newBoard.getContent());
    }

    @Test
    @DisplayName("페이징 처리는 제대로 되는지? (한 페이지당 10개의 게시물)")
    void findAllPost() {
        Member newMember = Member.builder()
                .memberId("member@test.com")
                .password("test")
                .nickName("userA")
                .build();
        memberRepository.save(newMember);

        Member member = memberRepository.findById(1L).orElse(null);
        Board board = new Board("title", "content", member);

        for (int i = 0; i < 10; i++) {
            boardRepository.save(board);
        }

        Page<Board> boards = boardService.findAllPost(PageRequest.of(1, 10));
        assertThat(boards.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시글 수정은 제대로 되는지?")
    void updatePost() {
        Board newBoard = Board.builder()
                .title("title")
                .content("content")
                .build();
        boardRepository.save(newBoard);

        BoardPatchDto dto = new BoardPatchDto(1L, "updateTitle", "updateContent");
        boardService.updatePost(1L, dto);

        Board updatedBoard = boardService.findPost(1L);

        assertThat(updatedBoard.getTitle()).isEqualTo(dto.getTitle());
        assertThat(updatedBoard.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    @DisplayName("데이터를 제대로 넘기지 않았을때 게시글 수정처리가 잘되는지?")
    void checkUpdate() {
        Board newBoard = Board.builder()
                .title("title")
                .content("content")
                .build();
        boardRepository.save(newBoard);

        BoardPatchDto titleDto = new BoardPatchDto(1L, null, "updateContent");
        BoardPatchDto contentDto = new BoardPatchDto(1L, "updateTitle", null);
        BoardPatchDto allDto = new BoardPatchDto(1L, null, null);

        assertThrows(IllegalArgumentException.class, () -> boardService.updatePost(1L, titleDto));
        assertThrows(IllegalArgumentException.class, () -> boardService.updatePost(1L, contentDto));
        assertThrows(IllegalArgumentException.class, () -> boardService.updatePost(1L, allDto));
    }

    @Test
    @DisplayName("게시글 삭제는 제대로 되는지?")
    void delete() {
        Board newBoard = Board.builder()
                .title("title")
                .content("content")
                .build();
        boardRepository.save(newBoard);
        boardService.delete(1L);

        assertThat(boardRepository.count()).isEqualTo(0);
    }
}