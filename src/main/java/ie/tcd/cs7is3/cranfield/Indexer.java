package ie.tcd.cs7is3.cranfield;

import ie.tcd.cs7is3.cranfield.parser.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
import java.util.ArrayList;
import java.util.Objects;

public class Indexer {

    private static Logger logger = LoggerFactory.getLogger(Indexer.class);
    private static String INDEX_PATH = "Index/";

    public static boolean createIndex(String docPath, Analyzers analyserChoice, Similarities similarityChoice) {

        Analyzer analyzer = getAnalyzer(analyserChoice);
        Similarity similarity = getSimilarity(similarityChoice);

        try{
            Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            iwc.setSimilarity(similarity);
            IndexWriter indexWriter = new IndexWriter(directory, iwc);

            ArrayList<Document> documents = Parser.parse(docPath);
            indexWriter.addDocuments(documents);
            indexWriter.close();

        }catch (IOException ioe){
            logger.error("Error while indexing", ioe);
        }
        return true;
    }


    public static Analyzer getAnalyzer(Analyzers choice) {
        switch (choice) {
            case SIMPLE: return new SimpleAnalyzer();
            case STANDRD: return new StandardAnalyzer();
            case WHITESPACE: return new WhitespaceAnalyzer();
            case ENGLISH: return new EnglishAnalyzer();
            case CUSTOM: return null;
        }
        return new EnglishAnalyzer();
    }

    public static Similarity getSimilarity(Similarities choice) {
        switch (choice) {
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
