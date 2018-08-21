package com.eric.chapter03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 环绕执行模式
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Question {

    /**
     * 这段代码是有限的，只能返回一行，如果想对数据进行处理，基本无法做到
     * 思路：
     *  把processFile方法的行为参数化
     */
    public static String processFile() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("data.txt"))) {
            return br.readLine();
        }
    }
    /**
     * 第二步 使用函数式接口来传递行为
     */
        //创建接口BufferedReaderProcesser

    /**
     * 第三步 执行一个行为
     */
    public static String processFile(BufferedReaderProcessor p) throws IOException{
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
            //你可以processFile主体内，对得到的BufferedReaderProcess调用process方法执行处理
            return p.processFile(br);
        }
    }

    /**
     * 第四步 传递Lambda
     * 现在你就可以通过传递不同的Lambda重用ProcessFile方法，并以不同的方式处理文件
     */
    public static void main(String[] args) throws Exception {
        String l = processFile((BufferedReader br) -> br.readLine());
    }

}
