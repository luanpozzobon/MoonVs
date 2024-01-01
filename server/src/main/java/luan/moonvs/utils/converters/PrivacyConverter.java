package luan.moonvs.utils.converters;

import jakarta.persistence.AttributeConverter;
import luan.moonvs.models.enums.Privacy;

public class PrivacyConverter implements AttributeConverter<Privacy, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(Privacy attribute) {
        return attribute.getValue();
    }

    @Override
    public Privacy convertToEntityAttribute(Boolean dbData) {
        return dbData ? Privacy.PRIVATE : Privacy.PUBLIC;
    }
}
