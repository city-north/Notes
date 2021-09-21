package vip.Jonathan.study.io.files;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2021/1/4 21:35
 */
public class ZipInputStreamTest {

    @Test
    public void testReadZipFile() throws Exception {
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream("F:\\教学视频\\培训计划\\leaf-hntc-dev_qingpu.zip"));
        ZipEntry nextEntry = null;
        while ((nextEntry = zipIn.getNextEntry()) != null) {
            System.out.println(nextEntry.getName());
        }
        zipIn.close();
    }
}
