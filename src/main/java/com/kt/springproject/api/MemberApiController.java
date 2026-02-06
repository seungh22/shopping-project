package com.kt.springproject.api;

import com.kt.springproject.domain.Member;
import com.kt.springproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    // V1의 문제점 - 요청 값으로 Member 엔티티를 직접 받는다.
    // 1.단순한 조회인데 검증 로직이 엥ㄴ티티에 들어가 있음 -> 다른 API에서는 이 검증 로직이 사용안될 수 있음 (@NotEmpty..)
    // 2.엔티티랑 API 스펙이 1대1로 매핑되어 있음 -> 엔티티를 수정하면 API스펙 자체가 바뀜 -> 엔티티를 외부에서 json 오는 걸 바인딩 받는데 쓰면 안됨.
    //  -> API 스펙을 위한 별도의 DTO를 만들어야 함
    // 3.실무에서는 등록 API가 여러개 만들어질 확률이 높음(소셜 로그인, 간편 로그인...) -> 지금처럼 API하나는 절대 안 됨 그래서 엔티티를 외부에 노출해서는 안된다
    // => API를 만들때 항상 엔티티를 파라미터로 받지 말고, 외부에 노출하지 말자

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // V1의 문제점 -> 엔티티를 직접 반환하면 안됨
    // 회원 정보만 원하는 건데 orders도 포함되어 있음 -> 엔티티를 직접 노출하게 되면 이 엔티티에 있는 정보들이 다 외부에 노출이 됨
    // -> 해결: @JsonIgnore을 사용하면 주문 정보가 빠짐
    // 어느 api는 주소가 필요한데 어느 api는 필요없고 등.. 케이스가 다양할 수 있다 -> 이걸 다 엔티티로 해결하기는 답이 없다,
    // 그리고 엔티티가 화면을 뿌리기 위한 로직이 들어감(@JsonIgnore 등) -> 엔티티가 쭉 엔티티로 의존 관계가 유지되어야 되는데 반대로 나가버린 것,
    // -> 양방향으로 의존 관계가 걸리면서 애플리케이션을 수정하기 어렵게 된다
    // 엔티티를 변경하면 API 스펙이 변경된다
    // 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)
    // 해결: API 응답 스펙에 맞추어 별도의 DTO를 반환한다.
    // 엔티티는 내부 로직에 집중하고, 외부와의 통신(API)에는 항상 DTO를 사용하여 분리하는 것이 바람직한 설계이다.

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        // 엔티티 -> DTO 변환
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    // 응답을 단순 JSON 배열로 반환하는 대신 별도의 'Wrapper' 객체(예: Result 클래스) 안에 배열을 담아 반환
    // 단순 배열로는 메타데이터(총 개수 등)를 함께 보내기 어렵지만, 객체로 감싸면 데이터 목록 외에 필요한 추가 정보를 유연하게 담을 수 있어 편리
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
    }

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
    @PostMapping("/api/v2/members/{id}")
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
