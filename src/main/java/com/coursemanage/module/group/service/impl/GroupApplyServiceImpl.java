package com.coursemanage.module.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.group.entity.GroupApply;
import com.coursemanage.module.group.entity.GroupMember;
import com.coursemanage.module.group.mapper.GroupApplyMapper;
import com.coursemanage.module.group.mapper.GroupMemberMapper;
import com.coursemanage.module.group.service.GroupApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupApplyServiceImpl extends ServiceImpl<GroupApplyMapper, GroupApply> implements GroupApplyService {

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    /**
     * 优化点：添加重复申请校验，避免无效提交
     */
    @Override
    public GroupApply applyJoinGroup(GroupApply apply) {
        // 1. 校验：是否已加入该小组（避免重复申请）
        QueryWrapper<GroupMember> memberWrapper = new QueryWrapper<>();
        memberWrapper.eq("group_id", apply.getGroupId())
                .eq("group_member_id", apply.getGroupApplicantStudentId());
        if (groupMemberMapper.selectCount(memberWrapper) > 0) {
            throw new RuntimeException("已加入该小组，无需重复申请");
        }

        // 2. 校验：是否有未审批的申请（避免重复提交）
        QueryWrapper<GroupApply> applyWrapper = new QueryWrapper<>();
        applyWrapper.eq("group_id", apply.getGroupId())
                .eq("group_applicant_student_id", apply.getGroupApplicantStudentId())
                .eq("is_read", false); // 未读=未审批（适配boolean类型）
        if (baseMapper.selectCount(applyWrapper) > 0) {
            throw new RuntimeException("已提交申请，请等待老师审批");
        }

        // 原有逻辑：初始化状态（保持不变）
        apply.setIsRead(false); // 未读
        apply.setIsPass(false); // 未通过
        save(apply);
        return apply;
    }

    /**
     * 优化点：校验申请是否存在，避免无效审批
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean approveApply(GroupApply apply) {
        // 1. 校验申请记录是否存在
        GroupApply existingApply = baseMapper.selectById(apply.getId());
        if (existingApply == null) {
            throw new RuntimeException("申请记录不存在");
        }

        // 2. 自动标记为“已读”，并更新审批结果
        existingApply.setIsRead(true);
        existingApply.setIsPass(apply.getIsPass());
        updateById(existingApply);

        // 3. 若通过，添加到小组成员表
        if (apply.getIsPass()) {
            GroupMember member = new GroupMember();
            member.setGroupId(existingApply.getGroupId());
            member.setGroupMemberId(existingApply.getGroupApplicantStudentId());
            groupMemberMapper.insert(member);
        }
        return true;
    }

    /**
     * 新增：查看所有申请列表（供老师使用，按申请时间倒序）
     */
    @Override
    public List<GroupApply> getApplyList() {
        QueryWrapper<GroupApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time"); // 假设表中有create_time字段（无则删除该条件）
        return baseMapper.selectList(queryWrapper);
    }
}