package SSRboard.member.service;

import SSRboard.member.domain.Member;
import SSRboard.member.dto.MemberJoinDto;
import SSRboard.member.dto.MemberLoginDto;
import SSRboard.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void beforeEach() {
        // 테스트 한번 돌리고 다 지워준다.
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 메서드가 잘 작동하는가?")
    void joinMember() {
        // 클라이언트에서 주는 DTO
        MemberJoinDto memberJoinDto =
                new MemberJoinDto("member@test.com", "test123!@#", "userA");

        // 회원가입..!
        memberService.joinMember(memberJoinDto);

        // 정상적으로 작동이 됬으면 repository 에 하나가 있어야함.
        assertThat(memberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원가입시 공백으로 데이터를 넘기면 메서드 예외가 잘 처리되는가?")
    void joinMemberException() {
        // 클라이언트에서 주는 데이터가 누락된 DTO
        MemberJoinDto emailJoinDto =
                new MemberJoinDto(null, "test123!@#", "userA");

        MemberJoinDto passwordJoinDto =
                new MemberJoinDto("member@test.com", null, "userA");

        MemberJoinDto nickNameJoinDto =
                new MemberJoinDto("member@test.com", "test123!@#", null);

        assertThrows(IllegalArgumentException.class, () -> memberService.joinMember(emailJoinDto));
        assertThrows(IllegalArgumentException.class, () -> memberService.joinMember(passwordJoinDto));
        assertThrows(IllegalArgumentException.class, () -> memberService.joinMember(nickNameJoinDto));
    }

    @Test
    @DisplayName("로그인이 잘 되는지?")
    void login() {
        // 클라이언트에서 주는 DTO
        MemberJoinDto memberJoinDto =
                new MemberJoinDto("member@test.com", "test123!@#", "userA");

        // 회원가입..!
        memberService.joinMember(memberJoinDto);

        MemberLoginDto dto = new MemberLoginDto("member@test.com", "test123!@#");
        Member loginMember = memberService.login(dto);

        // 회원가입된 정보와 로그인 정보가 일치하는지 체크
        Member findMember = memberRepository.findByMemberIdAndPassword(dto.getMemberId(), dto.getPassword());
        assertThat(loginMember.getMemberId()).isEqualTo(findMember.getMemberId());
        assertThat(loginMember.getPassword()).isEqualTo(findMember.getPassword());
    }

    @Test
    @DisplayName("아이디나 비밀번호를 잘못 입력한 경우 예외처리가 되는지?")
    void checkLogin() {
        // 클라이언트에서 주는 DTO
        MemberJoinDto memberJoinDto =
                new MemberJoinDto("member@test.com", "test123!@#", "userA");

        // 회원가입..!
        memberService.joinMember(memberJoinDto);

        MemberLoginDto idDto = new MemberLoginDto(null, "test123!@#");

        MemberLoginDto passwordDto = new MemberLoginDto("member@test.com", null);

        // 데이터가 정확하게 들어오지 않는 경우 예외처리
        assertThrows(IllegalArgumentException.class, () -> memberService.login(idDto));
        assertThrows(IllegalArgumentException.class, () -> memberService.login(passwordDto));
    }


}