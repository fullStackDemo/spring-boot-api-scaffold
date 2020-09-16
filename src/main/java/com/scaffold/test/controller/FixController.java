package com.scaffold.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SocketAcceptor;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.TestRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Fix 控制器
 */

@Slf4j
@RestController
@RequestMapping("fix")
public class FixController {

    @Resource
    private SocketAcceptor socketAcceptor;

    /**
     * 执行报告
     *
     * @throws SessionNotFound
     */
    @GetMapping("executeReport")
    public void executeReport() throws SessionNotFound {
        if (socketAcceptor == null) {
            return;
        }
        log.error("test推送......");
        ExecutionReport report = new ExecutionReport();
        report.set(new OrderID("22554912"));
        report.set(new ExecID("22554912"));
        report.set(new ExecType(ExecType.TRADE_CANCEL));
        report.set(new OrdStatus(OrdStatus.CANCELED));
        report.set(new Account("2121"));
        report.set(new Symbol("USD"));
        report.set(new SecurityExchange("Exchange"));
        report.set(new Side(Side.BUY));
        report.set(new OrderQty(112));
        report.set(new LeavesQty(0));
        report.set(new CumQty(1));
        report.set(new AvgPx(112));
        report.set(new Text("时间:" + System.currentTimeMillis()));
        ArrayList<SessionID> sessions = socketAcceptor.getSessions();
        for (SessionID sessionID : sessions) {
            Session session = Session.lookupSession(sessionID);
            if (Objects.isNull(session)) {
                return;
            }
            if (session.isLoggedOn()) {
                Session.sendToTarget(report, sessionID);
            }
        }
    }

    /**
     * 测试请求
     * @throws Exception
     */
    @GetMapping("testRequest")
    public void testRequest() throws Exception {
        TestRequest testRequest = new TestRequest();
        testRequest.set(new TestReqID("6666"));
        ArrayList<SessionID> sessions = socketAcceptor.getSessions();
        for (SessionID sessionID : sessions) {
            log.error(sessionID.toString());
            Session session = Session.lookupSession(sessionID);
            if (Objects.isNull(session)) {
                return;
            }
            if (session.isLoggedOn()) {
                Session.sendToTarget(testRequest, sessionID);
            }
        }
    }

    /**
     * 订单请求
     * @throws Exception
     */
    @GetMapping("newOrderSingle")
    public void newOrderSingle() throws Exception {
        NewOrderSingle newOrderSingle = new NewOrderSingle();
        newOrderSingle.set(new ClOrdID("11"));
        newOrderSingle.set(new OrderQty(2000));
        newOrderSingle.set(new Symbol("USD"));
        newOrderSingle.set(new OrdType(OrdType.FOREX_MARKET));
        newOrderSingle.set(new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
        newOrderSingle.set(new TransactTime());

        ArrayList<SessionID> sessions = socketAcceptor.getSessions();
        for (SessionID sessionID : sessions) {
            log.error(sessionID.toString());
            Session session = Session.lookupSession(sessionID);
            if (Objects.isNull(session)) {
                return;
            }
            if (session.isLoggedOn()) {
                Session.sendToTarget(newOrderSingle, sessionID);
            }
        }
    }


}
