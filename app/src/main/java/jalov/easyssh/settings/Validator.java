package jalov.easyssh.settings;

import java.util.function.Predicate;

/**
 * Created by jalov on 2018-01-26.
 */

public class Validator<T> {
    private String errorMessage;
    private Predicate<T> validator;

    public Validator(Predicate<T> validator, String errorMessage) {
        this.errorMessage = errorMessage;
        this.validator = validator;
    }

    public boolean isValid(T value) {
        return validator.test(value);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
