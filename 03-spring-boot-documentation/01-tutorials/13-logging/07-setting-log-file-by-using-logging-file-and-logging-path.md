# Setting log file by using logging.file and logging.path properties

使用`logging.file`和`logging.path`设置日志文件

- **(1)logging.file=my-file.txt**This will write logs to my-file.txt at the location where application is running (the working directory). Within an application, the working directory can be found by `System.getProperty("user.dir")`.
- **(2)logging.file=/my-folder/my-file.txt**In this case, the location will be the root folder in the current partition. For example if we are running in windows and application is running somewhere in the directory D: then the log file will be in D:\my-folder\my-file.txt. In Linux, it will be created under the root directory where only root user has the permission (sudo commands applied). In both cases, the user who is running the application should have the permission to create files, otherwise no log files will be created. In windows, we can also use paths like D:/my-folder/my-file.txt or D:\\my-folder\\my-file.txt (not recommended).
- **(3)logging.path=/my-folder/**It will write logs to /myfolder/spring.log in the root directory.
  If we use both logging.path and logging.file then logging.path property will be ignored.