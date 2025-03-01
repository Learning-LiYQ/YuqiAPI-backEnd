# Yuqi-API

![language](https://img.shields.io/badge/language-Java-orange.svg)
![jdk](https://img.shields.io/static/v1?label=jdk&message=11&color=blue)

## 项目概述
本项目是一个简洁的API接口开放调用平台，管理员可以通过平台发布接口，并可视化各接口的调用情况；
用户可以浏览接口以及在线调试接口，并通过客户端SDK轻松调用接口。

## 技术栈
- 主语言：Java11
- 框架：SpringBoot、Mybatis-plus、Spring Cloud
- 数据库：MySQL8.0、Redis
- 消息队列：RabbitMQ
- 注册中心：Nacos
- RPC框架：Dubbo
- 网关：Spring Cloud Gateway

## 模块
- YuqiAPI-backend：后端服务，负责用户管理、接口管理和调用等基本功能
- YuqiAPI-frontEnd：前端页面
- YuqiAPI-common：公共模块，包含一些公用的实体类，公共接口等 
- YuqiAPI-gateway：api网关，作为整个后端的入口，具备服务转发、用户鉴权、统一日志、服务接口调用计数等功能
- YuqiAPI-interface：平台提供的接口服务，目前只有简单的Mock接口

## 系统架构

## 项目展示截图
