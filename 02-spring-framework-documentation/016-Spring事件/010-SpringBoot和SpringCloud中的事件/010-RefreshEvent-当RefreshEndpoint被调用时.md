# 010-RefreshEvent-当RefreshEndpoint被调用时 

[TOC]

## 一言蔽之

配置刷新事件，接收到这个事件时会构造一个临时的ApplicationContext（会加上BootstrapApplicationListener和 ConfigFileApplicationListener）意味着从配置中小心和配置文件中重新获取配置数据， 构建完毕后新的Environment里的PropertySource会跟原来的Environment里的PropertySource进行对比覆盖

## 
