## 简介 ##
本项目为kepler可选的组件，该组件能够让运维人员在没有@See[<a href="https://github.com/Kepler-Framework/Kepler-Admin">kepler admin</a>]的情况下通过命令行控制台快速修改运行在本机的kepler服务的参数，@See[<a href="https://github.com/Kepler-Framework/Kepler-All/wiki/%E5%8F%82%E6%95%B0%E9%85%8D%E7%BD%AE-%E5%8A%A8%E6%80%81%E5%8F%82%E6%95%B0">动态参数</a>]。

## 快速安装 ##
+ Clone项目  
`git clone https://github.com/Kepler-Framework/Kepler-Terminal`
+ 进入Kepler目录  
`cd Kepler-Terminal`
+ 安装至Maven本地仓库  
`mvn clean install`
+ (可选)安装至Maven内网私服  
`mvn clean deploy`
+ 加入pom.xml  
```
	<dependency>
		<groupId>com.kepler</groupId>
		<artifactId>kepler-terminal</artifactId>
		<version>实际Clone的版本</version>
	</dependency>
```

## 快速配置 ##
在spring配置文件里引入：kepler-terminal.xml
```

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd 
    http://www.springframework.org/schema/util
	http://www.springframework.org/schema/util/spring-util-3.0.xsd">

	<import resource="classpath:kepler-server.xml" />
	<import resource="classpath:kepler-terminal.xml" />

	<bean class="com.kepler.impl.TestAppImpl" />

</beans>

```
## 参数配置 ##

+ com.kepler.terminal.TerminalServer.enabled   
  `boolean型，默认true。terminal 服务开关`  
  `作用于Server`
+ com.kepler.terminal.TerminalServer.port   
  `int型，默认8888。terminal 服务端口号`  
  `作用于Server` 
+ com.kepler.terminal.TerminalServer.ip   
  `String型，默认127.0.0.1。terminal 服务綁定的本地ip`  
  `作用于Server` 
 + com.kepler.terminal.TerminalServer.cmd_max_length   
  `int型，默认1024。命令的最大长度`  
  `作用于Server` 

## 例子 ##

@See[<a href="https://github.com/Kepler-Framework/Kepler-Example/tree/master/config">Config</a>]
