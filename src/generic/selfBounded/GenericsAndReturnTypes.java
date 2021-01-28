package generic.selfBounded;

/**
 * 自限定返回协变
 * @author just4liz
 *
 */

interface GenericGetter<T extends GenericGetter<T>>{
	T get();
}

interface Getter extends GenericGetter<Getter>{}

public class GenericsAndReturnTypes {
	void test(Getter get) {
		Getter getter = get.get();
		GenericGetter getter2 = get.get();
	}
}
