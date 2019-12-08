/**
 * note：
 * 使用builder构建状态机结构
 * 几个子句：
 * - when...perform...(abbr:wp)         条件和迁移动作挂载
 * - rom...to...on...[wp]               单迁移路径定义
 * - between...and...onMutual...[wp]    相互迁移路径定义
 * - from...toAmong...onEach...[wp]     一到多迁移路径定义
 * - fromAmong...to...on...[wp]         多到一迁移路径定义
 *
 * 基于构建者模式，具体说是Step-Builder，这里DSL的实现很值得借鉴
 */
package org.squirrelframework.foundation.fsm.builder;