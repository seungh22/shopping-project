package com.kt.springproject.api;

import com.kt.springproject.domain.Member;
import com.kt.springproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.web.bind.annotation.*;

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
     * 회원 등록 API
     * V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 수정 API
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        Member findMember = memberService.findOne(id);
        memberService.update(id, request.getName());    // 수정은 왠만하면 더티 체킹으롱
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());  // @AllArgsConstructor을 썼기 때문에 모든 파라미터를 넘기는 생성자가 필요
    }

    @Data
    static class UpdateMemberRequest {
                private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
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
