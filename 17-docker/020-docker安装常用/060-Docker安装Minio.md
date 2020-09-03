# Docker 安装 Minio

- 拉取镜像：# docker pull minio/minio:RELEASE.2018-05-25T19-49-13Z
- 启动容器，端口 9000：
```
# docker run -d -p 9000:9000 --name minio -v /hzero/data-server/minio/data:/data -v /hzero/data-server/minio/config:/root/.minio 324b server /data
```
 -获取用户名和密钥：# docker logs minio