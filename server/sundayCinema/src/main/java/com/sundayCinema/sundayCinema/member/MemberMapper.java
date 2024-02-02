package com.sundayCinema.sundayCinema.member;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.MemberPostDto memberPostDto);


    Member memberPatchDtoToMember(MemberDto.MemberPatchDto memberPatchDto);

    MemberDto.MemberResponseDto memberToMemberResponseDto(Member member);

    List<MemberDto.MemberResponseDto> membersToMemberResponses(List<Member> members);
}