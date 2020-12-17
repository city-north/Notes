# Spring基于注解编程

- SpringFramewrok-1.X注解启蒙时代
- [SpringFramewrok-2.X注解驱动过渡时代](#SpringFramewrok-2.X注解驱动过渡时代)
- SpringFramework-2.5引入新的骨架式 Annnotation
- SpringFramework-3.x注解驱动黄金时代
- SpringFramework4.x注解驱动完善时代

- SpringFramework-5.0时代，注解驱动成熟时代

## SpringFramewrok-1.X注解启蒙时代

Spring framework 1.2.0 版本开始，开始支持 Annnotation ，虽然框架层面均已支持 

- @ManagedResource

  > 资源管理注解

- @Transactional

但是主要还是以

 

## SpringFramewrok-2.X注解驱动过渡时代

- @Required 

- @Controller

- @RequestMapping

- @ModelAttribute

- JSR-250

  - @Resource

  - @PostContruct

    > 代替` <bean init-method=''>`

  - @PostDestory

    > 代替`<bean destory-method="">`

    尽管


### SpringFramework-3.x注解驱动黄金时代

Spring Framework3.x 是一个里程碑式的时代，3.0 除了提升Spring模式注解的“派生”层次性，首要任务是替换XML配置方式，引入配置类注解 @Configuration 