package SSRboard.member.controller;

import SSRboard.config.web.session.SessionConst;
import SSRboard.member.domain.Member;
import SSRboard.member.dto.MemberJoinDto;
import SSRboard.member.dto.MemberLoginDto;
import SSRboard.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/join")
    public String viewJoinPage(Model model) {
        model.addAttribute("joinDto", new MemberJoinDto());
        return "member/joinPage";
    }

    // 회원가입
    @PostMapping("/join")
    public String joinMember(@Validated @ModelAttribute(value = "joinDto") MemberJoinDto dto,
                             BindingResult bindingResult) {
        log.info("userId = {}, password = {}, nickName = {}", dto.getMemberId(), dto.getPassword(), dto.getNickName());

        if (bindingResult.hasErrors()) {
            // 이메일 형식으로 오지 않을 경우 예외처리..!
            log.error("error = {}", bindingResult.getFieldError());
            return "member/joinPage";
        }

        memberService.joinMember(dto);
        return "redirect:/member/login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        model.addAttribute("loginDto", new MemberLoginDto());
        return "member/login";
    }

    // 로그인(세션)
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute(name = "loginDto") MemberLoginDto dto,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        log.info("userId = {}, password = {}", dto.getMemberId(), dto.getPassword());
        log.info("redirectURL = {}", redirectURL);

        Member loginMember = memberService.login(dto);

        if (bindingResult.hasErrors()) {
            log.error("error = {}", bindingResult.getFieldError());
            return "member/login";
        }

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호를 다시 입력해주세요.");
            return "member/login";
        }

        // 세션이 있으면 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.Login_MEMBER, loginMember);
        return "redirect:" + redirectURL;
    }

    // 로그 아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션 삭제
        HttpSession session = request.getSession(false); // --> false 세션을 새로 생성하지 않는다.

        if (session != null) {
            session.invalidate(); // 세션 제거
        }
        return "redirect:/";
    }

}
