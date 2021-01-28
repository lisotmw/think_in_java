package generic.selfBounded;

/**
 * 协变返回类型
 * @author just4liz
 *
 */

class Base{}
class Derived extends Base{}

interface OrdinaryGetter{
	Base get();
}

interface DerivedGetter extends OrdinaryGetter{
	// !!! 重写可以以子类型返回
	@Override
	Derived get();
}
public class CovariantReturnTypes {
	void test(DerivedGetter d) {
		Derived d2 = d.get();
	}
}
