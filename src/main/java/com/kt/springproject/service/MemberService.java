package com.kt.springproject.service;

import com.kt.springproject.domain.Member;
import com.kt.springproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // Setter Injection -> 스프링이 바로 주입하는게 아니라 들어와서 주입해준다
    // 장점: 테스트 코드 작성할때 mock을 직접 주입해 줄 수 있다 (필드는 그냥 주입하기 까다로움)
    // 단점: 애플리케이션이 돌아가는 시점에 변경을 할 수도 있다 -> 하지만 보통은 이 변경하는 일이 없다
    // => 그래서 Setter Injection은 좋은 방법이 아니고 생성자 인젝션을 사용한다
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 스프링이 뜰 때 생성자에서 memberRepository를 인젝션 해줌
    // 생성자 인젝션은 한번 생성할때 끝나기 때문에 중간에 set해서 memberRepository를 바꿀 수 없다
    // 테이스 케이스를 작성할때 예로 멤버 서비스를 작성하면 직접 값을 주입해야되서 놓칠일이 없다
    // 생성자가 하나만 있는 경우에는 스프링이 자동으로 인젝션을 해주기 때문에 @Autowired 어노테이션이 없도 됨

    // @RequiredArgsConstructor는 final이 있는 필드만 가지고 생성자를 만들어줌
    // 기본적으로 인젝션 하면선 생성자에서 세팅하고 끝날 애들은 final로 잡아줌
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // member 수를 세서 그게 수가 0보다 크면 문제가 있다는 식으로 로직을 짜는게 더 최적화
    // 멀티스레드나 두 사람이 동시에 같은 이름으로 가입하는 경우를 고려해서 디비에 member의 name을 유니크 제약조건을 잡는걸 권장
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
