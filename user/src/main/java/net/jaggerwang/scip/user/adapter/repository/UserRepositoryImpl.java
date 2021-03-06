package net.jaggerwang.scip.user.adapter.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQuery;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.jaggerwang.scip.user.adapter.repository.jpa.UserFollowJpaRepository;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.UserDo;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.UserFollowDo;
import net.jaggerwang.scip.user.usecase.port.repository.UserRepository;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.user.adapter.repository.jpa.UserJpaRepository;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.QUserDo;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.QUserFollowDo;
import net.jaggerwang.scip.common.entity.UserEntity;

@Component
public class UserRepositoryImpl implements UserRepository {
    private JPAQueryFactory jpaQueryFactory;
    private UserJpaRepository userJpaRepo;
    private UserFollowJpaRepository userFollowJpaRepo;

    public UserRepositoryImpl(JPAQueryFactory jpaQueryFactory, UserJpaRepository userJpaRepo,
                              UserFollowJpaRepository userFollowJpaRepo) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.userJpaRepo = userJpaRepo;
        this.userFollowJpaRepo = userFollowJpaRepo;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userJpaRepo.save(UserDo.fromEntity(userEntity)).toEntity();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userJpaRepo.findById(id).map(UserDo::toEntity);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaRepo.findByUsername(username).map(UserDo::toEntity);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userJpaRepo.findByEmail(email).map(UserDo::toEntity);
    }

    @Override
    public Optional<UserEntity> findByMobile(String mobile) {
        return userJpaRepo.findByMobile(mobile).map(UserDo::toEntity);
    }

    @Override
    public void follow(Long followerId, Long followingId) {
        userFollowJpaRepo.save(UserFollowDo.builder().followerId(followerId).followingId(followingId).build());
    }

    @Override
    public void unfollow(Long followerId, Long followingId) {
        userFollowJpaRepo.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    private JPAQuery<UserDo> followingQuery(Long followerId) {
        var user = QUserDo.userDo;
        var userFollow = QUserFollowDo.userFollowDo;
        var query = jpaQueryFactory.selectFrom(user).join(userFollow).on(user.id.eq(userFollow.followingId));
        if (followerId != null) {
            query.where(userFollow.followerId.eq(followerId));
        }
        return query;
    }

    @Override
    public List<UserEntity> following(Long followerId, Long limit, Long offset) {
        var query = followingQuery(followerId);
        var userFollow = QUserFollowDo.userFollowDo;
        query.orderBy(userFollow.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(UserDo::toEntity).collect(Collectors.toList());
    }

    @Override
    public Long followingCount(Long followerId) {
        return followingQuery(followerId).fetchCount();
    }

    private JPAQuery<UserDo> followerQuery(Long followingId) {
        var user = QUserDo.userDo;
        var userFollow = QUserFollowDo.userFollowDo;
        var query = jpaQueryFactory.selectFrom(user).join(userFollow).on(user.id.eq(userFollow.followerId));
        if (followingId != null) {
            query.where(userFollow.followingId.eq(followingId));
        }
        return query;
    }

    @Override
    public List<UserEntity> follower(Long followingId, Long limit, Long offset) {
        var query = followerQuery(followingId);
        var userFollow = QUserFollowDo.userFollowDo;
        query.orderBy(userFollow.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(UserDo::toEntity).collect(Collectors.toList());
    }

    @Override
    public Long followerCount(Long followingId) {
        return followerQuery(followingId).fetchCount();
    }

    @Override
    public Boolean isFollowing(Long followerId, Long followingId) {
        return userFollowJpaRepo.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
}
