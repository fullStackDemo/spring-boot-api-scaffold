//package com.scaffold.test.controller;
//
//
//import com.scaffold.test.base.Result;
//import com.scaffold.test.base.ResultGenerator;
//import com.scaffold.test.entity.FxPair;
//import com.scaffold.test.entity.FxType;
//import com.scaffold.test.service.FxPairService;
//import com.scaffold.test.service.FxRateService;
//import com.scaffold.test.service.FxTypeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * <p>
// * 外汇牌价数据
// * </p>
// *
// * @author alex wong
// * @since 2020-09-04
// */
//@RestController
//@RequestMapping("/fxRate")
//public class FxRateController {
//
//
//    @Autowired
//    FxRateService fxRateService;
//
//    @Autowired
//    FxPairService fxPairService;
//
//    @Autowired
//    FxTypeService fxTypeService;
//
//
//    @GetMapping("read")
//    public void readExcel() {
//        fxRateService.readExcel();
//    }
//
//
//    /**
//     * 获取卖出货币数据List
//     *
//     * @return
//     */
//    @GetMapping("sell")
//    public Result getSellList() {
//        return ResultGenerator.setSuccessResult(fxPairService.getSellList());
//    }
//
//    /**
//     * 获取对应卖出货币数据
//     * @param sellCcy 卖出货币
//     * @return
//     */
//    @GetMapping("buy")
//    public Result getSellList(@RequestParam String sellCcy) {
//        FxPair fxPair = new FxPair();
//        fxPair.setSellCcy(sellCcy);
//        return ResultGenerator.setSuccessResult(fxPairService.getBuyList(fxPair));
//    }
//
//
//    /**
//     * 获取对应货币对报价类型数据
//     * @param ccyPair 货币对
//     * @return
//     */
//    @GetMapping("type")
//    public Result getCcyPairType(@RequestParam String ccyPair) {
//        FxType fxType = new FxType();
//        fxType.setCcyPair(ccyPair);
//        return ResultGenerator.setSuccessResult(fxTypeService.getCcyPairType(fxType));
//    }
//
//
//}
//
