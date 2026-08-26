package nz.ac.aut.comp713.library.service;

import nz.ac.aut.comp713.library.domain.Member;
import nz.ac.aut.comp713.library.repository.MemberRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class MemberService {

    @Inject
    private MemberRepository memberRepository;

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }
}