# 版本更新说明

## 版本1.0.0 

日期：2020/03/25\
版本号：1.0.0\
更新说明：除主从库外，支持使用`1～10`这种普通的动态数据源

## 版本1.0.5

日期：2020/03/25\
版本号：1.0.5\
更新说明：解决`yaml`配置数据源时没有自动提示的问题

## 版本1.0.6

日期：2020/04/24\
版本号：1.0.6\
更新说明：[解决在同一个线程下数据源多次切换的回溯问题](https://github.com/wujiuye/easymulti-datasource/wiki/%E8%A7%A3%E5%86%B3%E5%9C%A8%E5%90%8C%E4%B8%80%E4%B8%AA%E7%BA%BF%E7%A8%8B%E4%B8%8B%E6%95%B0%E6%8D%AE%E6%BA%90%E5%A4%9A%E6%AC%A1%E5%88%87%E6%8D%A2%E7%9A%84%E5%9B%9E%E6%BA%AF%E9%97%AE%E9%A2%98)

## 版本1.0.9x
* 1、自动配置注解事务支持；
* 2、添加事务方法完成或者退出监听器；

## 版本1.1.1
* 修复1.0.9x存在的bug；

## 版本2.0.1
* 升级mybatis-plus到3.x版本，支持spring boot2.3.x

## 版本2.1.0
* 修复分页插件不起作用BUG；
* 弃用旧版本所有数据源共用一份连接池配置的策略，支持为每个数据源单独配置连接池；
* [支持sql埋点监听功能，并且支持事务，如果当前sql执行存在事务中，则会在事务提交后才会回调sql监听者](https://github.com/wujiuye/easymulti-datasource/wiki/%E6%94%AF%E6%8C%81%E7%9B%91%E5%90%ACSQL%E5%8A%9F%E8%83%BD)；

## 版本3.0.1
* 仅修改项目名称，由easymulti-datasource-spring-boot-starter更名为easymulti-datasource-mybatis

## 版本3.0.3
* fix bug: `No qualifying bean of type 'org.springframework.core.task.AsyncTaskExecutor' available: expected single matching bean but found 2: applicationTaskExecutor,taskScheduler`
* 优化调用链日记打印
