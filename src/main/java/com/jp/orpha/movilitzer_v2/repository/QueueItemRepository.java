package com.jp.orpha.movilitzer_v2.repository;

import com.jp.orpha.movilitzer_v2.entity.QueueItem;
import com.jp.orpha.movilitzer_v2.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QueueItemRepository extends JpaRepository<QueueItem, Long> {
    List<QueueItem> findByVenueOrderByCreatedAtAsc(Venue venue);
}