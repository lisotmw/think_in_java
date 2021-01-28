package generic;

/**
 * 泛型数组
 * @author just4liz
 *
 */

class Generic<T>{}
public class ArrayOfGeneric {
	static final int SIZE = 100;
	// 牛逼吧，可以声明
	static Generic<Integer>[] gia;
	
	public static void main(String[] args) {
		// [Ljava.lang.Object; cannot be cast to [Lgeneric.Generic
		//! gia = (Generic<Integer>[])new Object[SIZE];
		
		gia = (Generic<Integer>[])new Generic[SIZE];
		// Generic[]
		System.out.println(gia.getClass().getSimpleName());
		
		gia[0] = new Generic<Integer>();
		// 编译错误
		//! gia[1] = new Generic<Double>();
	}
}
