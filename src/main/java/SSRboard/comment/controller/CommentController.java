package SSRboard.comment.controller;

import SSRboard.board.domain.Board;
import SSRboard.board.service.BoardService;
import SSRboard.comment.domain.Comment;
import SSRboard.comment.dto.CommentUpdateDto;
import SSRboard.comment.dto.CommentWriteDto;
import SSRboard.comment.service.CommentService;
import SSRboard.config.web.session.SessionConst;
import SSRboard.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;

    // 게사글 상세 페이지에 댓글 렌더링
    @GetMapping("/view/{boardId}")
    public List<Comment> commentList(@PathVariable Long boardId,
                                     @SessionAttribute(name = SessionConst.Login_MEMBER, required = false) Member loginMember,
                                     Model model) {
        List<Comment> commentList = commentService.findCommentList(boardId);
        model.addAttribute("commentList", commentList);

        model.addAttribute("member", loginMember);
        return commentList;
    }

    // 댓글 작성 (로그인 된 사용자만 가능)
    @PostMapping("/write")
    public String writeComment(@Validated @ModelAttribute CommentWriteDto dto,
                               BindingResult bindingResult,
                               HttpServletRequest request) {
        // 로그인 체크
        Member loginUser = (Member) request.getSession().getAttribute(SessionConst.Login_MEMBER);

        // boardId 체크
        Long boardId = boardService.getBoardId(request);
        log.info("boardId = {}", boardId);

        if (bindingResult.hasErrors()) {
            log.error("error = {}", bindingResult.getFieldError());
            return "redirect:/board/detail/" + boardId;
        }

        commentService.commentWrite(boardId, loginUser, dto.getComment());
        return "redirect:/board/detail/" + boardId;
    }

    // 댓글 삭제
    @GetMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        commentService.deleteComment(commentId);
        Long boardId = boardService.getBoardId(request);
        log.info("boardId = {}", boardId);
        return "redirect:/board/detail/" + boardId;
    }

    // 댓글 수정 --> js 필요
    @PostMapping("/update/{commentId}")
    public String updateComment(@Validated @ModelAttribute CommentUpdateDto dto,
                                @PathVariable Long commentId,
                                BindingResult bindingResult,
                                HttpServletRequest request) {

        // 작성자 체크
        Member writeMember = (Member) request.getSession().getAttribute(SessionConst.Login_MEMBER);
        Long memberId = writeMember.getId();

        // 작성된 댓글
        Comment comment = commentService.findComment(commentId);

        if (bindingResult.hasErrors()) {
            log.error("error = {}", bindingResult.getFieldError());
            return "board/boardDetail";
        }

        // boardId 체크
        Long boardId = boardService.getBoardId(request);
        log.info("boardId = {}", boardId);

        if (!Objects.equals(comment.getMember().getId(), memberId)) {
            log.info("error = {}", bindingResult.getFieldError());
            bindingResult.reject("memberNotMatch", "작성자가 다릅니다.");
        }

        commentService.updateComment(commentId, dto);

        return "redirect:/board/detail/" + boardId;
    }

}
