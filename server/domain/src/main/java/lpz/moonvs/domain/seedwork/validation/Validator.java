package lpz.moonvs.domain.seedwork.validation;

import lpz.moonvs.domain.seedwork.entity.DomainObject;

public interface Validator<T extends DomainObject> {
    void validate(final T domain);
}
