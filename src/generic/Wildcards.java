package generic;

public class Wildcards {
	static void rawArgs(Holder holder, Object arg) {
		holder.set(arg);
		Object object = holder.get();
	}
	
	static void unboundedArg(Holder<?> holder, Object arg) {
		//holder.set(arg); error!!!
		Object object = holder.get(); //!!!
	}
}
