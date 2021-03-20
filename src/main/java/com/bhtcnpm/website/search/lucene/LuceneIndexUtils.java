package com.bhtcnpm.website.search.lucene;

import lombok.Getter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;

public class LuceneIndexUtils {

    private static String PATH_TO_INDEX = "target/lucene";

    private static final StandardAnalyzer standardAnalyzer = new StandardAnalyzer();

    public static IndexReader getReader (String entityName) throws IOException {
        File file = new File(PATH_TO_INDEX + "/" + entityName);
        Directory directory = FSDirectory.open(file.toPath());
        return DirectoryReader.open(directory);
    }
    public static StandardAnalyzer getStandardAnalyzer() {return standardAnalyzer;}
}
