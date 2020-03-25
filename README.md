# easymulti-datasource-spring-boot-starter
只需要简单的在yml中配置数据源，就可以使用动态多数据源，然后在项目中使用注解切换数据源。并且整合mybatis-plus，不再需要繁琐的配置。

### 提供两种动态多数据源

* 1、主从数据源
* 2、非主从的多数据源

这两种数据源互弃，使用了主从动态数据源，就不能使用非主从多数据源

### 自动整合mybatis-plus

该框架整合了mybatis-plus，mybatis-plus的配置依赖按照mybatis-plus官方介绍的去配置，
但需要注意的是，全局配置的主键生成策略，我在代码中强制不使用，主键生成使用数据库的自增主键。

### 连接池

数据库连接池使用的是HikariCP。所有数据源共享同一份连接池的配置，注意，是共用同一份
配置，而不是共用一个连接池。后续的版本将支持除默认共用的数据源配置外，可针对某个数据源
单独配置连接池。后续版本也会支持选择其它非HikariCP的连接池。

### 在项目中添加依赖

maven
```xml
<!-- https://mvnrepository.com/artifact/com.github.wujiuye/easymulti-datasource-spring-boot-starter -->
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>easymulti-datasource-spring-boot-starter</artifactId>
    <version>${版本号}</version>
</dependency>
```

gradle
```groovy
// https://mvnrepository.com/artifact/com.github.wujiuye/miniexcel
compile group: 'com.github.wujiuye', name: 'easymulti-datasource-spring-boot-starter', version: '${版本号}'
```

### 需要排除spring boot的或者mybatis starter的自动配置

需要排除其它的start自动配置：
```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Main{
}
```

### 1、使用主从数据源的配置

```yml
### 数据源配置
easymuti:
  datasource:
    # 连接池的配置
    pool:
      useConnPool: true
      maxPoolSize: 10
      connectionTimeout: 60
      maxLifetime: 60
    # 配置默认使用的数据源，不配置则默认使用master
    defalutDataSource: master
    # 主数据源
    master:
      jdbcUrl: jdbc:mysql://rm-wz9w2pj1crm4e2k80.mysql.rds.aliyuncs.com:3306/onion?useUnicode=true&characterEncoding=UTF-8&useLocalSessionState=true&rewriteBatchedStat&zeroDateTimeBehavior=convertToNull
      username: ycmanger_2b
      password: cdYqa!#sadi&^cdUYwe98==
    # 从数据源
    slave:
      jdbcUrl: jdbc:mysql://rr-wz914q2o8d8q75a90.mysql.rds.aliyuncs.com:3306/onion?useUnicode=true&characterEncoding=UTF-8&useLocalSessionState=true&rewriteBatchedStat&zeroDateTimeBehavior=convertToNull
      username: ycmanger_2b
      password: cdYqa!#sadi&^cdUYwe98==
```

项目中使用
```java
public class XxxController{

    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Master)
    public void api(){
    
    }

}
```

or

```java
public class XxxServiceImpl{
    
    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Slave)
    public void api(){
    
    }

}
```

### 2、普通的多数据源动态数据源

```yml
### 数据源配置
easymuti:
  datasource:
    # 连接池的配置
    pool:
      useConnPool: true
      maxPoolSize: 10
      connectionTimeout: 60
      maxLifetime: 60
    # 配置默认使用的数据源，不配置则默认使用master
    defalutDataSource: master
    first:
      jdbcUrl: jdbc:mysql://rm-wz9w2pj1crm4e2k80.mysql.rds.aliyuncs.com:3306/onion?useUnicode=true&characterEncoding=UTF-8&useLocalSessionState=true&rewriteBatchedStat&zeroDateTimeBehavior=convertToNull
      username: ycmanger_2b
      password: cdYqa!#sadi&^cdUYwe98==
    second:
      jdbcUrl: jdbc:mysql://rr-wz914q2o8d8q75a90.mysql.rds.aliyuncs.com:3306/onion?useUnicode=true&characterEncoding=UTF-8&useLocalSessionState=true&rewriteBatchedStat&zeroDateTimeBehavior=convertToNull
      username: ycmanger_2b
      password: cdYqa!#sadi&^cdUYwe98==
    third:
      jdbcUrl: jdbc:mysql://rr-wz914q2o8d8q75a90.mysql.rds.aliyuncs.com:3306/onion?useUnicode=true&characterEncoding=UTF-8&useLocalSessionState=true&rewriteBatchedStat&zeroDateTimeBehavior=convertToNull
      username: ycmanger_2b
      password: cdYqa!#sadi&^cdUYwe98==
    # ....还可以继续配置，配多少个就能用多少个
```

项目中使用
```java
public class XxxController{

    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.First)
    public void api(){
    
    }

}
```

or

```java
public class XxxServiceImpl{
    
    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Second)
    public void api(){
    
    }
    
    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.third)
    public void api(){
    
    }

}
```

1～10个库
```java
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.First)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Second)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Third)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Fourth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Fifth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Sixth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Seventh)
// .......
```

### 版本更新说明

#### 版本1.0.0 

日期：2020/03/25\
版本号：1.0.0\
更新说明：除主从库外，支持使用1～10这种普通的动态数据源


