# 045-缓冲区-Buffer-内存映射MappedBuffer.md

[TOC]

## 简介

内存映射是读和写文件数据的方法， 它可以比常规的基于流或者基于通道的IO快的多，内存映射IO是通过使文件中的数据出现为内存数组的内容来完成的，只有文件实际读取的内容或者写入的部分才会映射到内存里

```java
/**
 * IO映射缓冲区
 */
public class MappedBuffer {  
    static private final int start = 0;
    static private final int size = 26;
      
    static public void main( String args[] ) throws Exception {  
        RandomAccessFile raf = new RandomAccessFile( "E://test.txt", "rw" );
        FileChannel fc = raf.getChannel();
        
        //把缓冲区跟文件系统进行一个映射关联
        //只要操作缓冲区里面的内容，文件内容也会跟着改变
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,start, size );
          
        mbb.put( 0, (byte)97 );  //a
        mbb.put( 25, (byte)122 );   //z

        raf.close();  
    }  
}
```