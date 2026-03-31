package com.shisfish.news.web.controller.common;

import com.shisfish.news.common.constant.RedisConstants;
import com.shisfish.news.common.core.EhcacheUtil;
//import com.shisfish.common.core.redis.RedisService;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.utils.uuid.IdUtils;
import com.shisfish.news.framework.security.config.bean.LoginProperties;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 验证码操作处理
 * @Version: 1.0.0
 */
@Api(tags = "验证码接口")
@RestController
public class CaptchaController {

//    @Autowired
//    private RedisService redisService;

    @Autowired
    private EhcacheUtil ehcacheUtil;

    @Autowired
    private LoginProperties loginProperties;

    @ApiOperation(value = "生成验证码", notes = "生成验证码详情")
    @GetMapping("/code/img")
    public Result<HashMap<String, String>> getCodeImg() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        // 唯一标识
        String uuid = IdUtils.simpleUUID();
        String verifyKey = RedisConstants.CAPTCHA_CODE_KEY + uuid;
        // 保存到redis
        ehcacheUtil.setCaptcha(verifyKey, captcha.text());
//        redisService.setCacheObject(verifyKey, captcha.text(), loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        return ResultUtils.success(new HashMap<String, String>() {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }});
    }
}
