package cn.eccto.study.java.concurrent.stampedLock;

import java.util.concurrent.locks.StampedLock;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/07/20 15:24
 */
public class Point {
    private double x, y;

    private final StampedLock stampedLock = new StampedLock();

    /**
     * 排它锁 - 写锁
     */
    void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlock(stamp);
        }
    }

    /**
     * 乐观读锁 和悲观读锁的使用
     */
    double distanceFromOrigin() {
        // ④ 尝试获取乐观读锁
        long stamp = stampedLock.tryOptimisticRead();
        // ⑤ 将变量复制到方法栈中
        double currentX = x, currentY = y;
        //检查④ 获取乐观锁后,锁有没有被排他性抢占
        if (!stampedLock.validate(stamp)) {
            //如果被抢占了,获取一个悲观读锁
            stamp = stampedLock.readLock();
            try{
                currentX = x;
                currentY = y;
            }finally {
                stampedLock.unlockRead(stamp);
            }

        }
        //返回计算结果
        return Math.sqrt(currentX * currentX  + currentY * currentY);

    }





}
