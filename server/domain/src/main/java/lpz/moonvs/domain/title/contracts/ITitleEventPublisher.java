package lpz.moonvs.domain.title.contracts;

import lpz.moonvs.domain.seedwork.valueobject.Id;
import lpz.moonvs.domain.title.entity.Title;

public interface ITitleEventPublisher {
    void publishTitleCreated(final Id<Title> title);
}
