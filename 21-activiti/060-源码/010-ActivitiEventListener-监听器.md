- # ActivitiEventListener

  ## 继承结构

  ![img](https://www.showdoc.cc/server/api/common/visitfile/sign/36cb81f143608dc61fdcee2ecc27d719?showdoc=.jpg)

  ## BaseEntityEventListener

  基础实体监听器

  ```
  public class BaseEntityEventListener implements ActivitiEventListener {
  
    protected boolean failOnException;
    protected Class<?> entityClass;
  
  //省略
  
    @Override
    public final void onEvent(ActivitiEvent event) {
      if (isValidEvent(event)) {
        // Check if this event
        if (event.getType() == ActivitiEventType.ENTITY_CREATED) {
          onCreate(event);
        } else if (event.getType() == ActivitiEventType.ENTITY_INITIALIZED) {
          onInitialized(event);
        } else if (event.getType() == ActivitiEventType.ENTITY_DELETED) {
          onDelete(event);
        } else if (event.getType() == ActivitiEventType.ENTITY_UPDATED) {
          onUpdate(event);
        } else {
          // Entity-specific event
          onEntityEvent(event);
        }
      }
      /**
     * Called when an entity create event is received.
     */
    protected void onCreate(ActivitiEvent event) {
      // Default implementation is a NO-OP
    }
    }
  
  //省略
  ```

  - 只监听了创建/初始化/删除/更新
  - 如果我们要实现简单的监听,集成该类实现对应方法即可

  值得注意的是:

  可以看到,在`OnEvent()`方法中对 Event类型进行路由,每个事件类型会调用指定的方法,具体的实现由子类完成