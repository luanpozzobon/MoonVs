package lpz.moonvs.application.title.command;

import lpz.moonvs.application.title.SearchProvider;

public record SearchTitleCommand(String title,
                                 String language,
                                 SearchProvider provider,
                                 Integer page) { }
