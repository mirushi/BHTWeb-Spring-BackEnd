package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.Activity.ActivityMapper;
import com.bhtcnpm.website.model.dto.Announcement.AnnouncementDTO;
import com.bhtcnpm.website.model.dto.Announcement.AnnouncementListDTO;
import com.bhtcnpm.website.model.dto.Announcement.AnnouncementMapper;
import com.bhtcnpm.website.model.entity.Announcement;
import com.bhtcnpm.website.repository.AnnouncementRepository;
import com.bhtcnpm.website.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private static final int PAGE_SIZE = 10;

    private final AnnouncementRepository repository;

    private final AnnouncementMapper announcementMapper;

    @Override
    public AnnouncementListDTO getAllAnnouncements(@Min(0) Integer paginator) {
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE, Sort.by("lastUpdated").descending());

        Page<Announcement> queryResult = repository.findAll(pageable);

        List<AnnouncementDTO> announcementList = queryResult
                .stream()
                .map(announcementMapper::announcementToAnnouncementDTO)
                .collect(Collectors.toList());

        AnnouncementListDTO finalResult = new AnnouncementListDTO(announcementList, queryResult.getTotalPages(), queryResult.getTotalElements());

        return finalResult;
    }

    public AnnouncementListDTO getAllActiveAnnouncements(@Min(0) Integer paginator) {
        Pageable pageable = PageRequest.of(paginator, PAGE_SIZE, Sort.by("lastUpdated").descending());

        Page<Announcement> queryResult = repository.findAnnouncementByActivatedStatus(true, pageable);

        List<AnnouncementDTO> announcementList = queryResult
                .stream()
                .map(announcementMapper::announcementToAnnouncementDTO)
                .collect(Collectors.toList());

        AnnouncementListDTO finalResult = new AnnouncementListDTO(announcementList, queryResult.getTotalPages(), queryResult.getTotalElements());

        return finalResult;
    }

    @Override
    public AnnouncementDTO saveAnnouncementsByDTO(@Valid AnnouncementDTO announcementDTO) {
        Announcement announcement = announcementMapper.announcementDTOToAnnouncement(announcementDTO);
        announcement.setLastUpdated(LocalDateTime.now());

        return saveAndReturnDTO(announcement);
    }

    @Override
    public AnnouncementDTO createAnnouncementsByDTO(@Valid AnnouncementDTO announcementDTO) {
        Announcement announcement = announcementMapper.announcementDTOToAnnouncement(announcementDTO);
        announcement.setId(null);
        announcement.setLastUpdated(LocalDateTime.now());
        announcement.setVersion((short)0);

        return saveAndReturnDTO(announcement);
    }

    private AnnouncementDTO saveAndReturnDTO (@Valid Announcement announcement) {
        Announcement savedAnnouncement = repository.save(announcement);

        AnnouncementDTO announcementDTO = announcementMapper.announcementToAnnouncementDTO(savedAnnouncement);

        return announcementDTO;
    }

    @Override
    public void deleteAnnouncementByID (@Min(0) Long id) {
        repository.deleteById(id);
    }

}
