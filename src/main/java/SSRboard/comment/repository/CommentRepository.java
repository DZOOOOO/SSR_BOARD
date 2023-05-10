package SSRboard.comment.repository;

import SSRboard.comment.domain.Comment;
import SSRboard.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);
    Comment findByIdAndMember(Long id, Member member);
}
