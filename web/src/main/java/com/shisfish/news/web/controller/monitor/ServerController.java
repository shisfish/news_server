package com.shisfish.news.web.controller.monitor;

import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.framework.web.domain.Server;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 服务器监控
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public Result<Object> getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return ResultUtils.success(server);
    }
}
