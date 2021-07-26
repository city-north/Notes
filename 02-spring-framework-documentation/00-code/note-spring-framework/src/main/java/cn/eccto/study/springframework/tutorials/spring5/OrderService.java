package cn.eccto.study.springframework.tutorials.spring5;

/**
 * description
 *
 * @author JonathanChen 2019/11/27 18:06
 */
public interface OrderService {
    void placeOrder(String item, int qty);

    public static class OrderServiceImpl implements OrderService {

        private LogService logService;

        public OrderServiceImpl() {
            System.out.printf("instance of %s created: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }

        public OrderServiceImpl(LogService logService) {
            this();
            this.logService = logService;
        }

        public void setLogService(LogService logService) {
            this.logService = logService;
        }

        @Override
        public void placeOrder(String item, int qty) {
            System.out.printf("placing order item: %s, qty: %s, isntance: %s%n",
                    item, qty, System.identityHashCode(this));
            if (logService != null) {
                logService.log("Order placed");
            }
        }

        private void init() {
            System.out.printf("%s, init method called: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }

        private void destroy() {
            System.out.printf("%s, destroy method called: %s%n", this.getClass().getName(),
                    System.identityHashCode(this));
        }
    }
}