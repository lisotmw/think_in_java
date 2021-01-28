package generic.selfBounded;

/**
 * 自限定重写
 * @author just4liz
 *
 */

interface SelfBoundSetter<T extends SelfBoundSetter<T>>{
	void set(T arg);
}

interface Setter extends SelfBoundSetter<Setter>{}

public class SelfBoudingAndCovariantArguments {
	void test(Setter s1, Setter s2, SelfBoundSetter sbs, SelfBoundSetter sbs1) {
		s1.set(s2);
		//! s1.set(sbs); 
		/**
		 * 编译错误,s1不接受基类型，意味着及类型的方法签名已经被覆盖，而如果不用自限定，方法则会发生重载,参见：
		 * {@link OrdinaryArguments}
		 * 
		 * 自限定的价值在于可以产生协变参数类型：方法参数类型会随子类而变化！！！
		 */
		sbs.set(sbs1);
		sbs.set(s1);
	}
}
