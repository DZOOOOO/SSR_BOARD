package SSRboard.member.repository;

import SSRboard.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberIdAndPassword(String memberId, String password);
    Member findByMemberId(String memberId);

}
