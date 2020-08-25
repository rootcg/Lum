package rootcg.lum.util;

import java.util.Objects;
import java.util.function.Predicate;

public class Validations {

    public static <T> T checkNonNullArgument(T argument) {
        return checkArgument(Objects::nonNull, argument);
    }

    public static <T> T checkArgument(Predicate<T> condition, T argument) {
        if (condition.test(argument))
            return argument;

        throw new IllegalArgumentException();
    }

}
