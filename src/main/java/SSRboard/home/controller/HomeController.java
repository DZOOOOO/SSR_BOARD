package SSRboard.home.controller;

import SSRboard.board.domain.Board;
import SSRboard.board.service.BoardService;
import SSRboard.config.web.session.SessionConst;
import SSRboard.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.Login_MEMBER, required = false) Member loginMember,
                       Model model,
                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                       Pageable pageable) {

        Page<Board> boardList = boardService.findAllPost(pageable);

        // 메인 페이지에 작성된 글 페이징 처리
        model.addAttribute("boards", boardList);
        model.addAttribute("member", loginMember);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasPrev", boardList.hasPrevious());
        model.addAttribute("hasNext", boardList.hasNext());

        return "home/home"; // --> 홈 화면
    }
}
