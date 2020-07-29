package com.scaffold.test.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;

import java.util.List;

/**
 * 钉钉相关操作
 */

public interface DingTalkService {


    /**
     * 获取 access_token
     * @return
     */
    String getAccessToken();


    /**
     * 获取部门列表
     * @return List
     */
    List<OapiDepartmentListResponse.Department> getDeptList(String deptId);

}
