# easymulti-datasource-r2dbc
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/hotkit-r2dbc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource-r2dbc)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

`easymulti-datasource-r2dbc`为`spring-data-r2dbc`实现动态路由接口，为反应式编程提供声明式和编程式多数据源动态切换提供支持。并提供两种多数据源模式，覆盖常见的多数据源使用场景，分别是主从模式和`Cluster`模式，`Cluster`模式支持最多配置`3`个数据源，而主从模式支持一主一从。

## 添加依赖与配置数据源

使用`easymulti-datasource-r2dbc`后，无需再在项目中添加`spring-boot-starter-data-r2dbc`的依赖，也不需要添加`spring-data-r2dbc`的依赖。

`easymulti-datasource-r2dbc`版本号对应`spring-data-r2dbc`的版本号：
|easymulti-datasource-r2dbc|spring-data-r2dbc|
|--|--|
|3.0.1-RELEASE|1.1.0.RELEASE|

在项目中添加`easymulti-datasource-r2dbc`的依赖，如下。
```xml
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>easymulti-datasource-r2dbc</artifactId>
    <version>${version}</version>
</dependency>
```

此时，只需要额外添加用到的数据库类型对应的驱动依赖即可，例如，添加`mysql`的`r2dbc`驱动。
```xml
<dependency>
    <groupId>dev.miku</groupId>
    <artifactId>r2dbc-mysql</artifactId>
    <version>0.8.2.RELEASE</version>
</dependency>
```

如果使用主从模式，则使用如下配置。
```yaml
easymuti:
  database:
    r2dbc:
      master-slave-mode:
        master:
          url: r2dbc:mysql://127.0.0.1:3306/r2dbc_stu
          username: root
          password:
          pool:
            max-size: 5
            idel-timeout: 60
        slave:
          url: r2dbc:mysql://127.0.0.1:3306/r2dbc_stu
          username: root
          password:
          pool:
            max-size: 5
            idel-timeout: 60
```

`master`会被设置为默认使用的数据源，`slave`有则配置，没有也可以为空。虽然`slave`允许为空，但如果真的不需要多数据源，也是没有必要使用`easymulti-datasource-r2dbc`的。

如果使用`Cluster`模式，则使用如下配置。
```yaml
easymuti:
  database:
    r2dbc:
      cluster-mode:
        first:
          url: r2dbc:mysql://127.0.0.1:3306/r2dbc_stu
          username: root
          password:
          pool:
            max-size: 5
            idel-timeout: 60
        second:
          url: r2dbc:mysql://127.0.0.1:3306/r2dbc_stu
          username: root
          password:
          pool:
            max-size: 5
            idel-timeout: 60
        third:
          url: r2dbc:mysql://127.0.0.1:3306/r2dbc_stu
          username: root
          password:
          pool:
            max-size: 5
            idel-timeout: 60
```
其中`first`会被设置为默认使用的数据源，`second`与`third`可以为空。

## 声明式动态切换数据源

声明式动态切换数据源即使用注解方式动态切换数据源，只需要在`spring bean`的`public`方法或者类上添加`@R2dbcDataBase`注解，将注解的`value`属性指定为使用的数据源。

示例代码如下。

```java
@Service
public class PersonService {

    @Resource
    private PersonRepository personRepository;
  
    // 方法返回值类型为Mono测试
    @R2dbcDataBase(MasterSlaveMode.Master)
    @Transactional(rollbackFor = Throwable.class)
    public Mono<Integer> addPerson(Person... persons) {
        Mono<Integer> txOp = null;
        for (Person person : persons) {
            if (txOp == null) {
                txOp = personRepository.insertPerson(person.getId(), person.getName(), person.getAge());
            } else {
                txOp = txOp.then(personRepository.insertPerson(person.getId(), person.getName(), person.getAge()));
            }
        }
        return txOp;
    }

    // 方法返回值类型为Flux测试
    @R2dbcDataBase(MasterSlaveMode.Master)
    @Transactional(rollbackFor = Throwable.class)
    public Flux<Integer> addPersons(Flux<Person> persons) {
        return persons.flatMap(person -> personRepository.insertPerson(person.getId(), person.getName(), person.getAge()));
    }

}
```

* 如果是主从模式，`@R2dbcDataBase`注解的`value`属性可选值参见`MasterSlaveMode`接口声明的常量；
* 如果是`Cluster`模式，`@R2dbcDataBase`注解的`value`属性可选值参见`ClusterMode`接口声明的常量；

## 编程式动态切换数据源

声明式切换数据源的实现是依赖编程式切换数据源实现的，因此，我们也可以直接编写代码切换数据源，而不需要将方法改为`public`暴露出去。

只需要调用`EasyMutiR2dbcRoutingConnectionFactory`提供的静态方法`putDataSource`为`Context`写入使用的数据源，代码如下。

```java
public class RoutingTest extends SupporSpringBootTest {

    @Resource
    private DatabaseClient client;
    @Resource
    private ReactiveTransactionManager reactiveTransactionManager;

    @Test
    public void test() throws InterruptedException {
        TransactionalOperator operator = TransactionalOperator.create(reactiveTransactionManager);
        Mono<Void> atomicOperation = client.execute("INSERT INTO person (id, name, age) VALUES(:id, :name, :age)")
                .bind("id", "joe")
                .bind("name", "Joe")
                .bind("age", 34)
                .fetch().rowsUpdated()
                .then(client.execute("INSERT INTO person (id, name) VALUES(:id, :name)")
                        .bind("id", "joe")
                        .bind("name", "Joe")
                        .fetch().rowsUpdated())
                .then();
        // 包装事务
        Mono<Void> txOperation = operator.transactional(atomicOperation);
        // 包装切换数据源
        EasyMutiR2dbcRoutingConnectionFactory.putDataSource(txOperation, MasterSlaveMode.Slave).subscribe();
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

}
```

需要注意，如果需要使用事务，必须先调用`TransactionalOperator`对象的`transactional`方法，再调用`EasyMutiR2dbcRoutingConnectionFactory`的`putDataSource`方法。
