# EcologyInitTemp

# 泛微EC9二次开发示例
EC9技术站: [https://e-cloudstore.com/e9/index0.html](https://e-cloudstore.com/e9/index0.html)  
后端开发环境搭建:  [https://e-cloudstore.com/doc.html?appId=c6a9ae6e47b74d4da04c935ed51d177a](https://e-cloudstore.com/doc.html?appId=c6a9ae6e47b74d4da04c935ed51d177a)  
前端开发(_ecode代码编辑器_): [https://e-cloudstore.com/doc.html](https://e-cloudstore.com/doc.html)

_**两大需求场景:**_  
**流程表单前端控制**    直达[https://e-cloudstore.com/doc.html?appId=98cb7a20fae34aa3a7e3a3381dd8764e](https://e-cloudstore.com/doc.html?appId=98cb7a20fae34aa3a7e3a3381dd8764e)   
**异构系统调用EC9接口** 直达[https://e-cloudstore.com/ec/api/applist/index.html](https://e-cloudstore.com/ec/api/applist/index.html)

_**其他参考资料**_
EC9.0支持的可配置化的短信接口[查看](https://l1utaihong.gitee.io/custom/doc/SMS_INTERFACE.html)   
泛微OA产品部署HTTPS[查看](http://note.youdao.com/s/L8Qg8BFk)  
OA服务器运维脚本[查看](http://note.youdao.com/s/JhfblPlf)  
页面跳转支持传递登录人相关信息[查看](http://note.youdao.com/s/QYzRL8aK)

>#### 三、开发环境搭建
> ###### 1.通过git导入到本地开发工具中
* 提供git导入的方式创建
* 从零搭建，也只需要新建一个空的web项目即可，不需要引入任何框架。
* ###### 2.设置web目录
    建议在com.engine在设置自定义的项目目录
###### 3.拷贝必要的jar包依赖
    这个很重要，需要有客户环境的KB补丁包保持一致
- 进入服务器上的/weaver/ecology/classbean目录，执行jar -cvf local-ecology.jar ./  生成对应的jar包,将jar包拷贝到本地项目中
- 拷贝服务器上的/weaver/ecology/WEB-INF/lib目录本地项目中
- 需要额外引入Junit4的jar包才能支持单元测试
- 为了获得更好的支持建议每次更新eocde补丁包后需要拷贝ecode安装包 ecology_dev.zip里面 WEB-INF/lib下的jar包到JAVA工程中去
###### 4.拷贝必要的配置文件
- 需要拷贝服务器上的配置文件到/web/WEB-INF/prop目录目录下，包含但不限于有:
```
/weaver/ecology/WEB-INF/prop/ecustom.properties  
/weaver/ecology/WEB-INF/prop/initCache.properties  
/weaver/ecology/WEB-INF/prop/weaver.properties  
/weaver/ecology/WEB-INF/prop/weaver_enableMultiLangEncoding_whiteList.properties  
/weaver/ecology/WEB-INF/prop/weaver_enableMultiLangEncoding_whiteList_new.properties  
/weaver/ecology/WEB-INF/prop/weaver_rtx.properties  
```
- 需要拷贝服务器上/weaver/ecology/WEB-INF/config/escache.xml到 custom/web/WEB-INF/prop/目录下
###### 5.拷贝并设置本地的数据库链接文件
本地数据库连接文件依旧为weaver.properties,具体目录在 custom/web/WEB-INF/prop/下面。确保和需要调试的服务器保持一致即可。
###### 6.OA自定义日志输出路径
修改配置文件文件路径：/weaver/ecology/WEB-INF/log4jinit.properties，在最后面增加一下内容
#自定义开发日志文件
log4j.logger.autoLinkLog=INFO,ERROR,cusLog
log4j.appender.autoLinkLog=org.apache.log4j.DailyRollingFileAppender
log4j.appender.autoLinkLog.DatePattern='_'yyyyMMdd'.log'
#@custom为日志目录名称，cusLog.log为日志名称
log4j.appender.autoLinkLog.File=@cusLog/cusLog.log
log4j.appender.autoLinkLog.layout=org.apache.log4j.PatternLayout
log4j.appender.autoLinkLog.layout.ConversionPattern=%d{yyyy-MM-dd HH\:mm\:ss,SSS} %-5p [Thread\:%t] %m%n
log4j.additivity.autoLinkLog=false

###### 7.部署至客户环境
- out/custom 目录，执行 jar -cvf solelyr-ecology.1.0.1.jar ./com/  生成对应的jar包,将jar包拷贝到指定环境

#### OA不启动服务测试方案 在main方法中增加以下内容
- 复制OA服务器上数据库配置文件：xxx\ecology\WEB-INF\prop\weaver.properties 文件至项目路径web\WEB-INF\prop路径下，请注意该文件不要提交到git，避免数据库信息泄露。
- 执行RecordSetTest测试是否能正常输出结果，自定义的test类必须要继承BaseTest类。
- 如不继承BaseTest类则可通过在main方法中增加以下内容：
  ```
    GCONST.setServerName("ecology");
    String fileSeparator = File.separator;
    GCONST.setRootPath(System.getProperty("user.dir")+fileSeparator+"web"+fileSeparator);
  ```rverName("ecology");