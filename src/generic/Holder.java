package generic;

public class Holder<T> {
	private T val;
	public Holder(){};
	public Holder(T val) {this.val = val;}
	public void set(T val) {this.val = val;}
	public T get() { return val;}
	public boolean equals(Object obj) {
		return val.equals(obj);
	}
}
