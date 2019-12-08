# SQRL

## 介绍
SQRL仅支持[SCXML标准](https://www.w3.org/TR/scxml/)的核心功能

- 嵌套状态机：顺序执行、并行执行
- 历史状态机：浅、深

## 目标
- 阅读源码同时反思自己对设计模式的理解
- fsm表下的结构比较扁平，待理解之后希望将其立体化
- 深度改造

## 结构
- component：SQRL需要的基础组件，包括工厂、单例对象池、后处理器、配置器等
- event：SQRL的事件机制实现
- exception：内部异常，及异常码表
- fsm：状态机核心功能
    - annotation：注解，用于定义状态机结构，或用于标记钩子方法
    - builder：提供构建状态机的流式API
    - impl：这一切的实现基本都在这里
- util：SQRL无关的通用工具

## 其他

### 状态机可视化
参照：[graphviz](https://www.graphviz.org/)  
安装依赖：`brew install graphviz`  
常用命令： `dot -Tpng -oExample.png Example.dot`

### 待办
阅读状态机的核心功能