package org.whitneyrobotics.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

public class StateRegistrar {

    public static class HardwareHandler<T extends HardwareDevice> implements InvocationHandler {
        private T device;
        public HardwareHandler(T obj){
            this.device = obj;
        };
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().startsWith("set")) {
                markBusy(device);
            }
            return method.invoke(device, args);
        };
    }

    public static void markBusy(HardwareDevice device) {

    }

    public static <T extends HardwareDevice> T fetchHardware(HardwareMap hardwareMap, Class<T> clazz, String name) {
        return registerHardware(hardwareMap.get(clazz, name));
    }

    public static <T extends HardwareDevice> T registerHardware(T hw){
        T wrapped = (T) Proxy.newProxyInstance(
                hw.getClass().getClassLoader(),
                new Class[]{hw.getClass()},
                new HardwareHandler(hw)
        );
        return wrapped;
    }


}
