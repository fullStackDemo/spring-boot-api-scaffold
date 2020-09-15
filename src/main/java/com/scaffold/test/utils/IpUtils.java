package com.scaffold.test.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IpUtils {

    /*
     * 第一种方法
     */

    public static final String IPURL = "http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest";
    private static Map<Integer, List<int[]>> chinaIps = new HashMap<>();

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
        String LOCALHOST2 = "0:0:0:0:0:0:0:1";
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
                if (LOCALHOST.equalsIgnoreCase(ipAddress) || LOCALHOST2.equalsIgnoreCase(ipAddress)) {
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


    /*
     * 第二种方法
     */
    public static Map<String, String> getIpList() {
        // 集合存放Ip第一段
        Map<String, String> ipMap = new HashMap<>();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/china_ip.txt");
            List<String> lines = IOUtils.readLines(input, StandardCharsets.UTF_8);
            // 读取文件内所有的中国IP
            for (String line : lines) {
                if (!StringUtils.isEmpty(line)) {
                    String[] ips = line.split("\\.");
                    String ipFirst = ips[0];
                    // 得到一个ip地址段的起始范围
                    String[] strs = line.split("\\/");
                    long ip_len = Long.parseLong(strs[1]);
                    long start_ip = ipv4ToLong(strs[0]);
                    if(ipFirst.equals("101")){
                        System.out.println(line);
                    }
                    long end_ip = start_ip + (long) Math.pow(2, 32 - ip_len);
                    String ipRange = start_ip + "-" + end_ip;
                    ipMap.put(ipFirst, ipRange);
                }
            }
            System.out.println(ipMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipMap;
    }

    // 求出 IPV4 IP地址所对应的整数，比如 192.168.66.6 对应整数 3232252422
    // 192*256*256*256 + 168*256*256 + 66*256 + 6 = 3232252422
    // IP转换十进制（a.b.c.d）= a*256^3+b*256^2+c*256+d
    public static long ipv4ToLong(String ip) {
        String[] ips = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < ips.length; i++) {
            result += Long.parseLong(ips[i]) * Math.pow(256, 3 - i);
        }
        return result;
    }

    /**
     * 判断IP是不是在中国
     *
     * @param ipMap 中国ip集合
     * @param ip    传入的ip
     * @return true
     */
    public static boolean ipInChina(Map<String, String> ipMap, String ip) {
        if (ipMap == null) {
            ipMap = getIpList();
        }
        // 判断 IP 是否存在
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        // 第一个IP端作为key
        String key = ip.split("\\.")[0];
        // 当前IP转换为整数
        long ip_long = ipv4ToLong(ip);

        // 判断第一个IP端存在
        if (ipMap.containsKey(key)) {
            String ipRange = ipMap.get(key);
            String[] ipRanges = ipRange.split("\\-");
            if (ipRanges.length == 2) {
                long ipRange_start = Long.parseLong(ipRanges[0]);
                long ipRange_end = Long.parseLong(ipRanges[1]);
                // 判断是否在范围内
                if (ip_long >= ipRange_start && ip_long <= ipRange_end) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取外网IP
     *
     * @return
     */
    public static String getInternetIp() {
        // 内网IP
        String intranetIp = getIpAddress();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networkInterfaces.hasMoreElements()) {
                addrs = networkInterfaces.nextElement().getInetAddresses();
                while (addrs.hasMoreElements()) {
                    ip = addrs.nextElement();
                    if (ip instanceof Inet4Address && !ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress() && !ip.getHostAddress().equals(intranetIp)) {
                        return ip.getHostAddress();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return intranetIp;
    }

    public static void main(String[] args) {
        System.out.println(ipv4ToLong("192.168.66.6"));
        getIpList();
    }

}
