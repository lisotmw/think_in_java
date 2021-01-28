package generic.selfBounded;

class OrdinarySetter{
	void set(Base base) {
		System.out.println("OrdinarySetter.set(Base)");
	}
}

class DerivedSetter extends OrdinarySetter{
	//! @Override 这里并没有覆盖
	void set(Derived der) {
		System.out.println("DerivedSetter.set(Derived)");
	}
}

public class OrdinaryArguments {
	public static void main(String[] args) {
		Base base = new Base();
		Derived derived = new Derived();
		
		DerivedSetter ds = new DerivedSetter();
		ds.set(derived);
		
		ds.set(base);// 这里调用的是 OrdinarySetter.set()
		
		/**
		 * output:
		 * DerivedSetter.set(Derived)
		 * OrdinarySetter.set(Base)
		 */
		
	}
}
