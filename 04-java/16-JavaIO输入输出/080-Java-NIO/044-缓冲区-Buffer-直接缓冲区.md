# 044-缓冲区-Buffer-直接缓冲区

[TOC]

## 简介

直接缓冲区为了加快IO速度，使用特殊方式为其分配内存的缓冲区

给定一个直接缓冲区，Java虚拟机将尽可能努力直接对它执行本机IO操作， 也就是说， 它会在每一次调用底层操作系统的本地IO之前或者之后，尝试避免将缓冲区的内容拷贝到一个中间缓冲区中或者从一个中间缓冲区中拷贝数据

- 直接使用 allocateDirect() 分配

```java
/**
 * 直接缓冲区
 * Zero Copy 减少了一个拷贝的过程
  */
public class DirectBuffer {  
    static public void main( String args[] ) throws Exception {  

        //在Java里面存的只是缓冲区的引用地址
        //管理效率

       //首先我们从磁盘上读取刚才我们写出的文件内容
        String infile = "E://test.txt";
        FileInputStream fin = new FileInputStream( infile );  
        FileChannel fcin = fin.getChannel();

        //把刚刚读取的内容写入到一个新的文件中
        String outfile = String.format("E://testcopy.txt");
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();  
          
        // 使用allocateDirect，而不是allocate
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);  
          
        while (true) {  
            buffer.clear();  
              
            int r = fcin.read(buffer);  
              
            if (r==-1) {  
                break;  
            }  
              
            buffer.flip();  
              
            fcout.write(buffer);  
        }
   }  
}
```