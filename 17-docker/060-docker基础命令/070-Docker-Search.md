# Docker Search

## 语法

```
docker search [option] keyword
```
- `-f` 过滤输出内容
- `-- filter` 过滤输出内容
- `limit` 限制输出结果个数,默认是 25
- `no-trunc` 不截断输出结果


##实例

- 查询官方版的镜像

```
docker search --filter=is-official=true nginx

```

- 查询收藏数超过 4 的关键字为 nginx 的镜像

```
docker search --filter=stars=4 nginx
```