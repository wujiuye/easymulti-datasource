# Mybatis-Plus代码生成器

## 使用easy-mybatis-plus-generator的前提
* 1、想通过配置文件配置方式使用；
* 2、不想生成Service、ServiceImpl、Controller类；
* 3、接受使用一些默认配置：为xml文件自动生成baseResultMap、不开启二级缓存、使用lombok、列名自动驼峰映射；

## 关于版本号
easy-mybatis-plus-generator选择与mybatis-plus的版本号保持一致，3.3.2版本对应mybatis-plus的3.3.2版本；

## 使用方式
* 第一步：添加依赖
```xml
<!-- easy-mybatis-plus-generator -->
<dependency>
    <groupId>com.github.wujiuye</groupId>
    <artifactId>easy-mybatis-plus-generator</artifactId>
    <version>3.3.2</version>
</dependency>
```
当然，还需要数据库的jdbc驱动包，比如mysql
```xml
<!-- mysql -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.29</version>
    <scope>runtime</scope>
</dependency>
```

* 第二步：添加配置
在resources目录下添加mybatis-plus-generator.properties配置文件，配置项如下
```properties
### 数据源配置
datasource.jdbc.url=
datasource.jdbc.driverName=com.mysql.jdbc.Driver
datasource.jdbc.username=
datasource.jdbc.password=

### 全局配置
globalconfig.author=mybatis-plus
globalconfig.outputDir=easy-mybatis-plus-generator/test/src/main/java
globalconfig.entityName=%sPO
globalconfig.mapperName=%sMapper
globalconfig.xmlName=%sMapper
```

* 第三步：编写代码

编写测试类，或者编写一个main方法，调用EasyMybatisGenerator#run方法生成代码，如下。
```java
public class EasyGeneratorTest {

    public static void main(String[] args) throws Exception {
        // 配置包信息 
        PackageConfig config = new PackageConfig()
                .setParent("com.github.wujiuye.generator")
                .setEntity("repo.dao.po")
                .setMapper("repo.dao")
                .setXml("repo.dao");
        // 开始生成代码
        EasyMybatisGenerator.run(config,"pay_config_rec");
    }

}
```

EasyMybatisGenerator#run方法参数说明：
* packageConfig：生成的Entity、Mappper的包信息，同时也是生成的Entity、Mappper、Xml文件的输出路径；
* tables：此次需要自动生成代码的数据库表的名称；

