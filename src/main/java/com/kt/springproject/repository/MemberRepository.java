package com.kt.springproject.repository;

import com.kt.springproject.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext // 스프링이 EntityManager를 만들어서 인잭션 해줌
    // 스프링 데이터 JPA 사용하면 @Autowired로 인젝션되게 지원을 해주고, @RequiredArgsConstructor을 사용하여 final로 바꿀 수 있다
    // 레포지토리에 Entity 매니저를 인젝션 생성자로 인젝션 한 상태
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
