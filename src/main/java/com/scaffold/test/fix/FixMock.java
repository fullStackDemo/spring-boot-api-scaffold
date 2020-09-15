package com.scaffold.test.fix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SocketAcceptor;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

@Component
@Slf4j
public class FixMock {

    @Resource
    private SocketAcceptor socketAcceptor;

    @Scheduled(cron = "0/10 * * * * ?")
    public void executeReport() throws Exception {
        if (Objects.isNull(socketAcceptor)) {
            return;
        }
        log.error("mock推送......");
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
            log.trace(sessionID.toString());
            Session session = Session.lookupSession(sessionID);
            if (Objects.isNull(session)) {
                return;
            }
            if (session.isLoggedOn()) {
                Session.sendToTarget(report, sessionID);
            }
        }
    }


}
