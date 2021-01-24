# 介绍

地址：[https://github.com/hekailiang/squirrel](https://github.com/hekailiang/squirrel)

定位：轻量级的可扩展、可诊断、易用且类型安全的状态机实现

# 使用说明

## 状态机定义

### 状态机接口的四个泛型类型

* T：状态机的类型（Type）
* S：状态（State）类型
* E：事件（Event）类型
* C：外部上下文（Context）类型
### 状态机创建器

状态机创建器（StateMachineBuilder）使用 StateMachineBuilderFactory 来生成。创建器用来构建各种动作，包含迁移动作、状态进入退出动作等。当各种动作执行时，其内部状态会被隐式创建。

出于优化内存的考虑，一个创建器生成的所有状态机实例共享相同的**定义数据**。创建器采用了一种延迟的方式来生成状态机定义，具体的，当 Builder 首次创建状态机实例时，才会生成状态机定义。当状态机的定义生成后，后面再创建新的状态机实例就会快很多。通常，创建器应该尽可能的重复利用。

使用示例如下，create 方法传入的四个类型即是上面的 T、S、E、C

```
StateMachineBuilder<MyStateMachine, MyState, MyEvent, MyContext> builder = StateMachineBuilderFactory.create(MyStateMachine.class, MyState.class, MyEvent.class, MyContext.class);
```
### 流式API

在状态机创建器生成后，我们可以使用流式API来定义状态机的状态、迁移及动作。

迁移操作分为**外部迁移**和**内部迁移**，外部迁移才会完成状态变化，而内部迁移不会有状态变化。内部迁移可以定义优先级，当状态机被扩展时，更高优先级的迁移将替代低的。

有状态的迁移可以定义迁移条件，当满足条件时才会执行迁移动作。迁移条件的实现方式有如下两种。

1、实现状态接口，例如：

```
builder.externalTransition().from(MyState.C).to(MyState.D).on(MyEvent.GoToD).when(
    new Condition<MyContext>() {
        @Override
        public boolean isSatisfied(MyContext context) {
            return context!=null && context.getValue()>80;
        }
        
        @Override
        public String name() {
            return "MyCondition";
        }
}).callMethod("myInternalTransitionCall");
```
2、使用 MVEL 来描述
```
builder.externalTransition().from(MyState.C).to(MyState.D).on(MyEvent.GoToD).whenMvel("MyCondition:::(context!=null && context.getValue()>80)").callMethod("myInternalTransitionCall");
```
### 行为方法调用

我们可以在定义迁移与状态的时候定义匿名的动作，但这样会导致动作会比较分散，并且其他人难以覆写已有动作，从而导致其难以维护。因此，squirrel-foundation 支持在状态机类中定义行为方法，从而可以在流式API中显式指定。

```
StateMachineBuilder<...> builder = StateMachineBuilderFactory.create(MyStateMachine.class, MyState.class, MyEvent.class, MyContext.class);
builder.externalTransition().from(A).to(B).on(toB).callMethod("fromAToB");// All transition action method stays with state machine class
public class MyStateMachine<...> extends AbstractStateMachine<...> {
    protected void fromAToB(MyState from, MyState to, MyEvent event, MyContext context) {
        // this method will be called during transition from "A" to "B" on event "toB"
        // the action method parameters types and order should match
        ...
    }
}
```
另外，行为方法调用也支持「约定优于配置」的方式，当方法名符合规范时，会自动被注册到行为方法中。支持的命名模式有下面几个。

>transitFrom[fromStateName]To[toStateName]On[eventName]When[conditionName]
>transitFrom[fromStateName]To[toStateName]On[eventName]
>transitFromAnyTo[toStateName]On[eventName]
>transitFrom[fromStateName]ToAnyOn[eventName]
>transitFrom[fromStateName]To[toStateName]
>>on[eventName]
>exit[StateName]
>entry[StateName]
>>beforeExitAny/afterExitAny
>beforeEntryAny/afterEntryAny
### 注解声明

使用注解的方式来声明或扩展状态机。注释需要声明在状态机的实现类或接口上，并且可以同流式API混用。示例如下。

```
@States({
    @State(name="A", entryCallMethod="entryStateA", exitCallMethod="exitStateA"),
    @State(name="B", entryCallMethod="entryStateB", exitCallMethod="exitStateB")
})
@Transitions({
    @Transit(from="A", to="B", on="GoToB", callMethod="stateAToStateBOnGotoB"),
    @Transit(from="A", to="A", on="WithinA", callMethod="stateAToStateAOnWithinA", type=TransitionType.INTERNAL)
})
interface MyStateMachine extends StateMachine<MyStateMachine, MyState, MyEvent, MyContext> {
    void entryStateA(MyState from, MyState to, MyEvent event, MyContext context);
    void stateAToStateBOnGotoB(MyState from, MyState to, MyEvent event, MyContext context)
    void stateAToStateAOnWithinA(MyState from, MyState to, MyEvent event, MyContext context)
    void exitStateA(MyState from, MyState to, MyEvent event, MyContext context);
    ...
}
```
### 转化器

为了支持注解声明时对状态及事件的配置，用户需要自行实现转化器，来完成特定类型到字符串的转化，并且需要注册到 ConverterProvider 中。当然，如果原来的类型就是 String 或枚举，就无需显式实现转化器了。

## 状态机实例

### 新的状态机实例

在定义好状态机行为后，建造器就可以创建状态机实例了，此时，建造器中就不能新增元素了。产生新的状态机实例时，初始状态是必传项。

```
MyStateMachine stateMachine = builder.newStateMachine(MyState.Initial);
```
### 触发迁移

可以使用 fire 方法来触发状态迁移，示例如下。

```
stateMachine.fire(MyEvent.Prepare, new MyContext("Testing"));
```
## 特殊的状态机

### 无类型的状态机

为了简化状态机的定义，避免太多的泛型类型，因此产生了无类型的状态机。无类型的状态机需要继承自 AbstractUntypedStateMachine，并且通过 StateMachineBuilderFactory 来创建，需要使用 @StateMachineParameters 来声明泛型类型。示例如下。

```
enum TestEvent {
    toA, toB, toC, toD
}@Transitions({
    @Transit(from="A", to="B", on="toB", callMethod="fromAToB"),
    @Transit(from="B", to="C", on="toC"),
    @Transit(from="C", to="D", on="toD")
})
@StateMachineParameters(stateType=String.class, eventType=TestEvent.class, contextType=Integer.class)
class UntypedStateMachineSample extends AbstractUntypedStateMachine {
    // No need to specify constructor anymore since 0.2.9
    // protected UntypedStateMachineSample(ImmutableUntypedState initialState,
    //  Map<Object, ImmutableUntypedState> states) {
    //    super(initialState, states);
    // }
    
    protected void fromAToB(String from, String to, TestEvent event, Integer context) {
        // transition action still type safe ...
    }    protected void transitFromDToAOntoA(String from, String to, TestEvent event, Integer context) {
        // transition action still type safe ...
    }
}UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(
    UntypedStateMachineSample.class);
// state machine builder not type safe anymore
builder.externalTransition().from("D").to("A").on(TestEvent.toA);
UntypedStateMachine fsm = builder.newStateMachine("A");
```
### 无上下文的状态机

有些状态机是无需上下文的，其迁移仅由事件驱动，这种情况下，用户可以使用无上下文状态机来简化方法调用。这里需要使用 @ContextInsensitive 注解来标注实现类。示例如下。

```
@ContextInsensitive
public class ATMStateMachine extends AbstractStateMachine<ATMStateMachine, ATMState, String, Void> {
    // no need to add context parameter here anymore
    public void transitFromIdleToLoadingOnConnected(ATMState from, ATMState to, String event) {
        ...
    }
    public void entryLoading(ATMState from, ATMState to, String event) {
        ...
    }
}
```
## 异常处理

### 迁移异常处理器

状态迁移时，有异常发生将导致动作列表执行终止，并且状态机进入错误状态，这意味着状态机实例不能再处理其他事件。迁移期间（动作执行与监听器执行）的异常都会被包装为 TransitionException，默认策略是简单粗暴地抛出，如果某些异常可以恢复的话，可以手动实现 afterTransitionCausedException 接口来完成恢复逻辑，示例如下。

```
@Override
protected void afterTransitionCausedException(Object fromState, Object toState, Object event, Object context) {
    Throwable targeException = getLastException().getTargetException();
    // recover from IllegalArgumentException thrown out from state 'A' to 'B' caused by event 'ToB'
    if(targeException instanceof IllegalArgumentException &&
            fromState.equals("A") && toState.equals("B") && event.equals("ToB")) {
        // do some error clean up job here
        // ...
        // after recovered from this exception, reset the state machine status back to normal
        setStatus(StateMachineStatus.IDLE);
    } else if(...) {
        // recover from other exception ...
    } else {
        super.afterTransitionCausedException(fromState, toState, event, context);
    }
}
```
## 高级特性

### 定义层次状态

层次状态即包含嵌套状态的状态。当一个层级状态被激活的时候，有且只有一个子状态是激活状态。层级状态的定义可以通过API或者是注解的方式来完成。

### 定义并行状态

并行状态包含了一套子状态，当父状态激活的时候，子状态被同步激活。并行状态的定义可以通过API或者是注解的方式来完成。

### 定义上下文事件

上下文事件即用户定义的事件作为状态机预定义的事件。squirrel-foundation 中预定义了三种上下文事件：

* 开始/终止事件：当状态机启动或终止时触发
* 结束事件：当所有的并行状态均到达最终状态时，结束事件被自动触发

定义这三种事件可以通过API或者是注解的方式来完成。

### 使用历史状态保存或恢复当前状态

历史伪态允许状态机记录它的状态配置。通过这个配置，状态机可以将当前状态回溯为上一个状态。记录状态的方式根据历史状态类型分为「浅」和「深」两种，「浅」类型则仅记录直接状态，「深」类型则记录所有激活的类型。

### 迁移类型

根据UML标准，迁移分为如下三种。

* 内部迁移：触发时不会有任何状态变化，可以理解为迁移标记是一个自环
* 本地迁移：迁移发生在合成状态内部，不会触发迁移动作
* 外部迁移：两个合成状态之间的迁移

squirrel-foundation 使用 API 和注解的形式支持这两种配置

### 多态事件分配

在状态机的生命周期中，会有多种事件被触发，其继承关系如下。

```
|--StateMachineEvent                        
       |--StartEvent                        
       |--TerminateEvent                    
       |--TransitionEvent                   
            |--TransitionBeginEvent         
            |--TransitionCompleteEvent      
            |--TransitionExceptionEvent     
            |--TransitionDeclinedEvent      
            |--TransitionEndEvent      
```
用户可以自定义监听器来定制事件发生时的处理动作，示例如下。
```
stateMachine.addStateMachineListener(new StateMachineListener<...>() {
        @Override
        public void stateMachineEvent(StateMachineEvent<...> event) {
            // ...
        }
});
```
### 声明事件监听器

上面的事件监听器添加有时候用起来很让人厌烦，并且其内部使用了很多泛型也使代码难以阅读。为了简化状态机的使用，更重要的是提供一种非侵入式的集成方式，squirrel-foundation 通过注解提供了一种声明的方式来添加时间监听器。

```
static class ExternalModule {
    @OnTransitionEnd
    @ListenerOrder(10) // Since 0.3.1 ListenerOrder can be used to insure listener invoked orderly
    public void transitionEnd() {
        // method annotated with TransitionEnd will be invoked when transition end...
        // the method must be public and return nothing
    }
    
    @OnTransitionBegin
    public void transitionBegin(TestEvent event) {
        // method annotated with TransitionBegin will be invoked when transition begin...
    }
    
    // 'event'(E), 'from'(S), 'to'(S), 'context'(C) and 'stateMachine'(T) can be used in MVEL scripts
    @OnTransitionBegin(when="event.name().equals(\"toB\")")
    public void transitionBeginConditional() {
        // method will be invoked when transition begin while transition caused by event "toB"
    }
    
    @OnTransitionComplete
    public void transitionComplete(String from, String to, TestEvent event, Integer context) {
        // method annotated with TransitionComplete will be invoked when transition complete...
    }
    
    @OnTransitionDecline
    public void transitionDeclined(String from, TestEvent event, Integer context) {
        // method annotated with TransitionDecline will be invoked when transition declined...
    }
    
    @OnBeforeActionExecuted
    public void onBeforeActionExecuted(Object sourceState, Object targetState,
            Object event, Object context, int[] mOfN, Action<?, ?, ?,?> action) {
        // method annotated with OnAfterActionExecuted will be invoked before action invoked
    }
    
    @OnAfterActionExecuted
    public void onAfterActionExecuted(Object sourceState, Object targetState,
            Object event, Object context, int[] mOfN, Action<?, ?, ?,?> action) {
        // method annotated with OnAfterActionExecuted will be invoked after action invoked
    }    @OnActionExecException
    public void onActionExecException(Action<?, ?, ?,?> action, TransitionException e) {
        // method annotated with OnActionExecException will be invoked when action thrown exception
    }
}ExternalModule externalModule = new ExternalModule();
fsm.addDeclarativeListener(externalModule);
...
fsm.removeDeclarativeListener(externalModule);
```
这样做无需实现任何接口，只需要短短的几个注解就可以挂载到状态机的迁移上。方法的参数也是类型安全的，并且可以根据对应的事件自动推断。这很好地实现了「关注点分离」的思想。
### 迁移扩展方法

在 AbstractStateMachine 也有相应的扩展方法，允许状态机的具体实现来扩展，例如下面这些。

```
protected void afterTransitionCausedException(Exception e, S fromState, S toState, E event, C context) {
}protected void beforeTransitionBegin(S fromState, E event, C context) {
}protected void afterTransitionCompleted(S fromState, S toState, E event, C context) {
}protected void afterTransitionEnd(S fromState, S toState, E event, C context) {
}protected void afterTransitionDeclined(S fromState, E event, C context) {
}protected void beforeActionInvoked(S fromState, S toState, E event, C context) {
}
```
### 有权重的动作

用户可以定义动作的权重来调整动作的执行顺序，权重越大越先执行，其默认值是0。

使用注解来定义方法权重需要用':'来分隔，示例如下。

```
// define state entry action 'goEntryD' weight -150
@State(name="D", entryCallMethod="goEntryD:-150")
// define transition action 'goAToC1' weight +150
@Transit(from="A", to="C", on="ToC", callMethod="goAToC1:+150")
```
squirrel-foundation 也支持一种很方便的方式来声明权重，before 代表 100，而 after 代表 -100，分别表示最先和最后执行，而 ignore 表示该方法不会被执行。
另一种定义方式是通过实现方法，示例如下。

```
Action<...> newAction = new Action<...>() {
    ...
    @Override
    public int weight() {
        return 100;
    }
}
```
### 异步执行

可以使用 @AsyncExecute  来标记需要异步执行的动作，它可以被用于迁移动作或事件监听器中。示例如下。

```
@ContextInsensitive
@StateMachineParameters(stateType=String.class, eventType=String.class, contextType=Void.class)
public class ConcurrentSimpleStateMachine extends AbstractUntypedStateMachine {
    // No need to specify constructor anymore since 0.2.9
    // protected ConcurrentSimpleStateMachine(ImmutableUntypedState initialState,
    //    Map<Object, ImmutableUntypedState> states) {
    //  super(initialState, states);
    // }    @AsyncExecute
    protected void fromAToB(String from, String to, String event) {
        // this action method will be invoked asynchronously
    }
}
```
异步执行的任务将会被提交给 ExecutorService 来执行，用户也可以通过将自定义的执行器注册到 SquirrelSingletonProvider 中来指定。
```
ExecutorService executorService = Executors.newFixedThreadPool(1);
SquirrelSingletonProvider.getInstance().register(ExecutorService.class, executorService);
```
### 状态机后处理

用户可以定义指定状态机类型的后处理，在状态机实例化后添加后处理逻辑。

```
// 1 User defined a state machine interface
interface MyStateMachine extends StateMachine<MyStateMachine, MyState, MyEvent, MyContext> {
. . .
}// 2 Both MyStateMachineImpl and MyStateMachineImplEx are implemented MyStateMachine
class MyStateMachineImpl implements MyStateMachine {
    . . .
}
class MyStateMachineImplEx implements MyStateMachine {
    . . .
}// 3 User define a state machine post processor
MyStateMachinePostProcessor implements SquirrelPostProcessor<MyStateMachine> {
    void postProcess(MyStateMachine component) {
        . . .
    }
}// 4 User register state machine post process
SquirrelPostProcessorProvider.getInstance().register(MyStateMachine.class, MyStateMachinePostProcessor.class);
```
### 状态机导出

SCXMLVisitor 可以被用于导出状态机的 SCXML 定义文档，示例如下。

```
SCXMLVisitor visitor = SquirrelProvider.getInstance().newInstance(SCXMLVisitor.class);
stateMachine.accept(visitor);
visitor.convertSCXMLFile("MyStateMachine", true);
```
当然用户可以通过 StateMachine.exportXMLDefinition(true)  来导出美化后的 XML文档。DotVisitor 可以被用于生成状态机的图形文件。

```
DotVisitor visitor = SquirrelProvider.getInstance().newInstance(DotVisitor.class);
stateMachine.accept(visitor);
visitor.convertDotFile("SnakeStateMachine");
```
### 状态机导入

UntypedStateMachineImporter 可以用于导入 squirrel-foundation 自定义的类似于 SCXML 的定义，之后可以通过它来生成状态机。

```
UntypedStateMachineBuilder builder = new UntypedStateMachineImporter().importDefinition(scxmlDef);
ATMStateMachine stateMachine = builder.newAnyStateMachine(ATMState.Idle);
```
### 保存和加载状态机数据

当状态机不工作的时候，用户可以保存状态机的数据。

```
StateMachineData.Reader<MyStateMachine, MyState, MyEvent, MyContext>
    savedData = stateMachine.dumpSavedData();
```
之后用户可以在另一个已终止或刚刚初始化的实例中加载这个数据。
```
newStateMachineInstance.loadSavedData(savedData);
```
备注：状态机实例数据可以在 ObjectSerializableSupport 的帮助下使用 Base64 编码来编解码，用于外部存储。
### 状态机配置

当创建状态机实例的时候，用户可以通过 StateMachineConfiguration 来配置状态机的行为。

```
UntypedStateMachine fsm = builder.newUntypedStateMachine("a",
     StateMachineConfiguration.create().enableAutoStart(false)
            .setIdProvider(IdProvider.UUIDProvider.getInstance()),
     new Object[0]); // since 0.3.0
fsm.fire(TestEvent.toA);
```
### 状态机诊断

StateMachineLogger 用于观察状态机的内部状态，例如执行性能、动作调用序列、迁移进程等等。

```
StateMachine<?,?,?,?> stateMachine = builder.newStateMachine(HState.A);
StateMachineLogger fsmLogger = new StateMachineLogger(stateMachine);
fsmLogger.startLogging();
...
stateMachine.fire(HEvent.B2A, 1);
...
fsmLogger.terminateLogging();
```
在 v0.3.0 版本后，日志调试功能可以通过设置 StateMachineConfiguration 来方便启动。
```
StateMachine<?,?,?,?> stateMachine = builder.newStateMachine(HState.A,
        StateMachineConfiguration.create().enableDebugMode(true),
        new Object[0]);
```
StateMachinePerformanceMonitor 被用于检测动作的执行性能，包括迁移时间、平均迁移事件等。
在行为上使用 @LogExecTime 注解可以记录方法的执行时间，可以用在方法或是类上。

```
@LogExecTime
protected void transitFromAToBOnGoToB(MyState from, MyState to, MyEvent event, MyContext context)
```
### 定时状态

在进入状态后，定时事件可以延迟或者周期性地触发一些事件。定时任务将注册到 ScheduledExecutorService 中，当然具体的实现也可以由 SquirrelSingletonProvider 来定制实现。

```
// after 50ms delay fire event "FIRST" every 100ms with null context
builder.defineTimedState("A", 50, 100, "FIRST", null);
builder.internalTransition().within("A").on("FIRST");
```
### 链接状态（也称为子机状态）

链接状态描述了子机状态机的插入规范。包含链接状态的状态机称为包含状态机。 在单个包含状态机的上下文中，同一个状态机可能作为子机多次。

链接状态在语义上等效于复合状态，子机的作用域即是复合状态的作用域。 进入、退出和行为操作以及内部转换被定义为状态的一部分。 子机状态是一种分解机制，允许分解常见行为及其重用。 可以通过以下示例代码定义链接状态。

```
builderOfTestStateMachine.definedLinkedState(LState.A, builderOfLinkedStateMachine, LState.A1);
```
### ~~JMX支持~~

用户可以远程监控状态机实例并且在运行时修改配置，所有的实例信息均在 org.squirrelframework 域下面。

```
UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(...);
builder.setStateMachineConfiguration(StateMachineConfiguration.create().enableRemoteMonitor(true));
```
注：这个支持将于最新的3.9版本后过时

