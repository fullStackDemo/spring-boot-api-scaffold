package com.scaffold.test.obs;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * OBS Client
 */
public class MyObsClient {

    public static void main(String[] args) throws Exception {

        // 创建ObsClient实例
        ObsClient obsClient = null;
        String credentials = "/HuaWeiCloud/credentials.txt";
        String encoding = "UTF-8";

        try {
            String endPoint;
            // Access Key Id (AK)
            String ak;
            // Secret Access Key(SK)
            String sk;
            // 桶名
            String bucketName;

            // 获取认证信息
            File credentialsFile = new File(credentials);
            Map<Object, Object> credentialsData = new HashMap<>();
            if(credentialsFile.exists()){
                InputStreamReader inputStreamReader = null;
                try {
                    inputStreamReader = new InputStreamReader(new FileInputStream(credentialsFile), encoding);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String lineTxt = null;
                    while ((lineTxt = bufferedReader.readLine()) != null){
                        String[] arr = lineTxt.split("=");
                        credentialsData.put(arr[0], arr[1]);
                        System.out.println(lineTxt);
                    }
                    bufferedReader.close();
                } catch (UnsupportedEncodingException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(credentialsData.toString());
            // 赋值
            ak = (String) credentialsData.get("ak");
            sk = (String) credentialsData.get("sk");
            endPoint = (String) credentialsData.get("endPoint");
            bucketName = (String) credentialsData.get("bucketName");

            // 客户端
            obsClient = new ObsClient(ak, sk, endPoint);
            // 列举对象
            ObjectListing objectListing = obsClient.listObjects(bucketName);
            System.out.println("---------------------alex-images---start------------------");
            for (ObsObject obsObject : objectListing.getObjects()) {
                System.out.println(obsObject.getObjectKey() + "  " + Math.floor((double) (obsObject.getMetadata().getContentLength() / 1024)) + "KB");
            }
            System.out.println("---------------------alex-images----end-------------------");

            // 上传文件
            File file = new File("/HuaWeiCloud/test.png");
            String objectKey = file.getName();
            PutObjectResult result = obsClient.putObject(bucketName, objectKey, file);
            System.out.println(result.toString());
            System.out.println("-------------------------------------------");
            // 获取支持图片转码的下载链接
            // URL有效期，3600秒
            long expireSeconds = 365*24*3600L;
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
            request.setBucketName(bucketName);
            request.setObjectKey(objectKey);

            // 设置图片转码参数
            Map<String, Object> queryParams = new HashMap<>();
            // 图片处理
            // queryParams.put("x-image-process", "image/resize,m_fixed,w_100,h_100/rotate,100");
            request.setQueryParams(queryParams);
            // 通过ObsClient.createTemporarySignature生成带签名信息的URL
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            // 获取加密参数
            System.out.println(response.getSignedUrl());

        } catch (ObsException e) {
            System.out.println("HTTP Code: " + e.getResponseCode());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
            System.out.println("Request ID: " + e.getErrorRequestId());
            System.out.println("Host ID: " + e.getErrorHostId());
        } finally {
            // 关闭ObsClient实例，如果是全局ObsClient实例，可以不在每个方法调用完成后关闭
            // ObsClient在调用ObsClient.close方法关闭后不能再次使用
            if (obsClient != null) {
                try {
                    obsClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
