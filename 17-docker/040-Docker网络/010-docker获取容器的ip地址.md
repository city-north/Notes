# docker获取容器的ip地址

```
docker inspect NAMES 
```

# 查看容器所有状态信息；

```
docker inspect --format='{{.NetworkSettings.IPAddress}}' ID/NAMES
```
# 查看 容器ip 地址
```
docker inspect --format '{{.Name}} {{.State.Running}}' NAMES
```
# 查看 容器ip 地址
```
docker inspect --format='{{.Name}} - {{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)
```