package monotheistic.mongoose.core.utils;

import java.util.Optional;

public interface MiscUtils {
    static Optional<Integer> parseInt(String string) {
        try {
            return Optional.of(Integer.parseInt(string));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
