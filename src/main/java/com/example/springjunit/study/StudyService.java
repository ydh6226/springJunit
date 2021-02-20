package com.example.springjunit.study;

import com.example.springjunit.domain.Member;
import com.example.springjunit.domain.Study;
import com.example.springjunit.member.MemberService;

import java.util.Optional;

public class StudyService {

    private final MemberService memberService;

    private final StudyRepository repository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        assert memberService != null;
        assert repository != null;

        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createStudy(Long memberId, Study study) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member doesn't exist id : " + memberId));
        study.setOwner(member);
        Study newStudy = repository.save(study);

        memberService.notify(newStudy);
        memberService.notify(member);

        return newStudy;
    }

    public Study openStudy(Study study) {
        study.open();
        Study openStudy = repository.save(study);
        memberService.notify(openStudy);
        return openStudy;
    }
}
