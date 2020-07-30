package com.scaffold.test.controller;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.entity.Department;
import com.scaffold.test.service.DepartmentService;
import com.scaffold.test.service.DingTalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/dingTalk")
@RestController
public class DingTalkController {

    @Autowired
    DingTalkService dingTalkService;
    @Autowired
    DepartmentService departmentService;

    @PassToken
    @GetMapping("sync")
    public Object syncData() {
        List<OapiDepartmentListResponse.Department> deptList = dingTalkService.getDeptList("1");
        for (OapiDepartmentListResponse.Department dept: deptList) {
            Department department = new Department();
            department.setDepId(dept.getId());
            department.setDepName(dept.getName());
            department.setParentId(dept.getParentid());
            department.setYn(1);
            departmentService.insert(department);
        }
        return deptList;
    }

}
