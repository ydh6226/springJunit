package com.example.springjunit.study;

import com.example.springjunit.domain.Member;
import com.example.springjunit.domain.Study;
import com.example.springjunit.domain.StudyStatus;
import com.example.springjunit.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    StudyRepository repository;

    @Test
    @DisplayName("스터디 생성")
    //함수에서 직접 mock 주입 가능
    void createStudy(@Mock MemberService memberService, @Mock StudyRepository repository) throws Exception {
        StudyService studyService = new StudyService(memberService, repository);
        assertThat(studyService).isNotNull();

        Member member = new Member(1L, "ydh@gmail.com");
//        doThrow(IllegalArgumentException.class).when(memberService).validate(1L);

        //파라미터로 어떤 값이 들어와도 같은 값 반환
//        given(memberService.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(member));


        //순서에 따라 반환값 다르게 설정 가능
        given(memberService.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.of(member))
                .willThrow(new RuntimeException());

        Optional<Member> findMember = memberService.findById(1L);
        assertThat(findMember.get()).isEqualTo(member);
        assertThrows(RuntimeException.class, () -> memberService.findById(1L));
    }

    @Test
    @DisplayName("스터디 생성2")
    void createStudy2() throws Exception {
        //Optional 반환시 empty로 반환
        Optional<Member> optionalMember = memberService.findById(1L);
        assertThat(optionalMember).isEmpty();

        //void 반환시 아무일도 일어나지 않음.
        memberService.validate(1L);
    }

    @Test
    @DisplayName("stubbing 연습")
    void practice() throws Exception {
        //given
        StudyService studyService = new StudyService(memberService, repository);

        Member member = new Member(1L, "ydh@gmail.com");
        Study study = new Study(10, "test");

        given(memberService.findById(anyLong())).willReturn(Optional.of(member));
        given(repository.save(study)).willReturn(study);

        //when
        Study returnStudy = studyService.createStudy(1L, study);

        //then
        assertThat(returnStudy.getOwner()).isEqualTo(member);

        //MemberService::notify 가 1번 호출되었어야 함 (bddMockito)
        then(memberService).should(times(1)).notify(study);

        //더이상 추가적인 작업이 없어야 할 때
//        then(memberService).shouldHaveNoMoreInteractions();

        verify(memberService, times(1)).notify(member);

        //MemberService::validate는 호출되지 않았어야함.
        verify(memberService, never()).validate(any());

        //순차적으로 발생했는지 검증가능
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);

        inOrder.verify(memberService).notify(member);
    }

    @Test
    @DisplayName("스터디 open")
    void openStudy() throws Exception {
        //given
        StudyService service = new StudyService(memberService, repository);
        Study study = new Study(10, "더 자바 테스트");

        given(repository.save(any())).willReturn(study);

        //when
        service.openStudy(study);

        //then
        assertThat(study.getStatus()).isEqualTo(StudyStatus.OPENED);
        assertThat(study.getCreateTime()).isNotNull();
        then(memberService).should(times(1)).notify(study);
    }
}