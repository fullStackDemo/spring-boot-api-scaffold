package com.scaffold.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.FxPair;
import com.scaffold.test.entity.FxRate;
import com.scaffold.test.entity.FxType;
import com.scaffold.test.mapper.FxPairMapper;
import com.scaffold.test.mapper.FxRateMapper;
import com.scaffold.test.mapper.FxTypeMapper;
import com.scaffold.test.service.FxRateService;
import com.scaffold.test.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-09-04
 */

@Slf4j
@Service
public class FxRateServiceImpl extends ServiceImpl<FxRateMapper, FxRate> implements FxRateService {

    @Autowired
    FxRateMapper fxRateMapper;

    @Autowired
    FxTypeMapper fxTypeMapper;

    @Autowired
    FxPairMapper fxPairMapper;

    @Override
    public void readExcel() {
        // 读取文件夹里的文件
        File folder = new File("F:/spark");
        File folderOut = new File("F:/sparkOut");
        if (SystemUtils.isWindow() && !folderOut.exists()) {
            folderOut.mkdirs();
        }
        File[] files = folder.listFiles();
        for (File file : files) {
            // 获取货币对和报价品种
            String fileName = file.getName();
            String[] fileNameArr = fileName.split("\\.");
            String ccyPair = fileNameArr[0];
            String sellCcy = ccyPair.substring(0, 3);
            String buyCcy = ccyPair.substring(3, 6);
            String ccyType = fileNameArr[1];
            // 插入数据库
            FxType fxType = new FxType();
            fxType.setCcyType(ccyType);
            fxType.setCcyPair(ccyPair);
            fxTypeMapper.insertType(fxType);
            FxPair fxPair = new FxPair();
            fxPair.setSellCcy(sellCcy);
            fxPair.setBuyCcy(buyCcy);
            fxPair.setCcyPair(ccyPair);
            fxPairMapper.insertPair(fxPair);

            // 数据解析过程
            List<FxRate> rateList = new ArrayList<>();
            // CSV数据
            List<String> dataList = new ArrayList<>();
            BufferedReader bufferedReader = null;
            // 解析CSV文件
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    dataList.add(line);
                }
                // 无数据
                if (dataList.size() == 0) {
                    return;
                }
                // 判断CSV数据
                // 获取头部数据
                String[] headerData = dataList.get(0).split(",");
                String sparkTimestamp = "SparkTimestamp";
                String bestBidPrice = "BestBidPrice";
                String bestAskPrice = "BestAskPrice";
                int sparkTimestampIndex = 1;
                int bestBidPriceIndex = 5;
                int bestAskPriceIndex = 6;
                // 需要判断索引是否正确
                for (int i = 0; i < headerData.length; i++) {
                    String name = headerData[i];
                    if (name.equals(sparkTimestamp)) {
                        sparkTimestampIndex = i;
                    } else if (name.equals(bestBidPrice)) {
                        bestAskPriceIndex = i;
                    } else if (name.equals(bestAskPrice)) {
                        bestAskPriceIndex = i;
                    }
                }

                // 遍历数据
                for (int i = 0; i < dataList.size(); i++) {
                    if (i > 0) {
                        String[] currentLineData = dataList.get(i).split(",");
                        FxRate fxRate = new FxRate();
                        fxRate.setSource("spark");
                        // 获取货币类型和买卖货币
                        if (fileNameArr.length > 0) {
                            fxRate.setSellCcy(sellCcy);
                            fxRate.setBuyCcy(buyCcy);
                            fxRate.setCcyType(ccyType);
                            fxRate.setCcyPair(ccyPair);
                        }
                        if (currentLineData.length > 0) {
                            fxRate.setRateDate(currentLineData[sparkTimestampIndex]);
                            double bidPrice = Double.parseDouble(currentLineData[bestBidPriceIndex]);
                            double askPrice = Double.parseDouble(currentLineData[bestAskPriceIndex]);
                            fxRate.setRate((bidPrice + askPrice) / 2);
                        }
                        fxRate.setFlag(fxRate.getCcyPair() + fxRate.getCcyType() + fxRate.getRateDate());
                        rateList.add(fxRate);
                    }
                }

                // 入库
                for (FxRate data : rateList) {
                    fxRateMapper.insertRate(data);
                }

                // 删除已解析过的文件, 移除到其他文件夹
//                try {
//                    String outPath = file.getPath().replace("spark", "sparkOut");
//                    File outFile = new File(outPath);
//                    FileOutputStream fileOutputStream = new FileOutputStream(outFile);
//                    if (!outFile.exists()) {
//                        int a;
//                        byte[] b = new byte[1024];
//                        while ((a = fileInputStream.read(b)) != 1) {
//                            fileOutputStream.write(a);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        // 读取结束，删除文件
//                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
