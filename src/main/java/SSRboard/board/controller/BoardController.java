package SSRboard.board.controller;

import SSRboard.board.domain.Board;
import SSRboard.board.dto.BoardPatchDto;
import SSRboard.board.dto.BoardPostDto;
import SSRboard.board.service.BoardService;
import SSRboard.comment.domain.Comment;
import SSRboard.comment.dto.CommentWriteDto;
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

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 글 작성 화면 --> 로그인된 경우만 가능
    @GetMapping("/post")
    public String getPost(@SessionAttribute(name = SessionConst.Login_MEMBER, required = false) Member loginMember,
                          Model model) {

        model.addAttribute("member", loginMember);
        model.addAttribute("postDto", new BoardPostDto());
        return "board/addPost";
    }

    // 글 작성 Controller --> 로그인된 경우만 가능
    @PostMapping("/post")
    public String addPost(@Validated @ModelAttribute("postDto") BoardPostDto dto,
                          BindingResult bindingResult,
                          HttpServletRequest request) {
        Member writeMember = (Member) request.getSession().getAttribute(SessionConst.Login_MEMBER);
        log.info("writeMember ={}, title = {}, content = {}", writeMember, dto.getTitle(), dto.getContent());

        if (bindingResult.hasErrors()) {
            log.error("error = {}", bindingResult.getFieldError());
            return "board/addPost"; // 글쓰기 페이지 유지
        }

        Board newBoard = boardService.createPost(dto, writeMember);
        return "redirect:/board/detail/" + newBoard.getId();
    }

    // 게시글 조회 -> 비로그인 가능 + 작성된 댓글도 같이 조회
    @GetMapping("/detail/{boardId}")
    public String viewPost(@SessionAttribute(name = SessionConst.Login_MEMBER, required = false) Member loginMember,
                           @PathVariable("boardId") Long boardId,
                           @ModelAttribute Board board,
                           Model model,
                           BindingResult bindingResult) {
        model.addAttribute("member", loginMember);
        Board target = boardService.findPost(boardId);
        model.addAttribute("board", target);
        if (target == null) {
            log.info("error = {}", bindingResult.getFieldError());
            bindingResult.reject("게시글이 없습니다.");
            return "redirect:/"; // --> 메인페이지로
        }

        // 해당 게시물에 등록된 댓글 조회 (댓글도 페이징 가능하면 하기)
        List<Comment> boardCommentList = target.getCommentList();
        model.addAttribute("boardCommentList", boardCommentList);
        model.addAttribute("writeDto", new CommentWriteDto());

        return "board/boardDetail";
    }

    // 글 수정 화면 조회
    @GetMapping("/detail/{boardId}/update")
    public String updateViewPost(@SessionAttribute(name = SessionConst.Login_MEMBER, required = false) Member loginMember,
                                 @PathVariable("boardId") Long boardId,
                                 Model model) {
        // 로그인 유저
        model.addAttribute("member", loginMember);

        // 수정 게시글 대상
        Board target = boardService.findPost(boardId);
        model.addAttribute("board", target);

        log.info("boardId = {}, member = {}", boardId, target.getMember().getNickName());

        // 수정 DTO
        BoardPatchDto boardPatchDto = new BoardPatchDto(target.getId(), target.getTitle(), target.getContent());
        model.addAttribute("boardPatchDto", boardPatchDto);

        return "board/editPost";
    }

    // 글 수정
    @PostMapping("/detail/{boardId}/update")
    public String updatePost(@PathVariable("boardId") Long boardId,
                             @Validated @ModelAttribute("boardPatchDto") BoardPatchDto dto,
                             BindingResult bindingResult) {

        log.info("Update ===>  id = {}, title = {}, content = {}", dto.getBoardId(), dto.getTitle(), dto.getContent());

        if (bindingResult.hasErrors()) {
            log.error("error = {}", bindingResult.getFieldError());
            return "board/editPost"; // 글쓰기 페이지 유지
        }

        Board target = boardService.findPost(boardId);
        if (target == null) {
            log.info("error = {}", bindingResult.getFieldError());
            bindingResult.reject("게시글이 없습니다.");
            return "redirect:/";
        }

        boardService.updatePost(boardId, dto);
        return "redirect:/board/detail/" + boardId;
    }

    // 글 삭제
    @GetMapping("/detail/{boardId}/delete")
    public String deletePost(@PathVariable("boardId") Long boardId) {
        boardService.delete(boardId);
        return "redirect:/";
    }


}
