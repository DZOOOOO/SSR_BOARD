package SSRboard.board.service;

import SSRboard.board.domain.Board;
import SSRboard.board.dto.BoardPatchDto;
import SSRboard.board.dto.BoardPostDto;
import SSRboard.board.repository.BoardRepository;
import SSRboard.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 생성
    public Board createPost(BoardPostDto dto, Member writeMember) {
        if (writeMember == null) throw new IllegalArgumentException("로그인 후 사용해주세요.");
        if (dto.getTitle() == null || dto.getContent() == null) throw new IllegalArgumentException("다시 작성해주세요.");

        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(writeMember)
                .build();
        return boardRepository.save(board);
    }

    // 게시글 단건 조회
    public Board findPost(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }

    // 게시글 불러오기 (페이징 처리)
    public Page<Board> findAllPost(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 게시글 수정
    public void updatePost(Long boardId, BoardPatchDto dto) {
        Board target = boardRepository.findById(boardId).orElse(null);
        if (target == null)
            throw new NullPointerException("게시글이 없습니다.");

        if (dto.getTitle() == null || dto.getContent() == null)
            throw new IllegalArgumentException("제대로 작성해주세요.");

        target.updateBoard(dto.getTitle(), dto.getContent());
        boardRepository.save(target);
    }

    // 게시글 삭제
    public void delete(Long boardId) {
        Optional<Board> target = boardRepository.findById(boardId);
        if (target.isEmpty()) {
            throw new NullPointerException("게시글이 없습니다.");
        }
        boardRepository.deleteById(boardId);
    }

    // 게시글 ID 가져오기
    public Long getBoardId(HttpServletRequest request) {
        String url = request.getHeaders("Referer").nextElement();
        return Long.parseLong(url.substring(url.length() - 1));
    }
}
