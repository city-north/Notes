# Docker 安装 Tomcat

- 拉取镜像
```
docker pull tomcat:8
```
- 创建Tomcat 容器
```
docker run -id --name=mxg_tomcat -p 8888:8080 -v
/usr/local/project:/usr/local/tomcat/webapps --privileged=true tomcat:8
```
格式化一下:
```
docker run -id 
--name=tomcat8  --容器名字 
-p 8888:8080 
-v /usr/local/project:/usr/local/tomcat/webapps -- 映射 webapps文件夹
-v /usr/local/conf:/usr/local/tomcat/conf -- 映射配置文件目录
--privileged=true --container内的root拥有真正的root权限。
tomcat:8

```