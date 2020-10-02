#数据库表

Activiti 默认有 28 张表
![](https://www.showdoc.cc/server/api/common/visitfile/sign/65f981e4ab9e5b499b21aee65450427f?showdoc=.jpg)

## 分类

| 序号 | Service名称 | 缩写全称   | 主要作用                                                     | 对应Service       |
| ---- | ----------- | ---------- | ------------------------------------------------------------ | ----------------- |
| 1    | ACT_RE      | repository | 'RE'表示repository。 这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。 | RepositoryService |
| 2    | ACT_RU      | runtime    | 'RU'表示runtime。 这些运行时的表，包含流程实例，任务，变量，异步任务，等运行中的数据。 Activiti只在流程实例执行过程中保存这些数据， 在流程结束时就会删除这些记录。 这样运行时表可以一直很小速度很快。 | RuntimeService    |
| 3    | ACT_ID      | identity   | 'ID'表示identity。 这些表包含身份信息，比如用户，组等等。    | IdentityService   |
| 4.   | ACT_HI      | history    | 'HI'表示。 这些表包含历史数据，比如历史流程实例， 变量，任务等等。 | HistoryService    |
| 5    | ACT_GE      | general    | 通用数据，general 缩写，用于不同场景下，如存放资源文件。     |                   |