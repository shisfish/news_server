package com.shisfish.news.dao.viewobject.system;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 路由配置信息
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    private MetaVo meta;

    /**
     * 子路由
     */
    private List<RouterVo> children;

    public String getName() {
        return name;
    }

    public RouterVo setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public RouterVo setPath(String path) {
        this.path = path;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public RouterVo setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public String getRedirect() {
        return redirect;
    }

    public RouterVo setRedirect(String redirect) {
        this.redirect = redirect;
        return this;
    }

    public String getComponent() {
        return component;
    }

    public RouterVo setComponent(String component) {
        this.component = component;
        return this;
    }

    public Boolean getAlwaysShow() {
        return alwaysShow;
    }

    public RouterVo setAlwaysShow(Boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    public MetaVo getMeta() {
        return meta;
    }

    public RouterVo setMeta(MetaVo meta) {
        this.meta = meta;
        return this;
    }

    public List<RouterVo> getChildren() {
        return children;
    }

    public RouterVo setChildren(List<RouterVo> children) {
        this.children = children;
        return this;
    }
}
