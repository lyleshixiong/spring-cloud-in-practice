package net.jaggerwang.scip.stat.adapter.repository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.stat.adapter.repository.jpa.PostStatJpaRepository;
import net.jaggerwang.scip.stat.adapter.repository.jpa.entity.PostStatDo;
import net.jaggerwang.scip.common.entity.PostStatEntity;
import net.jaggerwang.scip.stat.usecase.port.repository.PostStatRepository;

@Component
public class PostStatRepositoryImpl implements PostStatRepository {
    @Autowired
    private PostStatJpaRepository postStatJpaRepo;

    @Override
    public PostStatEntity save(PostStatEntity postStatEntity) {
        return postStatJpaRepo.save(PostStatDo.fromEntity(postStatEntity)).toEntity();
    }

    @Override
    public Optional<PostStatEntity> findById(Long id) {
        return postStatJpaRepo.findById(id).map(postStatDo -> postStatDo.toEntity());
    }

    @Override
    public Optional<PostStatEntity> findByPostId(Long postId) {
        return postStatJpaRepo.findByPostId(postId).map(postStatDo -> postStatDo.toEntity());
    }
}
