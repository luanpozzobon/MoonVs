package luan.moonvs.repositories;

import luan.moonvs.models.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    List<Content> getByOriginalTitleContaining(String originalTitle);
    Content getReferenceByIdTmdb(int idTmdb);
    boolean existsByIdTmdb(int idTmdb);
}
