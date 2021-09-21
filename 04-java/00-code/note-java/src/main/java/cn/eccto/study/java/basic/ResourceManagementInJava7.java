package cn.eccto.study.java.basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * description
 *
 * @author Jonathan 2020/01/18 21:26
 */
public class ResourceManagementInJava7 {
    public static void main(String[] args)
    {
        try (BufferedReader br = new BufferedReader(new FileReader("C:/temp/test.txt")))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                System.out.println(sCurrentLine);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}