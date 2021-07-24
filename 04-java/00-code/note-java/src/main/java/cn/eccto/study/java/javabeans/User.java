package cn.eccto.study.java.javabeans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/10/30 21:57
 */
public class User {
    private String username;

    private Integer age;

    public String getUsername() {
        return username;
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * 属性（生效）变化监听器管理器
     */
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    /**
     * 启动属性（生效）变化
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    private void firePropertyChange(String propertyName, String oldValue, String newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        propertyChangeSupport.firePropertyChange(event);
    }

    /**
     * 添加属性（生效）变化监听器
     */
    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * 删除属性（生效）变化监听器
     */
    public void removePropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * 获取属性（生效）变化监听器
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }

    public void setUsername(String username) {
        String oldValue = this.username;
        this.username = username;
        firePropertyChange("username", oldValue, username);
    }
}
