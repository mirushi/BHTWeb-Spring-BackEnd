package com.bhtcnpm.website.model.dto.UserDocReaction;

import com.bhtcnpm.website.model.entity.DocEntities.UserDocReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserDocReactionMapper {

    UserDocReactionMapper INSTANCE = Mappers.getMapper(UserDocReactionMapper.class);

    List<UserDocReactionDTO> userDocReactionListToUserDocReactionDTOList (List<UserDocReaction> userDocReactions);

    @Mapping(source = "userDocReactionId.user.id", target = "userID")
    @Mapping(source = "userDocReactionId.doc.id", target = "docID")
    UserDocReactionDTO userDocReactionToUserDocReactionDTO (UserDocReaction userDocReaction);

    UserDocReactionUserOwnDTO userDocReactionToUserDocReactionUserOwnDTO (UserDocReaction userDocReaction);

}
