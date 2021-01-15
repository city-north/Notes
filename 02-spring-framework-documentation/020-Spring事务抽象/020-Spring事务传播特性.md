# 020-Spring事务传播特性

| 传播性                    | 值   | 描述                                   |
| ------------------------- | ---- | -------------------------------------- |
| propagation_required      | 0    | 当前有事务就用当前的,没有就用新的      |
| propagation_supports      | 1    | 事务可有可无,不是必须的                |
| propagation_mandatory     | 2    | 当前一定要有事务,不然就报错            |
| propagation_requires_new  | 3    | 无论是否有事务,都发起一个新的事务      |
| propagation_not_supported | 4    | 不支持事务,按非事物方式运行            |
| propagation_never         | 5    | 不支持事务,如果有事务则抛出异常        |
| propagation_nested        | 6    | 当前有事务就在当前事务里再发起一个事务 |

