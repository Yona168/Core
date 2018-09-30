package monotheistic.mongoose.core.components.commands;

import java.util.List;

public interface Executable<T, M, V> {

    boolean execute(T t, M m, V v, List<Object> objs);

}
