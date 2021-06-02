package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.exception.CaptchaInvalidException;
import com.bhtcnpm.website.model.exception.CaptchaServerErrorException;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

public interface UserWebsiteService {
    //TODO: Do heavy rate-limiting for this API.
    List<String> checkUserExists (String name, String displayName, String email);
}
