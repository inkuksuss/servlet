package com.example.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void singleTon() {
        Assertions.assertThat(MemberRepository.getInstance()).isSameAs(MemberRepository.getInstance());
    }

    @Test
    void save() {
        Member memberA = new Member("memberA", 20);
        Member saveMember = memberRepository.save(memberA);
        Member findMember = memberRepository.findById(saveMember.getId());

        Assertions.assertThat(saveMember).isEqualTo(findMember);
    }

    @Test
    void findAll() {
        Member memberA = new Member("memberA", 20);
        Member memberB = new Member("memberB", 10);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> all = memberRepository.findAll();

        Assertions.assertThat(all.size()).isEqualTo(2);
    }

}