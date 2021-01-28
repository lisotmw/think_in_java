package generic.selfBounded;

/**
 * 普通泛型对方法参数协变的测试
 * @author just4liz
 *
 */

class GenericSetter1<T extends GenericSetter1<T>>{}

//class DerivedGS0 extends GenericSetter1<Base>{}

class GenericSetter<T>{
	void set(T arg) {
		System.out.println("GenericSetter.set(Base)");
	}
}

class DerivedGS extends GenericSetter<Base>{
	void set(Derived derived) {
		System.out.println("DerivedGS.set(Derived)");
	}
}
// 老版自限定
class SelfGS extends GenericSetter<SelfGS>{
	void set(SelfGS self) {
		System.out.println("SelfGS.set(SelfGS)");
	}
}
public class PlainGenericInheritance {
	public static void main(String[] args) {
		Base base = new Base();
		Derived derived = new Derived();
				
		DerivedGS dgs = new DerivedGS();
		dgs.set(base);
		dgs.set(derived);
		
		SelfGS sg = new SelfGS();
		sg.set(new SelfGS());
		//! sg.set(new GenericSetter());
	}
}
