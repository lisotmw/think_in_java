package typeinfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理demo
 * @author just4liz
 *
 */
class DynamicProxyHandler implements InvocationHandler{
	private Object proxied;
	public DynamicProxyHandler(Object proxied) {
		this.proxied = proxied;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("proxy: "+proxy.getClass()+" ,method: "+method+" ,args: "+args);
		if(args!=null)
			for(Object ob:args)
				System.out.println(" "+ob);
		return method.invoke(proxied, args);
	}
}
public class SimpleDynamicProxy {
	public static void consumer(Interface iface) {
		iface.doSomething();
		iface.somethingElse("HugeLiz");
	}
	
	public static void main(String[] args) {
		RealObject real = new RealObject();
		consumer(real);
		Interface proxy = (Interface)Proxy.newProxyInstance(Interface.class.getClassLoader(),
				new Class[]{Interface.class},
				new DynamicProxyHandler(real));
		consumer(proxy);
	}
}
