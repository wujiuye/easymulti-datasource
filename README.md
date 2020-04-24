# easymulti-datasource-spring-boot-starter

项目地址：[https://github.com/wujiuye/easymulti-datasource-spring-boot-starter](https://github.com/wujiuye/easymulti-datasource-spring-boot-starter)

多数据源动态切换似乎已经成了微服务的标配，做过那么多项目发现每个项目都要配一个动态数据源，都要写一个切面去实现动态切换，因此，我想将这些繁琐的配置封装为`starter`，拿来即用。

去年我为项目封装过一个支持多数据库类型加`sharding-jdbc`的动态数据源，如`mysql+redshift`，但这毕竟是少数，大多数公司都只用`mysql`，所以就不考虑多数据库类型了，而分库可能更多的是用`mycat`，因此，我实现的只是一个单数据库类型（`mysql`）的动态数据源。

`easymulti`支持主从库模式，如果多于两个数据源，可切换为普通模式使用，普通模式支持最多十个数据源。使用非常简单，只需要简单的在`yml`中配置数据源，就可以使用动态多数据源，然后在项目中使用注解切换数据源。并且整合了`mybatis-plus`，不再需要繁琐的配置。

### 提供两种动态多数据源

* 1、主从数据源
* 2、非主从的多数据源

这两种数据源互弃，使用了主从动态数据源，就不能使用非主从多数据源

### 自动整合mybatis-plus

该框架整合了`mybatis-plus`，`mybatis-plus`的配置依然按照`mybatis-plus`官方介绍的去配置。
需要注意的是，全局配置的主键生成策略，我在代码中强制不生效了，主键生成使用数据库的自增主键。

### 连接池

数据库连接池使用的是`HikariCP`。所有数据源共享同一份连接池的配置，注意，是共用同一份
配置，而不是共用一个连接池。后续的版本将支持除默认共用的数据源配置外，可针对某个数据源
单独配置连接池。后续版本也会支持选择其它非`HikariCP`的连接池。

### 在项目中添加依赖

maven中使用：
```xml
<!-- https://mvnrepository.com/artifact/com.github.wujiuye/easymulti-datasource-spring-boot-starter -->
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>easymulti-datasource-spring-boot-starter</artifactId>
    <version>1.0.5</version>
</dependency>
```

gradle中使用：
```groovy
// https://mvnrepository.com/artifact/com.github.wujiuye/miniexcel
compile group: 'com.github.wujiuye', name: 'easymulti-datasource-spring-boot-starter', version: '1.0.5'
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
    # 连接池的配置
    pool:
      useConnPool: true
      maxPoolSize: 10
      connectionTimeout: 60
      maxLifetime: 60
    # 配置默认使用的数据源，不配置则默认使用master
    defalutDataSource: first
    first:
      jdbcUrl: jdbc:
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

easymulti-datasource-spring-boot-starter，作者开源的几个开源项目都有在项目中使用，遇到问题会及时解决。

在某些场景下，我们可能需要多次切换数据源才能处理完同一个请求，也就是在一个线程上多次切换数据源。

比如：ServiceA.a调用ServiceB.b，ServiceB.b调用ServiceC.c。ServiceA.a使用从库，ServiceB.b使用主库，ServiceC.c又使用从库，
因此，这一调用链路一共需要动态切换三次数据源。

数据源的切换我们都是使用切面完成，在方法执行之前切换，从注解上获取到数据源的key，将其保持到ThreadLocal。
当方法执行完成或异常时，需要从ThreadLocal中移除切换记录，否则可能会影响别的不显示声明切换数据源的地方获取到错误的数据源，
并且我们也需要保证ThreadLocal的remove方法被调用。这在多次切换数据源的情况下就会出问题。

当调用ServiceA.a时，切换到从库，方法执行到一半时由于需要调用ServiceB.b方法，此时数据源又被切换到了主库，
也就是说ServiceB.b方法切面将ServiceA.a方法切面的切换记录被覆盖了。当ServiceB.b方法执行完成后，ServiceB.b方法切面调用ThreadLocal的remove方法，
将ServiceB.b方法切面的切换记录移除，因此，再回到ServiceA.a方法继续往下执行时，由于ThreadLocal存储null, 如果配置了默认使用的数据源为主库，
那么ServiceA.a方法后面的数据库操作就都在主库上操作了。

通过切面切换数据源的方法如下：
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

为解决这个问题，我想到的是使用栈这个数据结构存储数据源切换记录，当调用ServiceA.a方法需要切换数据源时，
将数据源的key push到栈顶，当在ServiceA.a方法中调用ServiceB.b方法时，如果需要切换数据源，
也将ServiceB.b方法需要切换的数据源的key push到栈顶。

```java
public final class DataSourceContextHolder {
 /**
     * 设置数据源
     *
     * @param multipleDataSource
     */
    public static void setDataSource(EasyMutiDataSource.MultipleDataSource multipleDataSource) {
        DataSourceSwitchStack switchStack = multipleDataSourceThreadLocal.get();
        if (switchStack == null) {
            switchStack = new DataSourceSwitchStack();
            multipleDataSourceThreadLocal.set(switchStack);
        }
        switchStack.push(multipleDataSource);
    }
}
```

ServiceB.b方法执行完成时，方法切面需要调用clearDataSource方法将切换的数据源key从ThreadLocal中移除，这时我们可以先从
栈顶中移除一个元素，再判断栈的是否为空，为空再将栈从ThreadLocal中移除。

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
        switchStack.pop();
        if (switchStack.size() == 0) {
            multipleDataSourceThreadLocal.remove();
        }
    }
}
```

只有所有切点都调用完clearDataSource方法之后，再将切换栈从ThreadLocal中移除，每个切点执行完成之后，
调用clearDataSource方法将自身的切换记录移除，栈顶存储的就是前一个切点的切换记录，这就可以解决同一个线程下数据源多次切换的回溯问题，
使数据源切换正常。

存储切换记录的栈在easymulti-datasource的时候如下。

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

