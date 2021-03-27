# 021-通道-Channel-文件复制

[TOC]

## FileChannel - 文件通道

FileChannel 可以读取数据也可以写入数据， 但是为阻塞模式，不支持非阻塞模式

```java
//文件输入流
FileInputStream fis = new FileInputStream(srcFile);
//文件输入流通道
FileChannel inChannel = fis.getChannel();
//文件输出流
FileOutputStream fos = new FileOutputStream(destFile);
//文件输出流通道
FileChannel outchannel = fos.getChannel();
```

根据文件随机访问类， 获取FileChannel通道

```java
RandomAccessFile aFile = new RandomAccessFile(fileName, "rw");
FileChannel inChannel = aFile.getChannel();
```

## 需要注意的是

- 在实例代码中，外层的每一次while循环， 都需要两次模式 ByteBuffer切换
  1. 第一次切换时， 反转buf， 进入读取模式
  2. 第二次切换时， 清除buf,    进入写入模式
- 此方法只演示使用， 效率低， 效率高使用 transferFrom方法， 见FileNIOFastCopyDemo

## FileNIOCopyDemo

```Java
public class FileNIOCopyDemo {
    /**
     * 演示程序的入口函数
     *
     * @param args
     */
    public static void main(String[] args) {
        //演示复制资源文件
        nioCopyResouceFile();
    }


    /**
     * 复制两个资源目录下的文件
     */
    public static void nioCopyResouceFile() {
        String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcPath = IOUtil.getResourcePath(sourcePath);
        Logger.debug("srcPath=" + srcPath);

        String destShortPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destdePath = IOUtil.builderResourcePath(destShortPath);
        Logger.debug("destdePath=" + destdePath);

        nioCopyFile(srcPath, destdePath);
    }


    /**
     * 复制文件
     * @param srcPath
     * @param destPath
     */
    public static void nioCopyFile(String srcPath, String destPath) {

        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        try {
            //如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }


            long startTime = System.currentTimeMillis();

            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outchannel = null;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outchannel = fos.getChannel();

                int length = -1;
                ByteBuffer buf = ByteBuffer.allocate(1024);
                //从输入通道读取到buf
                while ((length = inChannel.read(buf)) != -1) {

                    //翻转buf,变成成读模式
                    buf.flip();

                    int outlength = 0;
                    //将buf写入到输出的通道
                    while ((outlength = outchannel.write(buf)) != 0) {
                        System.out.println("写入字节数：" + outlength);
                    }
                    //清除buf,变成写入模式
                    buf.clear();
                }


                //强制刷新磁盘
                outchannel.force(true);
            } finally {
                IOUtil.closeQuietly(outchannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            Logger.debug("base 复制毫秒数：" + (endTime - startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
```

## FileNIOFastCopyDemo

```java
public class FileNIOFastCopyDemo {

    public static void main(String[] args) {
        //演示复制资源文件
        nioCopyResouceFile();
    }

    /**
     * 复制两个资源目录下的文件
     */
    public static void nioCopyResouceFile() {
        String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcDecodePath = IOUtil.getResourcePath(sourcePath);
        Logger.debug("srcDecodePath=" + srcDecodePath);

        String destPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destDecodePath = IOUtil.builderResourcePath(destPath);
        Logger.debug("destDecodePath=" + destDecodePath);

        nioCopyFile(srcDecodePath, destDecodePath);
    }


    /**
     * 复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void nioCopyFile(String srcPath, String destPath) {

        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        try {
            //如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }


            long startTime = System.currentTimeMillis();

            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outChannel = null;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outChannel = fos.getChannel();
                long size = inChannel.size();
                long pos = 0;
                long count = 0;
                while (pos < size) {
                    //每次复制最多1024个字节，没有就复制剩余的
                    count = size - pos > 1024 ? 1024 : size - pos;
                    //复制内存,偏移量pos + count长度
                    pos += outChannel.transferFrom(inChannel, pos, count);
                }
                //强制刷新磁盘
                outChannel.force(true);
            } finally {
                IOUtil.closeQuietly(outChannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            Logger.debug("base 复制毫秒数：" + (endTime - startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
```