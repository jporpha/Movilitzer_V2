package com.jp.orpha.movilitzer_v2.repository;

import com.jp.orpha.movilitzer_v2.entity.Vote;
import com.jp.orpha.movilitzer_v2.entity.QueueItem;
import com.jp.orpha.movilitzer_v2.entity.PublicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByQueueItemAndPublicUser(QueueItem item, PublicUser user);
}