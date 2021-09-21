# 010-Actor生命周期-初始化ActorSystem

[TOC]

## 一言蔽之





## 初始化入口

```java
public class DefaultActorService implements ActorService {

    public static final String APP_DISPATCHER_NAME = "app-dispatcher";
    public static final String RULE_DISPATCHER_NAME = "rule-dispatcher";
    public static final String ENGINE_DISPATCHER_NAME = "engine-dispatcher";
...    
}
```

初始化具体逻辑

1. 创建ActorSystem的设置
2. 初始化app-dispatcher
3. 初始化rule-dispatcher
4. 初始化engine-dispatcher
5. 设置到actorContext
6. 创建根Actor
7. 设置根Actor到actorContext

```java
@PostConstruct
public void initActorSystem() {
  log.info("Initializing actor system.");
  actorContext.setActorService(this);
  ActorSystemSettings settings = new ActorSystemSettings(actorThroughput, schedulerPoolSize, maxActorInitAttempts);
  system = new DefaultActorSystem(settings);
  //初始化APP 分发器 , size = 1
  system.createDispatcher(APP_DISPATCHER_NAME, initDispatcherExecutor(APP_DISPATCHER_NAME, appDispatcherSize));
  //初始化规则分发器
  system.createDispatcher(RULE_DISPATCHER_NAME, initDispatcherExecutor(RULE_DISPATCHER_NAME, ruleDispatcherSize));
  //初始化引擎分发器
  system.createDispatcher(ENGINE_DISPATCHER_NAME, initDispatcherExecutor(ENGINE_DISPATCHER_NAME, engineDispatcherSize));

  actorContext.setActorSystem(system);

  appActor = system.createRootActor(APP_DISPATCHER_NAME, new AppActor.ActorCreator(actorContext));
  actorContext.setAppActor(appActor);

  log.info("Actor system initialized.");
}

```

## 监听应用

```java
@EventListener(ApplicationReadyEvent.class)
public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    log.info("Received application ready event. Sending application init message to actor system");
  	//发送应用初始化消息
    appActor.tellWithHighPriority(new AppInitMsg());
}
```

这一部分在后续章节完善