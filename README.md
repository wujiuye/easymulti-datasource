# easymulti-datasource-spring-boot-starter

[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource-spring-boot-starter)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## 关于版本
* 1.x版本：对应mybatis-plus2.x版本，spring boot 2.0.x版本，最新版本：1.1.1-RELEASE 
* 2.x版本：对应mybatis-plus3.x版本，spring boot 2.3.x版本，最新版本：2.1.0-RELEASE
* 2.0.1版本开始添加mybatis-plus代码生成器的支持，见[代码生成器项目](easy-mybatis-plus-generator/README.MD)

## 版本重大特性更新（详情见本文档的版本更新部分）
>由于新版本与旧版本可能不兼容，为此每个版本都会创建一个分支，便于修复该版本的bug，避免更换到新版本需要改动太多代码；
* 1.x版本不再更新特性，只维护；
* 2.0.1版本开始Mapper AOP切面支持监听事务状态，支持事务跟踪；
* 2.1.0版本开始支持sql埋点监听功能，sql事件消费支持监听事务状态、支持在sql执行之前同步做一些事情、sql执行之后异步消费；
* 2.1.0版本开始支持独立配置每个数据源的连接池；

## 简介

多数据源动态切换似乎已经成了微服务的标配，做过那么多项目发现每个项目都要配一个动态数据源，都要写一个切面去实现动态切换，因此，我想将这些繁琐的配置封装为`starter`，拿来即用。

去年我为项目封装过一个支持多数据库类型加`sharding-jdbc`的动态数据源，如`mysql+redshift`，但这毕竟是少数，大多数公司都只用`mysql`，所以就不考虑多数据库类型了，而分库可能更多的是用`mycat`，因此，我实现的只是一个单数据库类型（`mysql`）的动态数据源。

`easymulti`支持主从库模式，如果多于两个数据源，可切换为普通模式使用，普通模式支持最多十个数据源。使用非常简单，只需要简单的在`yml`中配置数据源，就可以使用动态多数据源，然后在项目中使用注解切换数据源。并且整合了`mybatis-plus`，不再需要繁琐的配置。

### 提供两种动态多数据源

* 1、主从数据源
* 2、非主从的多数据源

这两种数据源互弃，使用了主从动态数据源，就不能使用非主从多数据源
> 无论使用哪种，都支持单数据源的使用，如果只使用单数据源，主从数据源模式只需要配置主数据源，非主从的多数据源模式只需要配置Master数据源

### 自动整合mybatis-plus

该框架整合了`mybatis-plus`，`mybatis-plus`的配置依然按照`mybatis-plus`官方介绍的去配置。
需要注意的是，全局配置的主键生成策略，我在代码中强制不生效了，主键生成使用数据库的自增主键。

### 连接池

数据库连接池使用的是`HikariCP`，在2.1.0版本中，已支持为每个数据源单独配置连接池。

### 在项目中添加依赖

maven中使用：
```xml
<!-- https://mvnrepository.com/artifact/com.github.wujiuye/easymulti-datasource-spring-boot-starter -->
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>easymulti-datasource-spring-boot-starter</artifactId>
    <version>2.1.0</version>
</dependency>
```

gradle中使用：
```groovy
// https://mvnrepository.com/artifact/com.github.wujiuye/easymulti-datasource-spring-boot-starter
compile group: 'com.github.wujiuye', name: 'easymulti-datasource-spring-boot-starter', version: '2.1.0'
```

### 需要排除spring boot的数据源自动配置

需要排除其它的`start`自动配置：
`org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`

```java
// 只扫描被@Mapper注解的接口，避免获取service包下的一些service接口
@MapperScan(basePackages = "com.xxxx", annotationClass = Mapper.class)
// 排除spring boot的数据源自动配置
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

### 使用主从数据源的配置

```yaml
### 数据源配置
easymuti:
  datasource:
    # 配置默认使用的数据源，不配置则默认使用master
    defalutDataSource: master
    # 主数据源
    master:
      jdbcUrl: 
      username: 
      password: 
      # 连接池的配置
      pool:
        useConnPool: true
        maxPoolSize: 10
        connectionTimeout: 60
        maxLifetime: 60
    # 从数据源
    slave:
      jdbcUrl: 
      username: 
      password: 
      # 连接池的配置
      pool:
        useConnPool: true
        maxPoolSize: 10
        connectionTimeout: 60
        maxLifetime: 60
