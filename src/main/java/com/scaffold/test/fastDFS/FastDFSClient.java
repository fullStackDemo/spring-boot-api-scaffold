package com.scaffold.test.fastDFS;

import jodd.util.StringUtil;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * FastDFS分布式文件系统
 */
public class FastDFSClient {

    // 配置信息
    private static final String client_conf = Thread.currentThread().getContextClassLoader().getResource("fastDFS/fastClient.conf").getPath();

    // 客户端
    private static StorageClient storageClient = null;

    static {
        try {
            // 加载配置文件
            ClientGlobal.init(client_conf);
            // 初始化Tracker客户端
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            // 初始化Tracker服务端
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            // 初始化Storage客户端
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 初始化Storage客户端
            storageClient = new StorageClient(trackerServer, storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public static String uploadFile(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            FileInputStream fileInputStream = (FileInputStream) inputStream;
            String fileName = file.getOriginalFilename();
            String fileExtName = fileName.split("\\.")[1];
            // 准备字节数组
            byte[] fileBuff = null;
            // 文件元数据
            NameValuePair[] metaList = null;
            // 查看文件的长度
            int len = fileInputStream.available();
            // 初始化元数据数组
            metaList = new NameValuePair[2];
            // 第一组元数据，文件的原始名称
            metaList[0] = new NameValuePair("file_name", fileName);
            // 第二组元数据，文件的长度
            metaList[1] = new NameValuePair("file_length", String.valueOf(len));
            // 创建对应长度的字节数组
            fileBuff = new byte[len];
            // 将输入流中的字节内容，读到字节数组中
            fileInputStream.read(fileBuff);
            String[] fileids = storageClient.upload_file(fileBuff, fileExtName, metaList);
            return StringUtil.join(fileids, "/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
