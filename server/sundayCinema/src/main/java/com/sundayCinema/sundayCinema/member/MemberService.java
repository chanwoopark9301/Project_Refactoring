package com.sundayCinema.sundayCinema.member;


import com.sundayCinema.sundayCinema.exception.BusinessLogicException;
import com.sundayCinema.sundayCinema.exception.ExceptionCode;
import com.sundayCinema.sundayCinema.logIn.utils.CustomAuthorityUtils;
import com.sundayCinema.sundayCinema.logIn.utils.UserAuthService;
import lombok.Synchronized;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    @Lazy
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                         CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;

    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, timeout = 30)
    public Member createMember(Member member) {
        try{
            String encryptedPassword = passwordEncoder.encode(member.getPassword());
            member.setPassword(encryptedPassword);
            List<String> roles = authorityUtils.createRoles(member.getEmail());
            member.setRoles(roles);
            Member savedMember = memberRepository.save(member);

            return savedMember;
        }catch (DataIntegrityViolationException e){
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
    //POST(OAuth2.0 회원 등록) : OAuth2.0 를 통해 가입된 회원 정보 저장 (DB에 해당 정보 존재하면 해당 엔티티 리턴하고 존재하지 않으면 저장)
    public Member createMemberOAuth2(Member member) {
        Optional<Member> findMember =memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()) {
            return findMember.get(); //이미 DB에 저장된 정보가 있다면 반환
        }

        // DB에 저장된 정보가 없다면
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        Member beSavedMember = new Member(
                member.getUserName(),          // DisplayName null (이후 추가로 변경하는 창을 redirection 할 수 있음)
                member.getEmail(), // 구글 이메일을 DB에 등록
                "",                //암호화된 비밀번호 빈 문자열
                roles,               //권한 목록
                member.getProfileImageUrl()
        );

        verifyExistsEmail();
        Member savedMember = memberRepository.save(beSavedMember);
        return savedMember;
    }

    public Member updateMember(Member member)  {
        Member findMember = verifyExistsEmail();

        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> findMember.setPassword(password));
        Optional.ofNullable(member.getUserName())
                .ifPresent(userName -> findMember.setUserName(userName));
        return memberRepository.save(findMember);
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }

    public void deleteMember(HttpServletRequest request)  {
        Member findMember = verifyExistsEmail();

        memberRepository.delete(findMember);
    }


    public Member verifyExistsEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return memberRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_SIGNED_IN);
        }
    }
}
