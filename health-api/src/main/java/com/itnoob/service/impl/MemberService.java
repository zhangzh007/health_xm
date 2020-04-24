package com.itnoob.service.impl;

import com.itnoob.pojo.Member;

import java.util.List;

public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonths(List<String> months);

}
