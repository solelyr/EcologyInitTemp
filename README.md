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

## 开发环境搭建

1. 通过 Git 导入到本地开发工具中
  - 项目地址：https://github.com/solelyr/EcologyInitTemp
2. 设置项目目录
  - 建议在 `com.engine` 路径下再设置自定义的项目目录
3. 拷贝必要的 JAR 包依赖
   这个很重要，需要与客户环境的KB补丁包保持一致
  - 进入服务器上的 `/weaver/ecology/classbean` 目录，执行 `jar -cvf local-ecology.jar ./` 生成对应的 JAR 包，将 JAR 包拷贝到本地项目中
  - 拷贝服务器上的 `/weaver/ecology/WEB-INF/lib` 目录到本地项目中
  - 需要额外引入 JUnit 4 的 JAR 包才能支持单元测试
4. 拷贝必要的配置文件
  - 需要拷贝服务器上的配置文件到 `/web/WEB-INF/prop` 目录下，包含但不限于:
    ```
    /weaver/ecology/WEB-INF/prop/ecustom.properties  
    /weaver/ecology/WEB-INF/prop/initCache.properties  
    /weaver/ecology/WEB-INF/prop/weaver.properties  
    /weaver/ecology/WEB-INF/prop/weaver_enableMultiLangEncoding_whiteList.properties  
    /weaver/ecology/WEB-INF/prop/weaver_enableMultiLangEncoding_whiteList_new.properties  
    /weaver/ecology/WEB-INF/prop/weaver_rtx.properties  
    ```
  - 需要拷贝服务器上 `/weaver/ecology/WEB-INF/config/escache.xml` 到 `custom/web/WEB-INF/config/` 目录下
5. OA 自定义日志输出路径
  - 修改配置文件路径：`/weaver/ecology/WEB-INF/log4jinit.properties`，在最后面增加以下内容
    ```
    #自定义开发日志文件
    log4j.logger.autoLinkLog=INFO,ERROR,custom
    log4j.appender.autoLinkLog=org.apache.log4j.DailyRollingFileAppender
    log4j.appender.autoLinkLog.DatePattern='_'yyyyMMdd'.log'
    #@custom为日志目录名称，custom.log为日志名称
    log4j.appender.autoLinkLog.File=@custom/custom.log
    log4j.appender.autoLinkLog.layout=org.apache.log4j.PatternLayout
    log4j.appender.autoLinkLog.layout.ConversionPattern=%d{yyyy-MM-dd HH\:mm\:ss,SSS} %-5p [Thread\:%t] %m%n
    log4j.additivity.autoLinkLog=false
    ```
6. 部署至客户环境
  - `out/custom` 目录，执行 `jar -cvf solelyr-ecology.1.0.1.jar ./com/` 生成对应的 JAR 包，将 JAR 包拷贝到指定环境

7. OA 不启动服务测试方案
  - 复制 OA 服务器上数据库配置文件：`xxx\ecology\WEB-INF\prop\weaver.properties` 文件至项目路径 `web\WEB-INF\prop` 路径下，请注意该文件不要提交到 Git，避免数据库信息泄露。
  - 执行 `RecordSetTest` 测试是否能正常输出结果，自定义的 test 类必须要继承 `BaseTest` 类。
  - 如不继承 `BaseTest` 类则可通过在 main 方法中增加以下内容：
    ```
      GCONST.setServerName("ecology");
      String fileSeparator = File.separator;
      GCONST.setRootPath(System.getProperty("user.dir")+fileSeparator+"web"+fileSeparator);
    ```
