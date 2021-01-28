package generic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static net.mindview.util.Print.*;


// different types of function objects;
interface Combiner<T> {T combine(T x,T y);}
interface UnaryFunction<R,T>{R function(T x);}
interface Collector<T> extends UnaryFunction<T,T>{
	T result();//Extract result of collecting parameter
}
interface UnaryPredicate<T>{boolean test(T x);}

public class Functional {
	// Calls the Combiner object on each element to combine
	// it with a running result, which is finally returned;
	public static <T> T reduce(Iterable<T> seq, Combiner<T> combiner) {
		Iterator<T> t = seq.iterator();
		if (t.hasNext()) {
			T result = t.next();
			while(t.hasNext()) {
				result = combiner.combine(result, t.next());
			}
			return result;
		}
		// If seq is the empty list;
		return null; // Or throw exception
	}
	
	
	/**
	 * Take a function object and call it on each object on the list;
	 * ignoring the return value. the function object may act as a collecting parameter,
	 * so it returned at the end
	 */
	
	public static <T> Collector<T>  foreach(Iterable<T> seq, Collector<T> func){
		for(T t : seq) {
			func.function(t);
		}
		return func;
	}
	
	
	/**
	 * Create a list of results by calling a function object for each object in the list
	 */
	public static <R,T> List<R> transform(Iterable<T> seq, UnaryFunction<R,T> func){
		List<R> result = new ArrayList<R>();
		for(T t : seq) {
			result.add(func.function(t));
		}
		return result;
	}
	
	
	/**
	 * Applies a unary predicate to each item inf a sequence,
	 * and returns a list of items that produced "true"
	 */
	public static <T> List<T> filter(Iterable<T> seq, UnaryPredicate<T> pred){
		List<T> result = new ArrayList<T>();
		for(T t : seq) {
			if (pred.test(t)) {
				result.add(t);
			}
		}
		return result;
	}
	
	// To use the above generic methods, we need to create
	// function objects to adapt to our particular needs:
	
	static class IntegerAdder implements Combiner<Integer>{
		public Integer combine(Integer x, Integer y) {
			return x + y;
		}
	}
	
	static class IntegerSubtracter implements Combiner<Integer>{
		public Integer combine(Integer x, Integer y) {
			return x - y;
		}
	}
	
	static class BigDecimalAdder implements Combiner<BigDecimal>{
		public BigDecimal combine(BigDecimal x, BigDecimal y) {
			return x.add(y);
		}
	}
	
	static class BigIntegerAdder implements Combiner<BigInteger>{
		public BigInteger combine(BigInteger x, BigInteger y) {
			return x.add(y);
		}
	}
	
	static class AtomicLongAdder implements Combiner<AtomicLong>{
		public AtomicLong combine(AtomicLong x, AtomicLong y) {
			return new AtomicLong(x.addAndGet(y.get()));
		}
	}
	
	// we can even make a UnaryFunction with an "ulp"
	// (Units in the last place)
	static class BigDecimalUlp implements UnaryFunction<BigDecimal,BigDecimal>{
		public BigDecimal function(BigDecimal x) {
			return x.ulp();
		}
	}
	
	static class GreaterThan<T extends Comparable<T>> implements UnaryPredicate<T>{
		private T bound;
		public GreaterThan(T bound){
			this.bound = bound;
		}
		public boolean test(T x) {
			return x.compareTo(bound) > 0;
		}
	}
	
	static class MultiplyingIntegerCollector implements Collector<Integer>{
		private Integer val = 1;
		public Integer function(Integer x) {
			val *= x;
			return val;
		}
		public Integer result() {
			return val;
		}
	}
	
	public static void main(String[] args) {
		// Generics, varargs & boxing working together
		List<Integer> list = Arrays.asList(1,2,3,4,5,6,7);
		Integer result = reduce(list, new IntegerAdder());
		print(result);
		
		result = reduce(list, new IntegerSubtracter());
		print(result);
		
		print(filter(list, new GreaterThan<Integer>(4)));
		
		print(foreach(list, new MultiplyingIntegerCollector()).result());
		
		print(foreach(filter(list, new GreaterThan<Integer>(4)), new MultiplyingIntegerCollector()).result());
		
		MathContext mc = new MathContext(7);
		
		List<BigDecimal> lbd = Arrays.asList(
				new BigDecimal(1.1, mc),new BigDecimal(2.2, mc),
				new BigDecimal(3.3, mc), new BigDecimal(4.4, mc));
		BigDecimal rdb = reduce(lbd, new BigDecimalAdder());
		print(rdb);
		
		print(filter(lbd, new GreaterThan<BigDecimal>(BigDecimal.valueOf(3))));
		
		// use the prime-generation facility of BigInteger
		
		List<BigInteger> lbi = new ArrayList<>();
		BigInteger bi = BigInteger.valueOf(11);
		for(int i = 0; i < 11; i++) {
			lbi.add(bi);
			bi = bi.nextProbablePrime();
		}
		print(lbi);
		
		BigInteger rbi = reduce(lbi, new BigIntegerAdder());
		print(rbi);
		
		// The sum of this list of primes is also prime
		print(rbi.isProbablePrime(5));
		
		List<AtomicLong> lal = Arrays.asList(
				new AtomicLong(11), new AtomicLong(47),
				new AtomicLong(74), new AtomicLong(133));
		
		AtomicLong ral = reduce(lal, new AtomicLongAdder());
		print(ral);
		
		print(transform(lbd, new BigDecimalUlp()));
		
	}
}