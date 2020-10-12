package com.scaffold.test.controller;

import com.scaffold.test.utils.IpUtils;
import fastDFS.FastDFSClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("fastdfs")
public class FastController {

    @PostMapping("upload")
    public String uploadFIle(@RequestParam MultipartFile file) {
        String ipAddress = IpUtils.getIpAddress();
        String port = "9002";
        String path = FastDFSClient.uploadFile(file);
        return "http://"+ipAddress+":"+port + "/"+path;
    }
}
