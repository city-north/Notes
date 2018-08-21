[返回目录](/README.md)

## 进入容器

### attach命令：

```
sudo docker attach [containerID]
```

* 多个窗口同时attach到同一个容器的时候，所有窗口都会同步显示
* 当某个窗口因命令阻塞时，其他窗口也无法操作

### exec命令：

```
sudo docker exec -ti [containerID] /bin/bash
```





