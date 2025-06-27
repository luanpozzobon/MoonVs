package lpz.moonvs.domain.seedwork.validation;

public interface Validator<T> {
    void validate(final T domain);
}