```

项目中使用
```java
public class XxxController{

    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Master)
    public void api(){
    
    }

}
```

或者

```java
public class XxxServiceImpl{
    
    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Slave)
    public void api(){
    
    }

}
```

### 非主从的多数据源动态数据源

```yaml
### 数据源配置
easymuti:
  datasource:
    # 配置默认使用的数据源，不配置则默认使用master
    defalutDataSource: first
    first:
      jdbcUrl: 
      username: 
      password: 
      # 连接池的配置
      pool:
        useConnPool: true
        maxPoolSize: 10
        connectionTimeout: 60
        maxLifetime: 60
    second:
      jdbcUrl: 
      username: 
      password: 
      # 连接池的配置
      pool:
        useConnPool: true
        maxPoolSize: 10
        connectionTimeout: 60
        maxLifetime: 60
    third:
      jdbcUrl: 
      username: 
      password: 
      # 连接池的配置
      pool:
        useConnPool: true
        maxPoolSize: 10
        connectionTimeout: 60
        maxLifetime: 60
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

或者

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

`1～10`个库可选，满足你的需求

```java
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.First)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Second)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Third)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Fourth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Fifth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Sixth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Seventh)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Eighth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Ninth)
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.Tenth)
```

### 版本更新说明

#### 版本1.0.0 

日期：2020/03/25\
版本号：1.0.0\
更新说明：除主从库外，支持使用`1～10`这种普通的动态数据源

#### 版本1.0.5

日期：2020/03/25\
版本号：1.0.5\
更新说明：解决`yaml`配置数据源时没有自动提示的问题

#### 版本1.0.6

日期：2020/04/24\
版本号：1.0.6\
更新说明：解决在同一个线程下数据源多次切换的回溯问题

在某些场景下，我们可能需要多次切换数据源才能处理完同一个请求，也就是在一个线程上多次切换数据源。

比如：`ServiceA.a`调用`ServiceB.b`，`ServiceB.b`调用`ServiceC.c`。`ServiceA.a`使用从库，`ServiceB.b`使用主库，`ServiceC.c`又使用从库，因此，这一调用链路一共需要动态切换三次数据源。

数据源的切换我们都是使用`AOP`完成，在方法执行之前切换，从注解上获取到数据源的`key`，将其保持到`ThreadLocal`。

当方法执行完成或异常时，需要从`ThreadLocal`中移除切换记录，否则可能会影响别的不显示声明切换数据源的地方获取到错误的数据源，并且我们也需要保证`ThreadLocal`的`remove`方法被调用，这在多次切换数据源的情况下就会出问题。

当调用`ServiceA.a`时，切换到从库，方法执行到一半时由于需要调用`ServiceB.b`方法，此时数据源又被切换到了主库，也就是说`ServiceB.b`方法切面将`ServiceA.a`方法切面的数据源切换记录覆盖了。

当`ServiceB.b`方法执行完成后，`ServiceB.b`方法切面调用`ThreadLocal`的`remove`方法，将`ServiceB.b`方法切面的数据源切换记录移除，此时回到`ServiceA.a`方法继续往下执行时，由于`ThreadLocal`存储`null`, 如果配置了默认使用的数据源为主库，那么`ServiceA.a`方法后面的数据库操作就都在主库上操作了。

这一现象我们可以称为方法调用回溯导致的动态数据源切换故障。

使用切面实现动态切换数据源的方法如下：

```java
public class EasyMutiDataSourceAspect {
/**
     * 切换数据源
     *
     * @param point 切点
     * @return
     * @throws Throwable
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        EasyMutiDataSource ds = method.getAnnotation(EasyMutiDataSource.class);
        if (ds == null) {
            DataSourceContextHolder.setDataSource(null);
        } else {
            DataSourceContextHolder.setDataSource(ds.value());
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.clearDataSource();
        }
    }
}
```

为解决这个问题，我想到的是使用栈这个数据结构存储动态数据源的切换记录。当调用`ServiceA.a`方法需要切换数据源时，将数据源的`key` `push`到栈顶，当在`ServiceA.a`方法中调用`ServiceB.b`方法时，切面切换数据源也将`ServiceB.b`方法需要切换的数据源的`key` `push`到栈顶。代码如下：

```java
public final class DataSourceContextHolder {
   
   /**
     * 设置数据源
     *
     * @param multipleDataSource
     */
    public static void setDataSource(EasyMutiDataSource.MultipleDataSource multipleDataSource) {
        // 用于存储切换记录的栈
        DataSourceSwitchStack switchStack = multipleDataSourceThreadLocal.get();
        if (switchStack == null) {
            switchStack = new DataSourceSwitchStack();
            multipleDataSourceThreadLocal.set(switchStack);
        }
        // 将当前切换的数据源推送到栈顶，覆盖上次切换的数据源
        switchStack.push(multipleDataSource);
    }
}
```

