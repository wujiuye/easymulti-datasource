# easymulti-datasource

多数据源动态切换似乎已经成了微服务的标配，做过那么多项目发现每个项目都要配一个动态数据源，都要写一个切面去实现动态切换，因此，我想将这些繁琐的配置封装为`starter`，拿来即用。

`easymulti-datasource`支持主从库模式，如果多于两个数据源，可切换为普通模式使用，普通模式支持最多十个数据源。使用非常简单，只需要简单的在`yml`中配置数据源，就可以使用动态多数据源，然后在项目中使用注解切换数据源，当然，也支持直接调用API切换数据源。

* 有详细的使用文档，点这里：[使用文档](https://github.com/wujiuye/easymulti-datasource/wiki)

## 通知
原`easymulti-datasource-spring-boot-starter`项目已更名为`easymulti-datasource`，原`easymulti-datasource-spring-boot-starter`组件已经更名为`easymulti-datasource-mybatis`。新版本增加了`easymulti-datasource-r2dbc`（原`hotkit-r2dbc`）。

## 模块
### `easymulti-datasource-mybatis`
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource-mybatis/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
`mybatis`版多数据源组件，整合了`mybatis-plus`，不再需要繁琐的配置；
* [版本更新历史](easymulti-datasource-mybatis/VERSION.MD)

### `easymulti-datasource-r2dbc`
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource-r2dbc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wujiuye/easymulti-datasource)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
`spring-data-r2dbc`版多数据源组件，用于响应式编程；
* [版本更新历史](easymulti-datasource-r2dbc/VERSION.MD)

## easymulti-datasource的综合特色
* 提供两种动态多数据源模式：主从数据源模式/非主从的多数据源模式；
* 每个数据源独立的连接池配置，可针对每个数据源单独配置连接池；
* 支持监听SQL，监听修改某个表的某些字段的sql，用于实现埋点事件；
* 支持事务状态监听、注册事务监听器，用于在事务回滚/提交时再完成一些后台操作。
