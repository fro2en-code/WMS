package com.ymt.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.wms.business.TaskFlow;

import wms.business.service.TaskFlowService;

@Service("taskUtils")
public class TaskUtils {
    @Resource
    private TaskFlowService taskFlowService;
    @Resource
    private UserUtils userUtils;

    /**
     * 根据任务代码,获取任务执行人
     */
    public Set<String> getTaskUserByTaskCode(int taskCode, String whCode) {
        Set<String> result = new HashSet<>();
        TaskFlow taskFlow = taskFlowService.getTaskFlowByTaskCode(taskCode, whCode);
        if (StringUtils.isNotEmpty(taskFlow.getNowPerson())) {// 优先取人员
            result.add(taskFlow.getNowPerson());
        } else if (StringUtils.isNotEmpty(taskFlow.getNowRole())) {// 然后是角色
            List<String> usres = userUtils.getUserByRole(taskFlow.getNowRole());
            result.addAll(usres);
        }
        return result;
    }
}
