package com.shisfish.news.web.controller.monitor;

//import com.shisfish.common.core.redis.RedisService;
import com.shisfish.news.common.lang.annotation.Log;
import com.shisfish.news.common.lang.enums.BusinessType;
import com.shisfish.news.common.result.Result;
import com.shisfish.news.common.result.ResultUtils;
import com.shisfish.news.common.result.code.ResponseCode;
        import com.shisfish.news.service.service.system.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

        import java.util.HashMap;
        import java.util.Map;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 在线用户监控
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController {

    @Autowired
    private ISysUserOnlineService userOnlineService;

//    @Autowired
//    private RedisService redisService;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public Map<String, Object> list(String ipaddr, String userName) {
        // 拿出所有当前登录用户token标识
//        Collection<String> keys = redisService.keys(RedisConstants.LOGIN_TOKEN_KEY + "*");
//        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
//        for (String key : keys) {
//            LoginUser user = redisService.getCacheObject(key);
//            // 先判断传参是否为空
//            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
//                // 通过登录地址/用户名查询信息
//                if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
//                    userOnlineList.add(userOnlineService.findOnlineByInfo(ipaddr, userName, user));
//                }
//            } else if (StringUtils.isNotEmpty(ipaddr)) {
//                // 通过登录地址查询信息
//                if (StringUtils.equals(ipaddr, user.getIpaddr())) {
//                    userOnlineList.add(userOnlineService.findOnlineByIpaddr(ipaddr, user));
//                }
//            } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser())) {
//                // 通过用户名查询信息
//                if (StringUtils.equals(userName, user.getUsername())) {
//                    userOnlineList.add(userOnlineService.findOnlineByUserName(userName, user));
//                }
//            } else {
//                userOnlineList.add(userOnlineService.setLoginUserToUserOnline(user));
//            }
//        }
//        Collections.reverse(userOnlineList);
//        userOnlineList.removeAll(Collections.singleton(null));
//        return new HashMap<String, Object>() {{
//            put("total", userOnlineList.size());
//            put("code", ResponseCode.OK.getCode());
//            put("rows", userOnlineList);
//        }};

        return new HashMap<String, Object>() {{
            put("total", 0);
            put("code", ResponseCode.OK.getCode());
            put("rows", null);
        }};
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tokenId}")
    public Result<Object> forceLogout(@PathVariable String tokenId) {
//        redisService.deleteObject(RedisConstants.LOGIN_TOKEN_KEY + tokenId);
        return ResultUtils.success();
    }
}
