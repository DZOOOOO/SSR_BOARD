package SSRboard.member.service;

import SSRboard.member.domain.Member;
import SSRboard.member.dto.MemberJoinDto;
import SSRboard.member.dto.MemberLoginDto;
import SSRboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    public void joinMember(MemberJoinDto dto) {
        if (dto.getMemberId() == null || dto.getPassword() == null || dto.getNickName() == null)
            throw new IllegalArgumentException("정확하게 데이터를 입력해주세요.");

        Member joinMember = Member.builder()
                .memberId(dto.getMemberId())
                .password(dto.getPassword())
                .nickName(dto.getNickName())
                .build();
        memberRepository.save(joinMember);
    }

    // 로그인
    public Member login(MemberLoginDto dto) {
        if (dto.getMemberId() == null || dto.getPassword() == null)
            throw new IllegalArgumentException("정확하게 데이터를 입력해주세요.");

        return memberRepository.findByMemberIdAndPassword(dto.getMemberId(), dto.getPassword());
    }

    // 맴버 찾기
    public Member findMember(String memberId) {
        if (memberId == null) throw new NullPointerException("맴버가 없습니다.");
        return memberRepository.findByMemberId(memberId);
    }
}
