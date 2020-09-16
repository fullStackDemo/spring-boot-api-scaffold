package com.scaffold.test.fix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.Text;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.MessageCracker;
import quickfix.fix44.NewOrderSingle;

/**
 * <p></p>
 *
 * @author chenxjuc
 * @date 2020/4/27
 */
@Slf4j
@Component
public class FixMessageCracker extends MessageCracker {


    @Override
    public void onMessage(Heartbeat order, SessionID sessionID) {
        log.info("[心跳消息]{}, message={} ", sessionID, order);
    }

    @Override
    public void onMessage(ExecutionReport report, SessionID sessionID) {
        log.info("[执行报告]{}, message={} ", sessionID, report);
    }

    @Override
    public void onMessage(NewOrderSingle order, SessionID sessionID) {
        log.info("[创建订单]{}, message={} ", sessionID, order);
        // 转发到360t
        try {
            Session.sendToTarget(order, "SINOIFSG_TI_TEST", "360T_TI_TEST");
            // TODO 需要删除 模拟响应数据给客户端
            order.setField(new Text("response data..."));
            Session.sendToTarget(order, sessionID);
        } catch (SessionNotFound sessionNotFound) {
            sessionNotFound.printStackTrace();
        }
    }

}
