# Docker rm 删除容器
语法:
```
docker [container] rm [-f|--force] [-l|--link][-v|--volumes] CONTAINER
[CONTAINER...]
```
参数
- `-f`, `--force=false`:是否强行终止并删除一个运行中的容器
- `-l`, `--link=false` :删除容器的链接,但是保留容器
- `-v`, `--volumes=false` : 删除容器挂载的数据卷

## 实例

```
docker rm ce55

```