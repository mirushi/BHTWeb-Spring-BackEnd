package com.bhtcnpm.website.repository.Doc.comparator;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;

import java.util.Comparator;

public class DocFileUploadComparatorRankBased implements Comparator<DocFileUpload> {
    @Override
    public int compare (DocFileUpload fileUpload1, DocFileUpload fileUpload2) {
        return Integer.compare(fileUpload1.getRank(), fileUpload2.getRank());
    }
}
