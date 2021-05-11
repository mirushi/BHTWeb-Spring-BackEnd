package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Slf4j
public class UserWebsiteServiceImpl implements UserWebsiteService {

    //Repositories
    private final UserWebsiteRepository uwRepository;

    @Override
    public List<String> checkUserExists (String name, String displayName, String email) {
        List<UserWebsite> userList = uwRepository.findAllByNameOrDisplayNameOrEmail(name, displayName, email);
        boolean isNameExist = false;
        boolean isDisplayNameExist = false;
        boolean isEmailExist = false;

        List<String> existedFields = new ArrayList<>();

        for (UserWebsite user : userList) {
            if (user.getName().equals(name)) {
                isNameExist = true;
            }
            if (user.getDisplayName().equals(displayName)) {
                isDisplayNameExist = true;
            }
            if (user.getEmail().equals(email)) {
                isEmailExist = true;
            }
        }

        if (isNameExist) {
            existedFields.add("name");
        }
        if (isDisplayNameExist) {
            existedFields.add("displayName");
        }
        if (isEmailExist) {
            existedFields.add("email");
        }

        return existedFields;
    }
}
