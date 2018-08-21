[返回目录](/README.md)

## 搜寻镜像

命令：

```
sudo docker search mysql [--automated=false] 
```

* --automated=false 仅显示自动创建的镜像（默认为false）
* --no-trunc=false 输出信息不截断显示
* -s , -- stars =- 仅显示评价为指定星际以上的镜像

例子：

    C:\Users\EricChen>docker search mysql --automated=true
    Flag --automated has been deprecated, use --filter=is-automated=true instead
    NAME                                                   DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
    mysql/mysql-server                                     Optimized MySQL Server Docker images. Create…   447                                     [OK]
    zabbix/zabbix-server-mysql                             Zabbix Server with MySQL database support       99                                      [OK]
    centurylink/mysql                                      Image containing mysql. Optimized to be link…   59                                      [OK]
    zabbix/zabbix-web-nginx-mysql                          Zabbix frontend based on Nginx web-server wi…   51                                      [OK]
    1and1internet/ubuntu-16-nginx-php-phpmyadmin-mysql-5   ubuntu-16-nginx-php-phpmyadmin-mysql-5          35                                      [OK]
    schickling/mysql-backup-s3                             Backup MySQL to S3 (supports periodic backup…   19                                      [OK]
    bitnami/mysql                                          Bitnami MySQL Docker Image                      15                                      [OK]
    zabbix/zabbix-proxy-mysql                              Zabbix proxy with MySQL database support        12                                      [OK]
    dsteinkopf/backup-all-mysql                            backup all DBs in a mysql server                3                                       [OK]
    ansibleplaybookbundle/mysql-apb                        An APB which deploys RHSCL MySQL                0                                       [OK]
    cloudposse/mysql                                       Improved `mysql` service with support for `m…   0                                       [OK]



