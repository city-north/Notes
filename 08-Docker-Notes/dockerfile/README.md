[返回目录](/README.md)

# 一个新的运行环境

之前，你想写一个Python程序，第一步就是安装Python运行环境。有一个问题，就是在本地配置的环境，在生产环境也应该一致。

使用Docker，你可以直接使用Python运行环境的image,不需要安装，DockerFile可以帮助我们创建这些灵活的image.

## 使用Dockerfile创建一个容器

dockerfile 定义了在容器内部的操作。像网络接口一样获取到资源，磁盘被虚拟化安装在环境中，直接与你的系统关联，所以你需要映射一个端口，使得容器可以访问外部世界，然后指定外部环境中你想复制的文件。当然，在你做完这些之后，Dockerfile定义了什么，他就会运行什么

我们来创建一个文件，交做Dockerfile:

```
# Use an official Python runtime as a parent image
# 使用官方的Python运行环境作为母镜像
FROM python:2.7-slim

# Set the working directory to /app
# 设置工作目录为 /app
WORKDIR /app

# Copy the current directory contents into the container at /app
# 把当前目录中的内容拷贝进容器中的/app 目录
ADD . /app

# Install any needed packages specified in requirements.txt
# 安装requirements.txt 指定的，特定的包
RUN pip install --trusted-host pypi.python.org -r requirements.txt

# Make port 80 available to the world outside this container
# 开放80端口与外界
EXPOSE 80

# Define environment variable
#定义环境变量
ENV NAME World

# Run app.py when the container launches
#运行app.py 运行程序，当容器启动
CMD ["python", "app.py"]
```

上面dockerfile中，提到了两个文件`app.py`和`requirements.txt`

在相同文件夹创建这两个文件，当Dockerfile 被build进镜像时，这两个文件也会被加入，因为dockerfile中的 add 命令，app.py的输出可以被获取到，是因为我们使用了EXPOSE命令开放了端口。

### `requirements.txt:` {#requirementstxt}

```
Flask
Redis
```

### `app.py:` {#apppy}

```
from flask import Flask
from redis import Redis, RedisError
import os
import socket

# Connect to Redis
redis = Redis(host="redis", db=0, socket_connect_timeout=2, socket_timeout=2)

app = Flask(__name__)

@app.route("/")
def hello():
    try:
        visits = redis.incr("counter")
    except RedisError:
        visits = "<i>cannot connect to Redis, counter disabled</i>"

    html = "<h3>Hello {name}!</h3>" \
           "<b>Hostname:</b> {hostname}<br/>" \
           "<b>Visits:</b> {visits}"
    return html.format(name=os.getenv("NAME", "world"), hostname=socket.gethostname(), visits=visits)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=80)
```

可以看到在Dockerfile中的第四个命令，pip install -r requirements.txt 安装可Flask 和 Redis .

然后当调用socket.gethostname\(\)的时候，程序输出变量NAME,因为我们只安装了reids 的python library，而不是reids，所以，运行到这一步的时候，会报错。

你不需要requirement.txt文件中的任何环境安装在你的主机，也没有运行一个装了它们的镜像，看上去你好像没有建立Python和Flask运行环境，但是你已经建立了。

## build

```
docker build -t friendlyhello .
```

输出：

