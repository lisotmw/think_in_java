package generic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

import javax.swing.plaf.basic.BasicArrowButton;

import net.mindview.util.Tuple;
import net.mindview.util.TwoTuple;

class MixinProxy implements InvocationHandler{

	Map<String,Object> delegateByMethod;
	
	public MixinProxy(TwoTuple<Object,Class<?>>... pairs) {
		delegateByMethod = new HashMap<String,Object>();
		for(TwoTuple<Object, Class<?>> pair : pairs) {
			for(Method method :pair.second.getMethods()) {
				String methodName = method.getName();
				// The first interface in the map implements the method
				if (!delegateByMethod.containsKey(methodName)) {
					delegateByMethod.put(methodName, pair.first);
				}
			}
		}
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		String methodName = method.getName();
		Object delegate = delegateByMethod.get(methodName);
		return method.invoke(delegate, args);
	}
	@SuppressWarnings("unchecked")
	public static Object newInstance(TwoTuple... pairs) {
		Class<?>[] interfaces = new Class[pairs.length];
		for(int i = 0;i < pairs.length; i++) {
			interfaces[i] = (Class<?>)pairs[i].second;
		}
		ClassLoader classLoader = pairs[0].first.getClass().getClassLoader();
		return Proxy.newProxyInstance(classLoader, interfaces, new MixinProxy(pairs));
	}
}

interface Basic{
	public void set(String val);
	public String get();
}
class BasicImp implements Basic{
	private String value;
	public void set(String val) {value = val;}
	public String get() {return value;}
}

interface SerialNumbered { long getSerialNumber();}
class SerialNumberedImp implements SerialNumbered{
	private static long counter = 1;
	private final  long serialNumber = counter++;
	public long getSerialNumber() {return serialNumber;}
}

interface TimeStamped{ long getStamp();}
class TimeStampedImp implements TimeStamped{
	private final long timeStamp;
	public TimeStampedImp() { this.timeStamp = new Date().getTime();}
	public long getStamp() { return timeStamp;}
}
public class DynamicProxyMixin {
	public static void main(String[] args) {
		Object mixin = MixinProxy.newInstance(
				Tuple.tuple(new BasicImp(), Basic.class),
				Tuple.tuple(new TimeStampedImp(), TimeStamped.class),
				Tuple.tuple(new SerialNumberedImp(), SerialNumbered.class));
		Basic b = (Basic)mixin;
		TimeStamped t = (TimeStamped)mixin;
		SerialNumbered s = (SerialNumbered) mixin;
		b.set("hello");
		System.out.println(b.get());
		System.out.println(t.getStamp());
		System.out.println(s.getSerialNumber());
	}
}
