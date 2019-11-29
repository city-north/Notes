package cn.eccto.study.springframework.tutorials.spring5;

/**
 * description
 *
 * @author EricChen 2019/11/27 18:06
 */
public interface LogService {
    void log(String msg);

    class LogServiceImpl implements LogService {
        public LogServiceImpl() {
            System.out.printf("instance of %s created: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }

        @Override
        public void log(String msg) {
            System.out.println(msg);
        }

        private void init() {
            System.out.printf("%s, init method called: ", this.getClass().getName(),
                    System.identityHashCode(this));
        }
    }
}