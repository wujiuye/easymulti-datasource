# easymuti-datasource-mybatis

## 选择版本需注意
* 1.x版本：对应mybatis-plus2.x版本，spring boot 2.0.x版本，最新版本：1.1.1-RELEASE 
* 2.x版本：对应mybatis-plus3.x版本，spring boot 2.3.x版本，最新版本：2.1.0-RELEASE
* 3.x版本：更名

>由于新版本与旧版本可能不兼容，为此每个版本都会创建一个分支，便于修复该版本的bug，避免更换到新版本需要改动太多代码；
* 1.x版本不再更新特性，只维护；
* 2.0.1版本开始Mapper AOP切面支持监听事务状态，支持事务跟踪；
* 2.1.0版本开始支持sql埋点监听功能，sql事件消费支持监听事务状态、支持在sql执行之前同步做一些事情、sql执行之后异步消费；
* 2.1.0版本开始支持独立配置每个数据源的连接池；
* 3.x版本已经由easymuti-datasource-spring-boot-starter更名为easymuti-datasource-mybatis；
