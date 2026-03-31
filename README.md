# 数据库文档

**数据库名：** news

**文档版本：** 1.0-SNAPSHOT

**文档描述：** 数据库文档生成

| 表名                  | 说明       |
| :---: | :---: |
| [news_collect](#news_collect) | 新闻收藏表 |
| [news_comment](#news_comment) | 新闻评论表 |
| [link](#link) | 友情链接表 |
| [news](#news) | 新闻表 |
| [news_type](#news_type) | 新闻栏目表 |
| [news_user_comment](#news_user_comment) | 用户评论中间表 |
| [sys_config](#sys_config) | 参数配置表 |
| [sys_dept](#sys_dept) | 部门表 |
| [sys_dict_data](#sys_dict_data) | 字典数据表 |
| [sys_dict_type](#sys_dict_type) | 字典类型表 |
| [sys_logininfo](#sys_logininfo) | 系统访问记录 |
| [sys_menu](#sys_menu) | 菜单权限表 |
| [sys_notice](#sys_notice) | 通知公告表 |
| [sys_oper_log](#sys_oper_log) | 操作日志记录 |
| [sys_role](#sys_role) | 角色信息表 |
| [sys_role_dept](#sys_role_dept) | 角色和部门关联表 |
| [sys_role_menu](#sys_role_menu) | 角色和菜单关联表 |
| [sys_user](#sys_user) | 用户信息表 |
| [sys_user_role](#sys_user_role) | 用户和角色关联表 |

**表名：** <a id="news_collect">news_collect</a>

**说明：** 新闻收藏表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | collection_id |   bigint   | 20 |   0    |    N     |  Y   |       | 收藏id  |
|  2   | news_id |   bigint   | 20 |   0    |    N     |  N   |   -1    | 新闻id  |
|  3   | user_id |   bigint   | 20 |   0    |    N     |  N   |   -1    | 用户id  |
|  4   | collect_time |   datetime   | 19 |   0    |    Y     |  N   |   CURRENT_TIMESTAMP    | 收藏时间  |

**表名：** <a id="news_comment">news_comment</a>

**说明：** 新闻评论表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | comment_id |   bigint   | 20 |   0    |    N     |  Y   |       | 新闻评论id  |
|  2   | news_id |   bigint   | 20 |   0    |    N     |  N   |   0    | 新闻id  |
|  3   | comment_content |   text   | 65535 |   0    |    Y     |  N   |       | 新闻评论内容  |
|  4   | thumbs |   bigint   | 20 |   0    |    Y     |  N   |   0    | 点赞数  |
|  5   | del_flag |   char   | 1 |   0    |    Y     |  N   |   0    | 删除标志（0代表存在2代表删除）  |
|  6   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  7   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  8   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  9   | update_time |   date   | 10 |   0    |    Y     |  N   |       | 更新时间  |
|  10   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="link">link</a>

**说明：** 友情链接表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   bigint   | 20 |   0    |    N     |  Y   |       | 友情链接id  |
|  2   | link_name |   varchar   | 63 |   0    |    N     |  N   |       | 友情链接名称  |
|  3   | link_url |   varchar   | 255 |   0    |    N     |  N   |       | 友情链接地址  |
|  4   | email |   varchar   | 63 |   0    |    N     |  N   |       | 联系人邮件  |
|  5   | order_num |   int   | 10 |   0    |    N     |  N   |   0    | 链接显示顺序  |
|  6   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  7   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  8   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  9   | update_time |   timestamp   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  10   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="news">news</a>

**说明：** 新闻表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | news_id |   bigint   | 20 |   0    |    N     |  Y   |       | 新闻id  |
|  2   | user_id |   bigint   | 20 |   0    |    N     |  N   |       | 作者id  |
|  3   | news_type_id |   bigint   | 20 |   0    |    N     |  N   |       | 新闻栏目id  |
|  4   | news_title |   varchar   | 255 |   0    |    N     |  N   |       | 新闻标题  |
|  5   | news_content |   longtext   | 2147483647 |   0    |    N     |  N   |       | 新闻内容  |
|  6   | news_attr |   char   | 1 |   0    |    N     |  N   |   0    | 新闻属性0.头条区新闻1.幻灯片区新闻2.热点区新闻  |
|  7   | news_image |   varchar   | 255 |   0    |    Y     |  N   |       | 新闻封面  |
|  8   | order_num |   int   | 10 |   0    |    Y     |  N   |   0    | 新闻显示顺序  |
|  9   | is_public |   char   | 1 |   0    |    Y     |  N   |   0    | 新闻是否公开（0.公开1.私有）  |
|  10   | thumbs |   bigint   | 20 |   0    |    Y     |  N   |   0    | 新闻点赞数  |
|  11   | visits |   bigint   | 20 |   0    |    Y     |  N   |   0    | 新闻浏览量  |
|  12   | comments |   bigint   | 20 |   0    |    Y     |  N   |   0    | 新闻评论数  |
|  13   | collects |   bigint   | 20 |   0    |    Y     |  N   |   0    | 新闻收藏数  |
|  14   | news_source_author |   varchar   | 63 |   0    |    Y     |  N   |       | 新闻来源原创作者(爬虫使用)  |
|  15   | news_source_tags |   varchar   | 127 |   0    |    Y     |  N   |       | 新闻博客分类标签  |
|  16   | news_source |   varchar   | 511 |   0    |    Y     |  N   |       | 新闻来源(爬虫使用)  |
|  17   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 审核状态（0.审核中1.审核成功2.审核失败）  |
|  18   | del_flag |   char   | 1 |   0    |    Y     |  N   |   0    | 删除标志（0代表存在2代表删除）  |
|  19   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  20   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  21   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  22   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  23   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="news_type">news_type</a>

**说明：** 新闻栏目表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | news_type_id |   bigint   | 20 |   0    |    N     |  Y   |       | 新闻栏目id  |
|  2   | news_type_name |   varchar   | 63 |   0    |    N     |  N   |       | 新闻栏目名称  |
|  3   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 新闻栏目状态（0正常1关闭）  |
|  4   | order_num |   int   | 10 |   0    |    N     |  N   |   0    | 显示顺序  |
|  5   | del_flag |   char   | 1 |   0    |    Y     |  N   |   0    | 删除标志（0代表存在2代表删除）  |
|  6   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  7   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  8   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  9   | update_time |   timestamp   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  10   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="news_user_comment">news_user_comment</a>

**说明：** 用户评论中间表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   bigint   | 20 |   0    |    N     |  Y   |       | 主键id  |
|  2   | user_id |   bigint   | 20 |   0    |    N     |  N   |       | 用户id  |
|  3   | comment_id |   bigint   | 20 |   0    |    N     |  N   |       | 评论id  |

**表名：** <a id="sys_config">sys_config</a>

**说明：** 参数配置表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | config_id |   int   | 10 |   0    |    N     |  Y   |       | 参数主键  |
|  2   | config_name |   varchar   | 127 |   0    |    Y     |  N   |       | 参数名称  |
|  3   | config_key |   varchar   | 127 |   0    |    Y     |  N   |       | 参数键名  |
|  4   | config_value |   varchar   | 127 |   0    |    Y     |  N   |       | 参数键值  |
|  5   | config_type |   char   | 1 |   0    |    Y     |  N   |   N    | 系统内置（Y是N否）  |
|  6   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  7   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  8   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  9   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  10   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_dept">sys_dept</a>

**说明：** 部门表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | dept_id |   bigint   | 20 |   0    |    N     |  Y   |       | 部门id  |
|  2   | parent_id |   bigint   | 20 |   0    |    Y     |  N   |   0    | 父部门id  |
|  3   | ancestors |   varchar   | 63 |   0    |    Y     |  N   |       | 祖级列表  |
|  4   | dept_name |   varchar   | 31 |   0    |    Y     |  N   |       | 部门名称  |
|  5   | order_num |   int   | 10 |   0    |    Y     |  N   |   0    | 显示顺序  |
|  6   | leader |   varchar   | 31 |   0    |    Y     |  N   |       | 负责人  |
|  7   | phone |   varchar   | 11 |   0    |    Y     |  N   |       | 联系电话  |
|  8   | email |   varchar   | 63 |   0    |    Y     |  N   |       | 邮箱  |
|  9   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 部门状态（0正常1停用）  |
|  10   | del_flag |   char   | 1 |   0    |    Y     |  N   |   0    | 删除标志（0代表存在2代表删除）  |
|  11   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  12   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  13   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  14   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  15   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_dict_data">sys_dict_data</a>

**说明：** 字典数据表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | dict_code |   bigint   | 20 |   0    |    N     |  Y   |       | 字典编码  |
|  2   | dict_sort |   int   | 10 |   0    |    Y     |  N   |   0    | 字典排序  |
|  3   | dict_label |   varchar   | 127 |   0    |    Y     |  N   |       | 字典标签  |
|  4   | dict_value |   varchar   | 127 |   0    |    Y     |  N   |       | 字典键值  |
|  5   | dict_type |   varchar   | 127 |   0    |    Y     |  N   |       | 字典类型  |
|  6   | css_class |   varchar   | 127 |   0    |    Y     |  N   |       | 样式属性（其他样式扩展）  |
|  7   | list_class |   varchar   | 127 |   0    |    Y     |  N   |       | 表格回显样式  |
|  8   | is_default |   char   | 1 |   0    |    Y     |  N   |   N    | 是否默认（Y是N否）  |
|  9   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 状态（0正常1停用）  |
|  10   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  11   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  12   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  13   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  14   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_dict_type">sys_dict_type</a>

**说明：** 字典类型表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | dict_id |   bigint   | 20 |   0    |    N     |  Y   |       | 字典主键  |
|  2   | dict_name |   varchar   | 127 |   0    |    Y     |  N   |       | 字典名称  |
|  3   | dict_type |   varchar   | 127 |   0    |    Y     |  N   |       | 字典类型  |
|  4   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 状态（0正常1停用）  |
|  5   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  6   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  7   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  8   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  9   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_logininfo">sys_logininfo</a>

**说明：** 系统访问记录

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | info_id |   bigint   | 20 |   0    |    N     |  Y   |       | 访问ID  |
|  2   | username |   varchar   | 63 |   0    |    Y     |  N   |       | 用户账号  |
|  3   | ipaddr |   varchar   | 63 |   0    |    Y     |  N   |       | 登录IP地址  |
|  4   | login_location |   varchar   | 255 |   0    |    Y     |  N   |       | 登录地点  |
|  5   | browser |   varchar   | 63 |   0    |    Y     |  N   |       | 浏览器类型  |
|  6   | os |   varchar   | 63 |   0    |    Y     |  N   |       | 操作系统  |
|  7   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 登录状态（0成功1失败）  |
|  8   | msg |   varchar   | 255 |   0    |    Y     |  N   |       | 提示消息  |
|  9   | login_time |   timestamp   | 19 |   0    |    Y     |  N   |   CURRENT_TIMESTAMP    | 访问时间  |

**表名：** <a id="sys_menu">sys_menu</a>

**说明：** 菜单权限表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | menu_id |   bigint   | 20 |   0    |    N     |  Y   |       | 菜单ID  |
|  2   | menu_name |   varchar   | 63 |   0    |    N     |  N   |       | 菜单名称  |
|  3   | parent_id |   bigint   | 20 |   0    |    Y     |  N   |   0    | 父菜单ID  |
|  4   | order_num |   int   | 10 |   0    |    Y     |  N   |   0    | 显示顺序  |
|  5   | path |   varchar   | 255 |   0    |    Y     |  N   |       | 路由地址  |
|  6   | component |   varchar   | 255 |   0    |    Y     |  N   |       | 组件路径  |
|  7   | is_frame |   int   | 10 |   0    |    Y     |  N   |   1    | 是否为外链（0是1否）  |
|  8   | menu_type |   char   | 1 |   0    |    Y     |  N   |       | 菜单类型（M目录C菜单F按钮）  |
|  9   | visible |   char   | 1 |   0    |    Y     |  N   |   0    | 菜单状态（0显示1隐藏）  |
|  10   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 菜单状态（0正常1停用）  |
|  11   | perms |   varchar   | 127 |   0    |    Y     |  N   |       | 权限标识  |
|  12   | icon |   varchar   | 127 |   0    |    Y     |  N   |   #    | 菜单图标  |
|  13   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  14   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  15   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  16   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  17   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_notice">sys_notice</a>

**说明：** 通知公告表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | notice_id |   int   | 10 |   0    |    N     |  Y   |       | 公告ID  |
|  2   | notice_title |   varchar   | 63 |   0    |    N     |  N   |       | 公告标题  |
|  3   | notice_type |   char   | 1 |   0    |    N     |  N   |       | 公告类型（1通知2公告）  |
|  4   | notice_content |   varchar   | 2047 |   0    |    Y     |  N   |       | 公告内容  |
|  5   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 公告状态（0正常1关闭）  |
|  6   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  7   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  8   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  9   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  10   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_oper_log">sys_oper_log</a>

**说明：** 操作日志记录

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | oper_id |   bigint   | 20 |   0    |    N     |  Y   |       | 日志主键  |
|  2   | title |   varchar   | 63 |   0    |    Y     |  N   |       | 模块标题  |
|  3   | business_type |   int   | 10 |   0    |    Y     |  N   |   0    | 业务类型（0其它1新增2修改3删除）  |
|  4   | method |   varchar   | 127 |   0    |    Y     |  N   |       | 方法名称  |
|  5   | request_method |   varchar   | 15 |   0    |    Y     |  N   |       | 请求方式  |
|  6   | operator_type |   int   | 10 |   0    |    Y     |  N   |   0    | 操作类别（0其它1后台用户2手机端用户）  |
|  7   | oper_name |   varchar   | 63 |   0    |    Y     |  N   |       | 操作人员  |
|  8   | dept_name |   varchar   | 63 |   0    |    Y     |  N   |       | 部门名称  |
|  9   | oper_url |   varchar   | 255 |   0    |    Y     |  N   |       | 请求URL  |
|  10   | oper_ip |   varchar   | 63 |   0    |    Y     |  N   |       | 主机地址  |
|  11   | oper_location |   varchar   | 255 |   0    |    Y     |  N   |       | 操作地点  |
|  12   | oper_param |   varchar   | 2047 |   0    |    Y     |  N   |       | 请求参数  |
|  13   | json_result |   varchar   | 2047 |   0    |    Y     |  N   |       | 返回参数  |
|  14   | status |   int   | 10 |   0    |    Y     |  N   |   0    | 操作状态（0正常1异常）  |
|  15   | error_msg |   varchar   | 2047 |   0    |    Y     |  N   |       | 错误消息  |
|  16   | oper_time |   timestamp   | 19 |   0    |    Y     |  N   |   CURRENT_TIMESTAMP    | 操作时间  |

**表名：** <a id="sys_role">sys_role</a>

**说明：** 角色信息表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | role_id |   bigint   | 20 |   0    |    N     |  Y   |       | 角色ID  |
|  2   | role_name |   varchar   | 31 |   0    |    N     |  N   |       | 角色名称  |
|  3   | role_key |   varchar   | 127 |   0    |    N     |  N   |       | 角色权限字符串  |
|  4   | role_sort |   int   | 10 |   0    |    N     |  N   |       | 显示顺序  |
|  5   | data_scope |   char   | 1 |   0    |    Y     |  N   |   1    | 数据范围（1：全部数据权限2：自定数据权限3：本部门数据权限4：本部门及以下数据权限）  |
|  6   | status |   char   | 1 |   0    |    N     |  N   |   0    | 角色状态（0正常1停用）  |
|  7   | del_flag |   char   | 1 |   0    |    Y     |  N   |   0    | 删除标志（0代表存在2代表删除）  |
|  8   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  9   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  10   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  11   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  12   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_role_dept">sys_role_dept</a>

**说明：** 角色和部门关联表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   bigint   | 20 |   0    |    N     |  Y   |       | 主键id  |
|  2   | role_id |   bigint   | 20 |   0    |    N     |  N   |       | 角色ID  |
|  3   | dept_id |   bigint   | 20 |   0    |    N     |  N   |       | 部门ID  |

**表名：** <a id="sys_role_menu">sys_role_menu</a>

**说明：** 角色和菜单关联表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   int   | 10 |   0    |    N     |  Y   |       | 主键ID  |
|  2   | role_id |   bigint   | 20 |   0    |    N     |  N   |       | 角色ID  |
|  3   | menu_id |   bigint   | 20 |   0    |    N     |  N   |       | 菜单ID  |

**表名：** <a id="sys_user">sys_user</a>

**说明：** 用户信息表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | user_id |   bigint   | 20 |   0    |    N     |  Y   |       | 用户ID  |
|  2   | dept_id |   bigint   | 20 |   0    |    Y     |  N   |       | 部门ID  |
|  3   | username |   varchar   | 31 |   0    |    N     |  N   |       | 用户账号  |
|  4   | nick_name |   varchar   | 31 |   0    |    N     |  N   |       | 用户昵称  |
|  5   | user_type |   varchar   | 2 |   0    |    Y     |  N   |   00    | 用户类型（00系统用户）  |
|  6   | email |   varchar   | 63 |   0    |    Y     |  N   |       | 用户邮箱  |
|  7   | phone |   varchar   | 11 |   0    |    Y     |  N   |       | 手机号码  |
|  8   | sex |   char   | 1 |   0    |    Y     |  N   |   0    | 用户性别（0男1女2未知）  |
|  9   | avatar |   varchar   | 255 |   0    |    Y     |  N   |       | 头像地址  |
|  10   | password |   varchar   | 127 |   0    |    Y     |  N   |       | 密码  |
|  11   | status |   char   | 1 |   0    |    Y     |  N   |   0    | 帐号状态（0正常1停用）  |
|  12   | del_flag |   char   | 1 |   0    |    Y     |  N   |   0    | 删除标志（0代表存在2代表删除）  |
|  13   | login_ip |   varchar   | 63 |   0    |    Y     |  N   |       | 最后登录IP  |
|  14   | login_date |   datetime   | 19 |   0    |    Y     |  N   |       | 最后登录时间  |
|  15   | create_by |   varchar   | 63 |   0    |    Y     |  N   |       | 创建者  |
|  16   | create_time |   datetime   | 19 |   0    |    Y     |  N   |       | 创建时间  |
|  17   | update_by |   varchar   | 63 |   0    |    Y     |  N   |       | 更新者  |
|  18   | update_time |   datetime   | 19 |   0    |    Y     |  N   |       | 更新时间  |
|  19   | remark |   varchar   | 511 |   0    |    Y     |  N   |       | 备注  |

**表名：** <a id="sys_user_role">sys_user_role</a>

**说明：** 用户和角色关联表

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   bigint   | 20 |   0    |    N     |  Y   |       | 主键id  |
|  2   | user_id |   bigint   | 20 |   0    |    N     |  N   |       | 用户ID  |
|  3   | role_id |   bigint   | 20 |   0    |    N     |  N   |       | 角色ID  |
