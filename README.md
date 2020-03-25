# easymulti-datasource-spring-boot-starter

多数据源动态切换似乎已经成了微服务的标配，做过那么多项目发现每个项目都要配一个动态数据源，都要写一个切面去实现动态切换，因此，我想将这些繁琐的配置封装为starter，拿来即用。

去年我为项目封装过一个支持多数据库类型加sharding-jdbc的动态数据源，如mysql+redshift，但这毕竟是少数，大多数公司都只用mysql，所以就不考虑多数据库类型了，而分库可能更多的是用mycat，因此，我实现的只是一个单数据库类型（mysql）的动态数据源。

easymulti支持主从库模式，如果多于两个数据源，可切换为普通模式使用，普通模式支持最多十个数据源。使用非常简单，只需要简单的在yml中配置数据源，就可以使用动态多数据源，然后在项目中使用注解切换数据源。并且整合了mybatis-plus，不再需要繁琐的配置。

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

需要排除其它的start自动配置：org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```java
// 只扫描被@Mapper注解的接口，避免获取service包下的一些service接口
@MapperScan(basePackages = "com.xxxx", annotationClass = Mapper.class)
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
public class Main{
}
```

### mybatis-plus的配置
```yaml
mybatis-plus:
  global-config:
    # 配置开启驼峰映射
    db-column-underline: true
  mapper-locations: ["classpath:mapper/*.xml"]

```

### 1、使用主从数据源的配置

```yaml
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
      jdbcUrl: 
      username: 
      password: 
    # 从数据源
    slave:
      jdbcUrl: 
      username: 
      password: 
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

```yaml
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
      jdbcUrl: 
      username: 
      password: 
    second:
      jdbcUrl: 
      username: 
      password: 
    third:
      jdbcUrl: 
      username: 
      password: 
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
```

### 版本更新说明

#### 版本1.0.0 

日期：2020/03/25\
版本号：1.0.0\
更新说明：除主从库外，支持使用1～10这种普通的动态数据源

#### 版本1.0.5

日期：2020/03/25\
版本号：1.0.5\
更新说明：解决`yaml`配置数据源时没有自动提示的问题


