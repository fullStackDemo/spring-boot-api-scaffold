package com.scaffold.test.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.scaffold.test.service.DingTalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DingTalkServiceImpl implements DingTalkService {


    private String appkey = "dingupgq6lgpjscg3s";
    private String appsecret = "rVPMi9pJGxThhcAaynIuXeeeHOJ5kAdZDb5sj1P4FrhB7JQ_R3x74UeAcOe";

    @Override
    public String getAccessToken() {
        String accessToken = "";
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appkey);
        request.setAppsecret(appsecret);
        request.setHttpMethod("GET");
        try {
            OapiGettokenResponse response = client.execute(request);
            accessToken = response.getAccessToken();
            log.info("获取accessToken=" +  accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    @Override
    public List<OapiDepartmentListResponse.Department> getDeptList(String deptId) {
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        if (deptId.equals("null")) {
            deptId = "1";
        }
        request.setId(deptId);
        request.setHttpMethod("GET");
        List<OapiDepartmentListResponse.Department> departmentList = null;
        try {
            OapiDepartmentListResponse response = client.execute(request, getAccessToken());
            departmentList = response.getDepartment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departmentList;
    }
}
