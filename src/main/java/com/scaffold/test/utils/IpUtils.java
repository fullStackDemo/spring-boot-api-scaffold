package com.scaffold.test.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IpUtils {


    public static final String IPURL = "http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest";

    /**
     * 获取中国IP
     *
     * @return
     */
//    public static Object getChinaIp() {
//        HttpParams httpParams = new HttpParams();
//        httpParams.setRequestUrl(IPURL);
//        String response = HttpUtils.get(httpParams);
//        File file = new File("F://ip.txt");
//        FileOutputStream fileOutputStream = null;
//        if (file.exists()) {
//            file.delete();
//        }
//        try {
//            file.createNewFile();
//            String content = response;
//            fileOutputStream = new FileOutputStream(file);
//            fileOutputStream.write(content.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fileOutputStream != null) {
//                    fileOutputStream.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }


    private static Map<Integer, List<int[]>> chinaIps = new HashMap<>();

    public IpUtils() {
        initData();
    }

    /**
     * 获取IP
     */
    public static Map<Integer, List<int[]>> initData() {
        System.out.println("start ip fetch");
        // 只存放属于中国的ip段
        Map<Integer, List<int[]>> map = new HashMap<>();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/delegated-apnic-latest");
            List<String> lines = IOUtils.readLines(input, StandardCharsets.UTF_8);
            for (String line : lines) {
                // 只获取中国的IP
                if (line.startsWith("apnic|CN|ipv4|")) {
                    System.out.println(line);
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
        return chinaIps;
    }


    /**
     * 判断是否属于中国IP
     *
     * @param ip
     * @return
     */
    public static boolean isChinaIp(String ip) {
        if (chinaIps.size() == 0) {
            chinaIps = initData();
        }
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        String[] strs = ip.split("\\.");
        int key = Integer.parseInt(strs[0]) * 256 + Integer.parseInt(strs[1]);
        List<int[]> list = chinaIps.get(key);
        if (list == null) {
            return false;
        }
        if (list.size() == 0) {
            // 整个IP端都是中国IP
            return true;
        }
        int value = Integer.parseInt(strs[2]) * 256 + Integer.parseInt(strs[3]);
        for (int[] ipRange : list) {
            if (value >= ipRange[0] && value <= ipRange[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getIpAddress() {
        String UNKNOWN = "unknown";
        String LOCALHOST = "127.0.0.1";
        String SEPARATOR = ",";
        String ipAddress = "";
        try {
            HttpServletRequest request = HttpUtils.getRequest();
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOCALHOST.equalsIgnoreCase(ipAddress)) {
                    InetAddress inetAddress = null;
                    try {
                        inetAddress = InetAddress.getLocalHost();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ipAddress = inetAddress.getHostAddress();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }



}
