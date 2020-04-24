package com.itnoob.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itnoob.constant.MessageConstant;
import com.itnoob.dao.MemberDao;
import com.itnoob.dao.OrderDao;
import com.itnoob.dao.OrderSettingDao;
import com.itnoob.entity.Result;
import com.itnoob.pojo.Member;
import com.itnoob.pojo.Order;
import com.itnoob.pojo.OrderSetting;
import com.itnoob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * synchronized:禁止同步操作。 仅限该项目部署在一台服务器上
     * 部署在多台服务器上 则用 分布式锁 如 redis、zookeeper
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Result order(Map map) throws Exception {

        //检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if(orderSetting == null){ //预约当天没有进行预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查预约日期是否预约已满
        Integer number = orderSetting.getNumber(); //可预约人数
        Integer reservations = orderSetting.getReservations(); //已预约人数
        if(reservations >= number){
            return new Result(false,MessageConstant.ORDER_NUMBER_FILL);
        }

        //检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约 则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if(member != null){
            // 判断是否在重复预约
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                // 已经预约成功 无需再此预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else{
            //检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//自动完成会员注册
        }

        // 预约成功
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(date);//预约日期
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐ID
        orderDao.add(order);

        // 更新当日的已预约人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) {
        return orderDao.findById4Detail(id);
    }

}