`ServiceB.b`方法执行完成时，方法切面需要调用`clearDataSource`方法将切换的数据源的`key`从`ThreadLocal`中移除，这时我们可以先从栈顶中移除一个元素，再判断栈是否为空，为空再将栈从`ThreadLocal`中移除。`pop`操作将`ServiceB.b`方法切面切换的数据源的`key`移除后，栈顶就是调用`ServiceB.b`方法之前使用的数据源。

```java
public final class DataSourceContextHolder {
    
   /**
     * 清除数据源
     */
    public static void clearDataSource() {
        DataSourceSwitchStack switchStack = multipleDataSourceThreadLocal.get();
        if (switchStack == null) {
            return;
        }
        // 回退数据源切换
        switchStack.pop();
        // 栈空则表示所有切换都已经还原，可以remove了
        if (switchStack.size() == 0) {
            multipleDataSourceThreadLocal.remove();
        }
    }
}
```

只有所有切点都调用完`clearDataSource`方法之后，再将保持数据源切换记录的栈从`ThreadLocal`中移除。每个切点执行完成之后，调用`clearDataSource`方法将自身的切换记录从栈中移除，栈顶存储的就是前一个切点的切换记录，即回退数据源切换。这就可以解决同一个线程下数据源多次切换的回溯问题，使数据源切换正常。

存储切换记录的栈在`easymulti-datasource`的时候如下。

```java
class DataSourceSwitchStack {

    private EasyMutiDataSource.MultipleDataSource[] stack;
    private int topIndex;
    private int leng = 2;

    public DataSourceSwitchStack() {
        stack = new EasyMutiDataSource.MultipleDataSource[leng];
        topIndex = -1;
    }

    public void push(EasyMutiDataSource.MultipleDataSource source) {
        if (topIndex + 1 == leng) {
            leng *= 2;
            stack = Arrays.copyOf(stack, leng);
        }
        this.stack[++topIndex] = source;
    }

    public EasyMutiDataSource.MultipleDataSource peek() {
        return stack[topIndex];
    }

    public EasyMutiDataSource.MultipleDataSource pop() {
        return stack[topIndex--];
    }

    public int size() {
        return topIndex + 1;
    }

}
```

#### 版本1.0.9x
* 1、自动配置注解事务支持；
* 2、添加事务方法完成或者退出监听器；

#### 版本1.1.1
* 修复1.0.9x存在的bug；

#### 版本2.0.1
* 升级mybatis-plus到3.x版本，支持spring boot2.3.x

#### 版本2.1.0
* 修复分页插件不起作用BUG；
* 弃用旧版本所有数据源共用一份连接池配置的策略，支持为每个数据源单独配置连接池；
* 支持sql埋点监听功能，并且支持事务，如果当前sql执行存在事务中，则会在事务提交后才会回调sql监听者；

第一步：启用sql埋点监听功能，并且启用事务调用链路追踪功能
```yaml
easymuti: 
  transaction: 
    open-chain: true
  sql-watcher:
    enable: true
```

第二步：编写观察者，可以有n多个，并且多个观察者也可观察同一个表、甚至相同字段
```java
/**
 * 封装通用逻辑
 *
 * @author wujiuye 2020/08/10
 */
@Component
@Slf4j
public class TestTableFieldObserver implements TableFieldObserver , InitializingBean {

    @Override
    public Set<WatchMetadata> observeMetadatas() {
       // 在这里注册要监听哪些表的哪些字段
    }

    /**
     * 监听到sql时被同步调用
     *
     * @param commandType 事件类型
     * @param matchResult 匹配的ITEM
     * @return 返回异步消费者
     */
    @Override
    public AsyncConsumer observe(CommandType commandType, MatchItem matchResult) {
        // 同步消费
        // 这里是sql执行之前，可在sql执行之前做一些事情，比如新旧数据的对比，这里查出旧数据

        // 异步消费，再sql执行完成时，或者在事务方法执行完成时（如果存在事务），完成指：正常执行完成 or 方法异常退出
        return throwable -> {
            // sql执行抛出异常不处理
            if (throwable != null) {
                return;
            }
            // 消费事件
            // ....
        };
    }

}

```
