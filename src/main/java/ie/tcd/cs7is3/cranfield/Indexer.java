package ie.tcd.cs7is3.cranfield;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class Indexer {

    private static Logger logger = LoggerFactory.getLogger(Indexer.class);
    private static String indexPath = "Index/";
    Analyzer analyzer;
    Similarity similarity;

    public boolean createIndex(String douPath) {
        try{
            Directory directory = FSDirectory.open(Paths.get(indexPath));

        }catch (IOException ioe){
            logger.error("Error while indexing", ioe);
        }
    }


    public Analyzer getAnalyzer(String choice) {
        switch (Objects.requireNonNull(Analyzers.fromName(choice))) {
            case SIMPLE: return new SimpleAnalyzer();
            case STANDRD: return new StandardAnalyzer();
            case WHITESPACE: return new WhitespaceAnalyzer();
            case ENGLISH: return new EnglishAnalyzer();
            case CUSTOM: return null;
        }
        return new EnglishAnalyzer();
    }

    public Similarity getSimilarity(String choice) {
        switch (Objects.requireNonNull(Similarities.fromName(choice))) {
            case CLASSIC: new ClassicSimilarity();
            case BOOLEAN: new BooleanSimilarity();
            case BM25: new BM25Similarity();
        }
        return new BM25Similarity();
    }

    public enum Analyzers {
        WHITESPACE("whitespace"), SIMPLE("simple"),
        STOP("stop"), STANDRD("standard"),
        ENGLISH("english"), CUSTOM("custom");
        public String type;
        private Analyzers(String type){this.type = type;}
        public String getType() {return type;}
        public static Analyzers fromName(String name) {
            for (Analyzers a : values()) {
                if (a.getType().equalsIgnoreCase(name))
                    return a;
            }
            return null;
        }
    }

    public enum Similarities {
        CLASSIC("classic"), BOOLEAN("boolean"), BM25("bm25");
        public String type;
        private Similarities(String type){this.type = type;}
        public String getType() {return type;}
        public static Similarities fromName(String name) {
            for (Similarities s : values()) {
                if (s.getType().equalsIgnoreCase(name))
                    return s;
            }
            return null;
        }
    }
}
