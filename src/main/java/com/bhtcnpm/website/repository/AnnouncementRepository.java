package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    Page<Announcement> findAnnouncementByActivatedStatus(Boolean activatedStatus, Pageable pageable);

}
