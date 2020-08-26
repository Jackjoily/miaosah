#这是仿照慕课上面的写秒杀项目

**框架SpringBooot+Mybatis+Redis+RabbitMq+Thymeleaf+Druid**


1.通过这个项目学习到了一些缓存的用处，通过Redis缓存对Mysql的读写进行优化，然后通过本地的缓存又降低了Redis的IO

2.然后一些非主要业务的逻辑，可以通过AOP，或者使用各种自定义的参数解析器，拦截器，对请求进行预处理，同时对各种异常也可以使用SpringBoot的@ControllerAdvice进行统一的捕获。

3.第一次解除了MQ，在这个项目中我觉得MQ的作用就是，将一个秒杀这样一个同步的过程的，变成了异步的过程，使得整个过程的开销的很大一部分都放在Client端，服务端只需告知Client结果就可以了。

4.终于明白了，当服务端的响应中设置了Cookie后，接下来的Request中会携带相应的Cookie

----
## 项目的运行流程

1.安装lombok插件，然后在本地活着服务器安装好Redis,RabbitMQ,然后配置好相应的URL以及端口，执行项目中SQL