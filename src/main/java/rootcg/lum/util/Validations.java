package rootcg.lum.util;

import java.util.Objects;
import java.util.function.Predicate;

public class Validations {

    public static class Arguments {

        public static void checkAnyNonNull(String message, Object... argument) {
            for (Object o : argument)
                if (o != null)
                    return;

            throw new IllegalArgumentException(message);
        }

        public static <T> T checkNonNull(T argument) {
            return check(Objects::nonNull, argument);
        }

        public static <T> T check(Predicate<T> condition, T argument) {
            if (condition.test(argument))
                return argument;

            throw new IllegalArgumentException();
        }

    }

}
