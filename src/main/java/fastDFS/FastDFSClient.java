package fastDFS;

import org.csource.fastdfs.*;

import java.io.File;

/**
 * FastDFS分布式文件系统
 */

public class FastDFSClient {

    // 配置信息
    private static final String client_conf = Thread.currentThread().getContextClassLoader().getResource("fastDFS") + "fastClient.conf";

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
    private static Object uploadFile(File file) {
        
        return null;
    }

}
