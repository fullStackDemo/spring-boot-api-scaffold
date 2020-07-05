package com.scaffold.test.controller;

import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ws")
public class WebsocketController {

    // 向客户端发出消息
    @GetMapping("/push/{userId}")
    public Result sendToWeb(@RequestParam String message, @PathVariable String userId) throws IOException {
        WebSocketServer.sendInfo(message, userId);
        return ResultGenerator.setSuccessResult("发送成");
    }
}
