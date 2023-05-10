package SSRboard.comment.service;

import SSRboard.board.domain.Board;
import SSRboard.board.service.BoardService;
import SSRboard.comment.domain.Comment;
import SSRboard.comment.dto.CommentUpdateDto;
import SSRboard.comment.repository.CommentRepository;
import SSRboard.member.domain.Member;
import SSRboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;

    // 댓글작성
    public Comment commentWrite(Long boardId, Member member, String comment) {
        if (boardId == null || member == null || comment == null)
            throw new IllegalArgumentException("다시 시도해주세요.");

        Board targetBoard = boardService.findPost(boardId);

        Comment newComment = Comment.builder()
                .content(comment)
                .member(member)
                .board(targetBoard)
                .build();

        return commentRepository.save(newComment);
    }

    // 작성 댓글 찾기(member + id)
    public Comment findComment(Long commentId, Member member) {
        return commentRepository.findByIdAndMember(commentId, member);
    }

    // 게시글에 작성된 댓글 모두 가져오기
    public List<Comment> findCommentList(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    // 댓글 수정
    public void updateComment(Long id, CommentUpdateDto dto) {
        Comment target = commentRepository.findById(id).orElse(null);

        if (target != null) {
            target.updateComment(dto.getComment());
            commentRepository.save(target);
        }
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
