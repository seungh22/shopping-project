package com.kt.springproject.api;

import com.kt.springproject.domain.Member;
import com.kt.springproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

//    @PostMapping("/api/v1/members")
//    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
//        Long id = memberService.join(member);
//        return new CreateMemberResponse(id);
//    }
    // V1의 문제점 - 요청 값으로 Member 엔티티를 직접 받는다.
    // 1.단순한 조회인데 검증 로직이 엥ㄴ티티에 들어가 있음 -> 다른 API에서는 이 검증 로직이 사용안될 수 있음 (@NotEmpty..)
    // 2.엔티티랑 API 스펙이 1대1로 매핑되어 있음 -> 엔티티를 수정하면 API스펙 자체가 바뀜 -> 엔티티를 외부에서 json 오는 걸 바인딩 받는데 쓰면 안됨.
    //  -> API 스펙을 위한 별도의 DTO를 만들어야 함
    // 3.실무에서는 등록 API가 여러개 만들어질 확률이 높음(소셜 로그인, 간편 로그인...) -> 지금처럼 API하나는 절대 안 됨 그래서 엔티티를 외부에 노출해서는 안된다
    // => API를 만들때 항상 엔티티를 파라미터로 받지 말고, 외부에 노출하지 말자

    /**
     * V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
