package typeinfo;
import static net.mindview.util.Print.*;

interface Interface{
	void doSomething();
	void somethingElse(String args);
}

class RealObject implements Interface{
	public void doSomething() {
		print("doSomething");
	}
	public void somethingElse(String args) {
		print("somethingElse: "+args);
	}
}

class SimpleProxy implements Interface{
	private Interface proxied;
	public SimpleProxy(Interface proxied) {
		this.proxied = proxied;
	}
	public void doSomething() {
		print("SimpleProxy doSomething");
		proxied.doSomething();
	}
	public void somethingElse(String args) {
		print("SimpleProxy somethingElse: "+args);
		proxied.somethingElse(args);
	}
}
public class SimpleProxyDemo {
	public static void consumer(Interface iface) {
		iface.doSomething();
		iface.somethingElse("bigLiz");
	}
	public static void main(String[] args) {
		consumer(new RealObject());
		consumer(new SimpleProxy(new RealObject()));
	}
}
