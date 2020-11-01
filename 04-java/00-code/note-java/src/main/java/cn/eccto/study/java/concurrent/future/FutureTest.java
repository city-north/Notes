package cn.eccto.study.java.concurrent.future;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Stream;

/**
 * <p>
 * 寻找包含指定关键字的文件
 * </p>
 *
 * @author EricChen 2020/10/31 12:17
 */
public class FutureTest {
    private static String dir = "/Users/ec/study/Notes/04-java/00-code/note-java/src/main/java/cn/eccto/study/java/concurrent";


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LeafRenameTask leafRenameTask = new LeafRenameTask(new File(dir));
        FutureTask<Integer> futureTask = new FutureTask(leafRenameTask);
        new Thread(futureTask).start();
        final Integer integer = futureTask.get();
        System.out.println(String.format("一共重命名了%s 个文件", integer));
    }


}


class LeafRenameTask implements Callable<Integer> {

    private final File directory;
    private int count;

    public LeafRenameTask(File directory) {
        this.directory = directory;
    }

    @Override
    public Integer call() throws Exception {
        final File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        List<Future<Integer>> answer = new ArrayList<>();
        Stream.of(files).forEach(file -> {
            if (file.isDirectory()) {
                LeafRenameTask leafRenameTask = new LeafRenameTask(file);
                FutureTask<Integer> futureTask = new FutureTask<>(leafRenameTask);
                answer.add(futureTask);
                new Thread(futureTask).start();
            } else {
                rename(file);

            }
        });
        for (Future<Integer> integerFuture : answer) {
            count += integerFuture.get();
        }
        return count;
    }

    private void rename(File file) {
        FileSuffix suffix = getSuffix(file);
        if (suffix == FileSuffix.OTHER) {
            return;
        }
        String absolutePath = file.getAbsolutePath();
        String targetPath = getTargetPath(absolutePath, suffix);
        System.out.println(absolutePath + " || rename to || " + targetPath);
        file.renameTo(new File(targetPath));
        count++;
    }

    private String getTargetPath(String absolutePath, FileSuffix suffix) {
        StringBuilder sb = new StringBuilder(absolutePath);
        switch (suffix) {
            case SCREEN:
                sb.replace(sb.length() - 6, sb.length(), "lview");
                break;
            case BM:
                sb.replace(sb.length() - 2, sb.length(), "lwm");
                break;
            case SVC:
                sb.replace(sb.length() - 3, sb.length(), "lsc");
                break;
        }
        return sb.toString();
    }

    private FileSuffix getSuffix(File file) {
        if (file != null && file.isFile() && file.canRead()) {
            String name = file.getName();
            int index = name.lastIndexOf('.');
            if (index < 0 || index + 1 > name.length()) {
                return FileSuffix.OTHER;
            }
            String substring = name.substring(index + 1);
            switch (substring.toLowerCase(Locale.CHINA)) {
                case "screen":
                    return FileSuffix.SCREEN;
                case "bm":
                    return FileSuffix.BM;
                case "svc":
                    return FileSuffix.SVC;
                default:
                    return FileSuffix.OTHER;
            }
        }
        return FileSuffix.OTHER;
    }

    enum FileSuffix {
        BM(),
        SCREEN(),
        SVC(),
        OTHER();
    }

}