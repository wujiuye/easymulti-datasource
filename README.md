# easymulti-datasource-spring-boot-starter

[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource-spring-boot-starter)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

* [使用文档](https://github.com/wujiuye/easymulti-datasource-spring-boot-starter/wiki)
* [版本更新历史](./VERSION.MD)

## 简介

多数据源动态切换似乎已经成了微服务的标配，做过那么多项目发现每个项目都要配一个动态数据源，都要写一个切面去实现动态切换，因此，我想将这些繁琐的配置封装为starter，拿来即用。

easymulti支持主从库模式，如果多于两个数据源，可切换为普通模式使用，普通模式支持最多十个数据源。使用非常简单，只需要简单的在yml中配置数据源，就可以使用动态多数据源，然后在项目中使用注解切换数据源。并且整合了mybatis-plus，不再需要繁琐的配置。

* 1.提供两种动态多数据源：主从数据源/非主从的多数据源，这两种数据源互弃，使用了主从动态数据源，就不能使用非主从多数据源
* 2.自动整合mybatis-plus，mybatis-plus的配置依然按照mybatis-plus官方介绍的去配置
* 3.独立连接池配置，数据库连接池使用的是HikariCP，可针对每个数据源单独配置连接池
* 4.支持监听SQL，监听修改某个表的某些字段的sql，用于实现埋点事件
* 5.支持事务状态监听、注册事务监听器，用于在事务回滚/提交时再完成一些后台操作

## 文档
* 详细的使用教程见文档：[使用文档](https://github.com/wujiuye/easymulti-datasource-spring-boot-starter/wiki)
* 版本选择注意事项及版本更新明细见文档：[版本更新历史](./VERSION.MD)
