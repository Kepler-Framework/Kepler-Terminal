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

## 例子 ##

@See[<a href="https://github.com/Kepler-Framework/Kepler-Example/tree/master/config">Config</a>]
