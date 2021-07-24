package cn.eccto.study.java.javabeans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/11/02 14:27
 */
public class PropertyChangeListenerExample {

    public static void main(String[] args) {
        User user = new User();
        user.setAge(1);
        user.setUsername("zhangsan");
        user.addPropertyChangeListener(new MyPropertyChangeListener());
        user.setUsername("lisi");
        user.setUsername("wangwu");
    }

}

class MyPropertyChangeListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getNewValue());
    }
}
