package generic.selfBounded;


class Basic{
	Basic b;
	public void set(Basic b){ 
		this.b = b;
		System.out.println("invoke Basic");
	}
	public void print() {
		System.out.println(b.getClass().getSimpleName());
	}
}

class SubType extends Basic{
	SubType b;
	public void set(SubType b){ 
		this.b = b;
		System.out.println("invoke sub");
	}
}

class GenericBase<T>{
	T b;
	public void set(T b){ this.b = b; }
	public void print() {
		System.out.println(b.getClass().getSimpleName());
	}
}

class GenericSub extends GenericBase<GenericSub>{}

class SelfBasic<T extends SelfBasic<T>>{
	T b;
	public void set(T t) {this.b = t;}
	public void print() {
		System.out.println(b.getClass().getSimpleName());
	}
}

class SelfSub extends SelfBasic<SelfSub>{
	
}


public class OrdinaryArguments2 {
	public static void main(String[] args) {
		SubType st = new SubType();
		st.set(new SubType());		//Ok
		//! st.print();	// java.lang.NullPointerException
		st.set(new Basic());		//调用了继承自Basic的set函数
		st.print();
		
		
		GenericSub sub = new GenericSub();
		sub.set(new GenericSub());
		//! sub.set(new GenericBasic()); //can not compiled
		sub.print();
		
		SelfSub ss = new SelfSub();
		
		ss.set(new SelfSub());
		ss.print();
		//! ss.set(new SelfBasic()); //can not compiled
	}

}
