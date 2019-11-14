package cn.eccto.study.springframework.tutorials.dependson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 事件管理器
 *
 * @author qiang.chen04@hand-china.com 2019/11/14 14:25
 */
public class EventManager {
    private final List<Consumer<String>> listeners = new ArrayList<>();

    private EventManager() {
    }

    private static final class SingletonHolder {
        private static final EventManager INSTANCE = new EventManager();
    }

    public static EventManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void publish(final String message) {
        listeners.forEach(l -> l.accept(message));
    }

    public void addListener(Consumer<String> eventConsumer) {
        listeners.add(eventConsumer);
    }
}


