package com.scaffold.test.utils;

import com.scaffold.test.entity.HttpParams;
import com.scaffold.test.redis.RedisUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IpUtils {

    public static final String IPURL = "http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest";

    /**
     * 获取中国IP
     *
     * @return
     */
    public static Object getChinaIp() {
        HttpParams httpParams = new HttpParams();
        httpParams.setRequestUrl(IPURL);
        String response = HttpUtils.get(httpParams);
        File file = new File("F://ip.txt");
        FileOutputStream fileOutputStream = null;
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            String content = response;
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Map<Integer, List<int[]>> chinaIps = new HashMap<>();

    public static int test = 0;
    public static void initData() {
        // 只存放属于中国的ip段
        Map<Integer, List<int[]>> map = new HashMap<>();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/delegated-apnic-latest");
            List<String> lines = IOUtils.readLines(input, StandardCharsets.UTF_8);
            for (String line : lines) {
                // 只获取中国的IP
                if (line.startsWith("apnic|CN|ipv4|")) {
                    test += 1;
                    System.out.println(line);
                    System.out.println(test);
                    String[] items = line.split("\\|");
                    // ip
                    String ip = items[3];
                    // IP分为ad1,ad2,ad3,ad4
                    String[] ipAddress = ip.split("\\.");
                    // 地址数
                    int count = Integer.parseInt(items[4]);

                    int startIp = Integer.parseInt(ipAddress[0]) * 256 + Integer.parseInt(ipAddress[1]);

                    while (count > 0) {
                        // 65536 = 256 * 256
                        if (count > 65536) {
                            // ad1,ad2 整段都是中国IP
                            chinaIps.put(startIp, Collections.emptyList());
                            count -= 65536;
                            startIp += 1;
                        } else {
                            // 范围
                            int[] ipRange = new int[2];
                            ipRange[0] = Integer.parseInt(ipAddress[2]) * 256 + Integer.parseInt(ipAddress[3]);
                            ipRange[1] = ipRange[0] + count;
                            count -= 1;

                            List<int[]> list = map.get(startIp);
                            if (list == null) {
                                list = new ArrayList<>();
                                map.put(startIp, list);
                            }
                            list.add(ipRange);

                        }
                    }


                }
            }
            chinaIps.putAll(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RedisUtils redisUtils = new RedisUtils();
        redisUtils.set("ipList", "11");
        System.out.println("test");
    }


    public static void main(String[] args) {
        initData();
    }
}
