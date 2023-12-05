package luan.moonvs.models.requests;

import luan.moonvs.models.enums.ContentType;

public record ExternalContentRequest(int id,
                                     ContentType contentType) { }
