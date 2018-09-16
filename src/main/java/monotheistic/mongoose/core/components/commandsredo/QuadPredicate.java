package monotheistic.mongoose.core.components.commandsredo;

public interface QuadPredicate<L, M, R, O> {

    Boolean test(L l, M m, R r, O o);
}
