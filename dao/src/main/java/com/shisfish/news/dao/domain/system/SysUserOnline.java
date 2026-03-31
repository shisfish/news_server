package com.shisfish.news.dao.domain.system;


/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 当前在线会话
 * @Version: 1.0.0
 */
public class SysUserOnline {
    /**
     * 会话编号
     */
    private String tokenId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private Long loginTime;

    public String getTokenId() {
        return tokenId;
    }

    public SysUserOnline setTokenId(String tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public String getDeptName() {
        return deptName;
    }

    public SysUserOnline setDeptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SysUserOnline setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public SysUserOnline setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
        return this;
    }

    public String getLoginLocation() {
        return loginLocation;
    }

    public SysUserOnline setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
        return this;
    }

    public String getBrowser() {
        return browser;
    }

    public SysUserOnline setBrowser(String browser) {
        this.browser = browser;
        return this;
    }

    public String getOs() {
        return os;
    }

    public SysUserOnline setOs(String os) {
        this.os = os;
        return this;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public SysUserOnline setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
        return this;
    }
}
