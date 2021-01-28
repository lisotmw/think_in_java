package generic;

import java.util.Random;

//import net.mindview.util.Generator;
//import net.mindview.util.RandomGenerator;

interface C<T>{}

class B implements C<B>{}
class A extends B implements C<B>{}
//不能同时实现 但接口的的不同泛型
//! class D extends B implements C<D>{}

interface Generator<T>{
	T next();
}

class Integer0 implements Generator<java.lang.Integer>{

	@Override
	public java.lang.Integer next() {
		int nextInt = new Random().nextInt(10000);
		return nextInt;
	}
}

class String0 implements Generator<java.lang.String>{

	private int len;
	String0(int len){
		this.len = len;
	}
	@Override
	public java.lang.String next() {
		StringBuilder sBuilder = new StringBuilder();
		Random rand = new Random();
		for(int i = 0; i < len; i++) {
			sBuilder.append((char)((rand.nextBoolean() ? 65 : 97) + rand.nextInt(26)));
		}
		return sBuilder.toString(); 
	}
	
}

/**
 * 泛型基本类型问题
 * @author just4liz
 *
 */

class FArray{
	public static <T> T[] fill(T[] a,Generator<T> gen) {
		for(int i = 0; i < a.length; i++) {
			a[i] = gen.next(); 
		}
		return a;
	}
}

public class PrimitiveGenericTest {
	public static void main(java.lang.String[] args) {
		java.lang.String[] strings = FArray.fill(new java.lang.String[7], new String0(10));
		for(java.lang.String string : strings) {
			System.out.println(string);
		}
		
		java.lang.Integer[] integers = FArray.fill(new java.lang.Integer[7], new Integer0());
		
		for(int i : integers) {
			System.out.println(i);
		}
	}
}
