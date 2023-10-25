package luan.moonvs.models.requests;

import java.util.Optional;

public record AccountRequestDTO(Optional<String> username, Optional<String> email) {
}
