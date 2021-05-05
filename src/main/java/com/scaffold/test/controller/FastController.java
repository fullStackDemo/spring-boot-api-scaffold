//package com.scaffold.test.controller;
//
//import com.scaffold.test.base.Result;
//import com.scaffold.test.base.ResultGenerator;
//import com.scaffold.test.constants.BaseApplication;
//import com.scaffold.test.fastDFS.FastDFSClient;
//import com.scaffold.test.utils.IpUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//
//@RestController
//@RequestMapping("fastdfs")
//public class FastController {
//
//    @Resource
//    BaseApplication baseApplication;
//
//    /**
//     * 上传
//     * @param file
//     * @return
//     */
//    @PostMapping("upload")
//    public Result uploadFile(@RequestParam MultipartFile file) {
//        String ipAddress = IpUtils.getIpAddress();
//        String port = baseApplication.getServerPort();
//        String path = FastDFSClient.uploadFile(file);
//        String filePath = "http://" + ipAddress + ":" + port + "/" + path;
//        return ResultGenerator.setSuccessResult(filePath);
//    }
//
//}
