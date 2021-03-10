# 020-act_re_model-流程定义信息

| order | 列名                               | 列类型          | 注释             |
| :---- | :--------------------------------- | :-------------- | :--------------- |
| 1     | id\_                               | varchar\(64\)   |                  |
| 2     | rev\_                              | int\(11\)       |                  |
| 3     | name\_                             | varchar\(255\)  |                  |
| 4     | key\_                              | varchar\(255\)  |                  |
| 5     | category\_                         | varchar\(255\)  |                  |
| 6     | create\_time\_                     | timestamp\(3\)  |                  |
| 7     | last\_update\_time\_               | timestamp\(3\)  |                  |
| 8     | version\_                          | int\(11\)       |                  |
| 9     | meta\_info\_                       | varchar\(4000\) |                  |
| 10    | deployment\_id\_                   | varchar\(64\)   |                  |
| 11    | editor\_source\_value\_id\_        | varchar\(64\)   |                  |
| 12    | editor\_source\_extra\_value\_id\_ | varchar\(64\)   |                  |
| 13    | tenant\_id\_                       | varchar\(255\)  |                  |
| 14    | document\_id\_                     | bigint\(20\)    | 单据ID           |
| 15    | overtime\_                         | int\(11\)       | 流程超时时间     |
| 16    | overtime\_unit\_                   | varchar\(30\)   | 流程超时时间单位 |
| 17    | overtime\_enabled\_flag\_          | tinyint\(4\)    | 超时提醒启用标示 |