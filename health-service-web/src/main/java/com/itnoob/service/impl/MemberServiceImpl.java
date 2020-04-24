package com.itnoob.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itnoob.dao.MemberDao;
import com.itnoob.pojo.Member;
import com.itnoob.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员服务
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //保存会员信息
    public void add(Member member) {
        String password = member.getPassword();
        if(password != null){
            //使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    //根据月份查询会员数量 统计会员数量
    public List<Integer> findMemberCountByMonths(List<String> sa) {//2018.05
        List<Integer> memberCount = new ArrayList<>();
        for (String month : sa) {
            String date = month + ".31";//2018.05.31
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }
        return memberCount;
    }
}
