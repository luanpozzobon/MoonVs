package lpz.moonvs.application.title.command;

import lpz.moonvs.application.title.SearchProvider;
import lpz.moonvs.domain.title.entity.Type;

public record SelectTitleCommand(Integer id,
                                 Type type,
                                 SearchProvider provider,
                                 String language) {
}
