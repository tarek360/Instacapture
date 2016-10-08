package com.tarek360.instacapture.screenshot.maps;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by tarek on 10/8/16.
 */

public class InterfaceReflection {

  public static Object createInterfaceImpl(Class interfaceClass,
      final MethodInvocationCallback callback) {

    return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
        new java.lang.reflect.InvocationHandler() {

          @Override public Object invoke(Object proxy, Method method, Object[] args)
              throws Throwable {

            callback.invoke(method, args);

            return null;
          }
        });
  }

  public interface MethodInvocationCallback {
    void invoke(Method method, Object[] args);
  }
}
