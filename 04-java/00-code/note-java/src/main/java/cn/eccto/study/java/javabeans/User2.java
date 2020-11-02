package cn.eccto.study.java.javabeans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/11/02 14:33
 */
public class User2 {

    private String username;
    private Integer age;
    /**
     * 属性（否决）变化监听器
     */
    private VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);
    /**
     * 启动属性（否决）变化
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    private void fireVetoableChange(String propertyName, String oldValue, String newValue) throws PropertyVetoException {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        vetoableChangeSupport.fireVetoableChange(event);
    }

    /**
     * 添加属性（否决）变化监听器
     */
    public void addVetoableChangeListener(VetoableChangeListener listener){
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    /**
     * 删除属性（否决）变化监听器
     */
    public void removeVetoableChangeListener(VetoableChangeListener listener){
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    public void setUsername(String username) throws PropertyVetoException {
        String oldValue = this.username;
        fireVetoableChange("username",oldValue,username);
        this.username = username;
        firePropertyChange("username", oldValue, username);
    }
    /**
     * 启动属性（生效）变化
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    private void firePropertyChange(String propertyName, String oldValue, String newValue) throws PropertyVetoException {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        vetoableChangeSupport.fireVetoableChange(event);
    }
}
