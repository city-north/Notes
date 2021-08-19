package cn.eccto.study.java.annotation;

import java.awt.event.ActionListener;
import java.lang.reflect.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/11/28 14:25
 */
public class ActionListenerInstaller {

    public static void processAnnotations(Object obj) {
        final Class<?> cls = obj.getClass();
        for (Method declaredMethod : cls.getDeclaredMethods()) {
            final boolean annotationPresent = declaredMethod.isAnnotationPresent(ActionListenerFor.class);
            if (annotationPresent) {
                final ActionListenerFor annotation = declaredMethod.getAnnotation(ActionListenerFor.class);
                final Field declaredField;
                try {
                    declaredField = cls.getDeclaredField(annotation.source());
                    declaredField.setAccessible(true);
                    addListener(declaredField.get(obj), obj, declaredMethod);
                } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void addListener(Object field, final Object param, Method declaredMethod) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(param);
            }
        };
        final Object listener = Proxy.newProxyInstance(null, new Class[]{ActionListener.class}, invocationHandler);
        final Method addActionListener = field.getClass().getMethod("addActionListener", ActionListener.class);
        addActionListener.invoke(field, listener);

    }
}