```
[root@localhost DockerFileDemo]# docker build -t friendlyhello
"docker build" requires exactly 1 argument.
See 'docker build --help'.

Usage:  docker build [OPTIONS] PATH | URL | - [flags]

Build an image from a Dockerfile
[root@localhost DockerFileDemo]# docker build -t friendlyhello .
Sending build context to Docker daemon  4.608kB
Step 1/7 : FROM python:2.7-slim
 ---> 7302ff8cae7a
Step 2/7 : WORKDIR /app
Removing intermediate container 6c0142e4da08
 ---> 70f2fcb9d189
Step 3/7 : ADD . /app
 ---> 26bbfca41cfb
Step 4/7 : RUN pip install --trusted-host pypi.python.org -r requirements.txt
 ---> Running in 26dd635558d9
Collecting Flask (from -r requirements.txt (line 1))
  Downloading https://files.pythonhosted.org/packages/7f/e7/08578774ed4536d3242b14dacb4696386634607af824ea997202cd0edb4b/Flask-1.0.2-py2.py3-none-any.whl (91kB)
Collecting Redis (from -r requirements.txt (line 2))
  Downloading https://files.pythonhosted.org/packages/3b/f6/7a76333cf0b9251ecf49efff635015171843d9b977e4ffcf59f9c4428052/redis-2.10.6-py2.py3-none-any.whl (64kB)
Collecting itsdangerous>=0.24 (from Flask->-r requirements.txt (line 1))
  Downloading https://files.pythonhosted.org/packages/dc/b4/a60bcdba945c00f6d608d8975131ab3f25b22f2bcfe1dab221165194b2d4/itsdangerous-0.24.tar.gz (46kB)
Collecting Jinja2>=2.10 (from Flask->-r requirements.txt (line 1))
  Downloading https://files.pythonhosted.org/packages/7f/ff/ae64bacdfc95f27a016a7bed8e8686763ba4d277a78ca76f32659220a731/Jinja2-2.10-py2.py3-none-any.whl (126kB)
Collecting Werkzeug>=0.14 (from Flask->-r requirements.txt (line 1))
  Downloading https://files.pythonhosted.org/packages/20/c4/12e3e56473e52375aa29c4764e70d1b8f3efa6682bef8d0aae04fe335243/Werkzeug-0.14.1-py2.py3-none-any.whl (322kB)
Collecting click>=5.1 (from Flask->-r requirements.txt (line 1))
  Downloading https://files.pythonhosted.org/packages/34/c1/8806f99713ddb993c5366c362b2f908f18269f8d792aff1abfd700775a77/click-6.7-py2.py3-none-any.whl (71kB)
Collecting MarkupSafe>=0.23 (from Jinja2>=2.10->Flask->-r requirements.txt (line 1))
  Downloading https://files.pythonhosted.org/packages/4d/de/32d741db316d8fdb7680822dd37001ef7a448255de9699ab4bfcbdf4172b/MarkupSafe-1.0.tar.gz
Building wheels for collected packages: itsdangerous, MarkupSafe
  Running setup.py bdist_wheel for itsdangerous: started
  Running setup.py bdist_wheel for itsdangerous: finished with status 'done'
  Stored in directory: /root/.cache/pip/wheels/2c/4a/61/5599631c1554768c6290b08c02c72d7317910374ca602ff1e5
  Running setup.py bdist_wheel for MarkupSafe: started
  Running setup.py bdist_wheel for MarkupSafe: finished with status 'done'
  Stored in directory: /root/.cache/pip/wheels/33/56/20/ebe49a5c612fffe1c5a632146b16596f9e64676768661e4e46
Successfully built itsdangerous MarkupSafe
Installing collected packages: itsdangerous, MarkupSafe, Jinja2, Werkzeug, click, Flask, Redis
Successfully installed Flask-1.0.2 Jinja2-2.10 MarkupSafe-1.0 Redis-2.10.6 Werkzeug-0.14.1 click-6.7 itsdangerous-0.24
Removing intermediate container 26dd635558d9
 ---> d0f88bc99b1a
Step 5/7 : EXPOSE 80
 ---> Running in bc30d8bdee2b
Removing intermediate container bc30d8bdee2b
 ---> b0049db2ad79
Step 6/7 : ENV NAME World
 ---> Running in f060aa83d7b4
Removing intermediate container f060aa83d7b4
 ---> 0cf5eaf778f1
Step 7/7 : CMD ["python", "app.py"]
 ---> Running in 90e83beb3ca3
Removing intermediate container 90e83beb3ca3
 ---> 6a33221dce81
Successfully built 6a33221dce81
Successfully tagged friendlyhello:latest
```

## 查看镜像

`docker image ls`

`REPOSITORY            TAG                 IMAGE ID`

`friendlyhello         latest              326387cea398`

## 运行镜像

```
docker run -p  4000:80 friendlyhello
```

输出：

```
[root@localhost DockerFileDemo]# docker run -p 4000:80 friendlyhello
 * Serving Flask app "app" (lazy loading)
 * Environment: production
   WARNING: Do not use the development server in a production environment.
   Use a production WSGI server instead.
 * Debug mode: off
 * Running on http://0.0.0.0:80/ (Press CTRL+C to quit)
```

![](/assets/import02.png)

静默运行：

```
docker run -d -p 4000:80 friendlyhello
```

## 如何分享这个刚刚构建的image

我们可以上传我们构建的image到注册中心，docker CLI 默认使用Docker公共镜像库

### 登录你的Docker ID

如果没有docker账户，注册：[hub.docker.com](https://hub.docker.com/)

```
docker login
```

### 打标

```
docker tag image username/repository:tag
```

例子：

```
docker tag friendlyhello  ericchen1688/get-started:part2
```

### 发布

```
docker push username/repository:tag
```

例子:

```
docker push ericchen1688/get-started:part2
```

### 运行

当在本地找不到时，回去网上仓库中拉取

```
docker run -p 4000:80 ericchen1688/get-started:part2
Unable to find image 'ericchen1688/get-started:part2' locally
part2: Pulling from ericchen1688/get-started
10a267c67f42: Already exists
f68a39a6a5e4: Already exists
9beaffc0cf19: Already exists
3c1fe835fb6b: Already exists
4c9f1fa8fcb8: Already exists
ee7d8f576a14: Already exists
fbccdcced46e: Already exists
Digest: sha256:0601c866aab2adcc6498200efd0f754037e909e5fd42069adeff72d1e2439068
Status: Downloaded newer image for ericchen1688/get-started:part2
 * Running on http://0.0.0.0:80/ (Press CTRL+C to quit)
```

![](/assets/import03.png)



