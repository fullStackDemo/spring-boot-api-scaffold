package com.scaffold.test.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class IpUtils {

    /*
     * 第一种方法
     */

    public static final String IPURL = "http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest";

    /**
     * 获取IP
     */
    public static Map<Integer, List<int[]>> initData() {
        Map<Integer, List<int[]>> chinaIps = new HashMap<>();
        System.out.println("start ip fetch");
        // 只存放属于中国的ip段
        Map<Integer, List<int[]>> map = new HashMap<>();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/delegated-apnic-latest-cn.txt");
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
    public static boolean isChinaIp(Map<Integer, List<int[]>> ipData, String ip) {
        if (ipData.size() == 0) {
            ipData = initData();
        }
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        String[] strs = ip.split("\\.");
        int key = Integer.parseInt(strs[0]) * 256 + Integer.parseInt(strs[1]);
        List<int[]> list = ipData.get(key);
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
    public static Map<String, Object> getIpList() {
        // 集合存放Ip第一段
        Map<String, Object> ipMap = new HashMap<>();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/china_ip.txt");
            List<String> lines = IOUtils.readLines(input, StandardCharsets.UTF_8);
            // 读取文件内所有的中国IP
            for (String line : lines) {
                if (!StringUtils.isEmpty(line)) {
                    JSONObject parentObj = new JSONObject();
                    String[] ips = line.split("\\.");
                    // 得到一个ip地址段的起始范围 101
                    int ip1 = Integer.parseInt(ips[0]);
                    int ip2 = Integer.parseInt(ips[1]);
                    /*
                     * 101.80.0.0/20 等于  apnic|CN|ipv4|101.80.0.0|1048576(2的20次方)
                     * 101.96.0.0/11 等于  apnic|CN|ipv4|101.96.0.0|2048(2的11次方)
                     * 类似如此数据，IP网端每个地址数256也就是2的8次方，总共是2的32次方
                     * 所以如果最后一个数值超过 16，意味着后两个网络被占满，前面的网段需要递增
                     * 101.80.0.0/20 中 20 意味着后两个网段已满, 第二个网络端递增 2的(20-16)次方等于16
                     * 101.80.0.0/20 = 以下IP从 80 ~ 95 全网端都是中国IP
                     * 101.80.0.0
                     * 101.81.0.0
                     * 101.82.0.0
                     * ...
                     * 101.95.0.0
                     */
                    // 获取从当前IP段开始的总地址数
                    String[] strs = line.split("\\/");
                    long addressCount = Long.parseLong(strs[1]);
                    // 存储各个网络段
                    JSONObject object = new JSONObject();
                    if (ipMap.get(String.valueOf(ip1)) != null) {
                        object = (JSONObject) ipMap.get(String.valueOf(ip1));
                    }
                    // 判断是否后两个字段被占满
                    if (addressCount > 16) {
                        // 后两个字段被占满时，也就是地址数大于 256*256=65536=2的16次方
                        double pow = Math.pow(2, addressCount - 16);
                        for (int i = 0; i < pow; i++) {
                            object.put(String.valueOf(ip2 + i), "all");
                        }
                    } else {
                        /**
                         * apnic CN三个连续数据如下
                         * 101.96.0.0/11
                         * 101.96.8.0/10
                         * 101.96.16.0/12
                         * ---------------------
                         * 如上在第二网段相同的情况
                         * 101.96.0.0/11 等于
                         * 101.96.0.0
                         * ...
                         * 101.96.7.0
                         * 共8个
                         *----------------------
                         * 101.96.8.0/10 等于
                         * 101.96.8.0
                         * ...
                         * 101.96.11.0
                         * 共4个
                         *----------------------
                         * 101.96.16.0/12 等于
                         * 101.96.16.0
                         * ...
                         * 101.96.31.0
                         * 共16个
                         * ---------------------
                         * 从上述数据中看到 101.96.11.0 到 101.96.16.0 出现了断层，中间内容不属于中国的IP
                         * 所以都需要被记录下来，多个IPRange 我们使用数组存储
                         */
                        // 转换IP为long
                        long start_ip = ipv4ToLong(strs[0]);
                        long ip_range = (long) Math.pow(2, addressCount);
                        long end_ip = start_ip + ip_range;
                        String ipRange = start_ip + "-" + end_ip;
                        // 判断是否已存在已有数据
                        JSONArray ipRangeExist = (JSONArray) object.get(String.valueOf(ip2));
                        if (ipRangeExist == null) {
                            ipRangeExist = new JSONArray();
                        }
                        ipRangeExist.add(ipRange);
                        object.put(String.valueOf(ip2), ipRangeExist);
                    }
                    ipMap.put(String.valueOf(ip1), object);
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
    public static boolean ipInChina(Map<String, Object> ipMap, String ip) {
        if (ipMap == null) {
            ipMap = getIpList();
        }
        // 判断 IP 是否存在
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        // 第一个IP端作为key
        String[] ipArr = ip.split("\\.");
        String key = ipArr[0];
        String childKey = ipArr[1];
        // 当前IP转换为整数
        long ip_long = ipv4ToLong(ip);

        // 判断第一个IP端存在
        if (ipMap.containsKey(key)) {
            JSONObject parentObj = (JSONObject) ipMap.get(key);
            // 判断第二个IP段是否存在
            if (parentObj.getString(childKey) != null) {
                String ipRange = parentObj.getString(childKey);
                if (ipRange.equals("all")) {
                    // 整个其余网段都是中国IP
                    return true;
                } else {
                    JSONArray ipRangeArray = JSONArray.parseArray(ipRange);
                    for (Object range : ipRangeArray) {
                        String[] ipRanges = String.valueOf(range).split("\\-");
                        if (ipRanges.length == 2) {
                            long ipRange_start = Long.parseLong(ipRanges[0]);
                            long ipRange_end = Long.parseLong(ipRanges[1]);
                            // 判断是否在范围内
                            return ip_long >= ipRange_start && ip_long <= ipRange_end;
                        }
                    }
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


    /**
     * 获取当前出口的IP
     *
     * @return
     */
    public static String getWebIp() {
        String ipHtml = "https://whatismyipaddress.com";
        String ip = "";
        try {
            Document document = Jsoup.connect(ipHtml).get();
            ip = document.getElementById("ipv4").text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }


    /**
     * ip2region
     *
     * @param ip
     * @return
     */
    public static String getCityInfo(String ip) {

        //db
        String path = IpUtils.class.getResource("/data/ip2region.db").getPath();
        File file = new File(path);

        if (!file.exists()) {
            log.error("Error: Invalid ip2region.db file");
        }

        //算法
        int algorithm = DbSearcher.BTREE_ALGORITHM;
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, path);

            // 方法
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
                default:
                    break;
            }

            DataBlock dataBlock = null;
            if (!Util.isIpAddress(ip)) {
                log.error("Error: Invalid ip address");
            }

            dataBlock = (DataBlock) method.invoke(searcher, ip);

            return dataBlock.getRegion();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {

    }

}
