package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.PostStatEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStatDto {
    private Long id;

    private Long postId;

    @Builder.Default
    private Long likeCount = 0L;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PostStatDto fromEntity(PostStatEntity postStatEntity) {
        return PostStatDto.builder().id(postStatEntity.getId()).postId(postStatEntity.getPostId())
                .likeCount(postStatEntity.getLikeCount()).createdAt(postStatEntity.getCreatedAt())
                .updatedAt(postStatEntity.getUpdatedAt()).build();
    }

    public PostStatEntity toEntity() {
        return PostStatEntity.builder().id(id).postId(postId).likeCount(likeCount)
                .createdAt(createdAt).updatedAt(updatedAt).build();
    }
}
