# 启动Tomcat

我们平时启动Tomcat过程是怎么样的？

1. 复制WAR包至Tomcat webapp 目录。
2. 执行starut.bat 脚本启动。
3. 启动过程中war 包会被自动解压装载。

但是我们在Eclipse 或idea 中启动WEB项目的时候 也是把War包复杂至webapps 目录解压吗？

显然不是，其真正做法是在Tomcat程序文件之外创建了一个部署目录，在一般生产环境中也是这么做的 即：

Tomcat 程序目录和部署目录分开 。我们只需要在启动时指定CATALINA_HOME 与  CATALINA_BASE 参数即可实现。

| **启动参数**    | **描述说明**                                                 |
| :-------------- | :----------------------------------------------------------- |
| JAVA_OPTS       | jvm 启动参数 , 设置内存  编码等 -Xms100m -Xmx200m -Dfile.encoding=UTF-8 |
| JAVA_HOME       | 指定jdk 目录，如果未设置从java 环境变量当中去找。            |
| CATALINA_HOME   | Tomcat 程序根目录                                            |
| CATALINA_BASE   | 应用部署目录，默认为$CATALINA_HOME                           |
| CATALINA_OUT    | 应用日志输出目录：默认$CATALINA_BASE/log                     |
| CATALINA_TMPDIR | 应用临时目录：默认：$CATALINA_BASE/temp                      |

可以编写一个脚本 来实现自定义配置：

**「更新 启动 脚本」**：

```
#!/bin/bash 
export JAVA_OPTS="-Xms100m -Xmx200m"
export JAVA_HOME=/root/svr/jdk/
export CATALINA_HOME=/root/svr/apache-tomcat-7.0.81
export CATALINA_BASE="`pwd`"

case $1 in
        start)
        $CATALINA_HOME/bin/catalina.sh start
                echo start success!!
        ;;
        stop)
                $CATALINA_HOME/bin/catalina.sh stop
                echo stop success!!
        ;;
        restart)
        $CATALINA_HOME/bin/catalina.sh stop
                echo stop success!!
                sleep 3
        $CATALINA_HOME/bin/catalina.sh start
        echo start success!!
        ;;
        version)
        $CATALINA_HOME/bin/catalina.sh version
        ;;
        configtest)
        $CATALINA_HOME/bin/catalina.sh configtest
        ;;
        esac
exit 0
```

**「自动部署脚本」**：

```
#!/bin/bash -e
export now_time=$(date +%Y-%m-%d_%H-%M-%S)
echo "deploy time:$now_time"

app=$1
version=$2
mkdir -p war/
#从svn下载程序至 war目录
war=war/${app}_${version}.war
echo "$war"
svn export svn://192.168.0.253/release/${app}_${version}.war $war

deploy_war() {
#解压版本至当前目录
target_dir=war/${app}_${version}_${now_time}
unzip -q $war -d $target_dir
rm -f appwar
ln -sf $target_dir appwar
target_ln=`pwd`/appwar
echo '<?xml version="1.0" encoding="UTF-8" ?>
<Context docBase="'$target_ln'" allowLinking="false">
</Context>' > conf/Catalina/localhost/ROOT.xml
#重启Tomcat服务
./tomcat.sh restart
}

deploy_war
```

##  