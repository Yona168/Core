package monotheistic.mongoose.core.utils;

import java.util.OptionalInt;

public interface MiscUtils {
    static OptionalInt parseInt(String string) {
        try {
            return OptionalInt.of(Integer.parseInt(string));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return OptionalInt.empty();
        }
    }
}
