package ie.tcd.cs7is3.cranfield;

import ie.tcd.cs7is3.cranfield.parser.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Indexer {

    private static Logger logger = LoggerFactory.getLogger(Indexer.class);
    private static String INDEX_PATH = "index/";
    private static String STOPWORD_FILE = "data/stopwords.txt";


    public static boolean createIndex(String docPath, Analyzers analyserChoice, Similarities similarityChoice) {

        Analyzer analyzer = getAnalyzer(analyserChoice);
        Similarity similarity = getSimilarity(similarityChoice);

        try{
            assert analyzer != null;
            logger.debug("Analyzer: " + analyzer.toString() + " Similarity: " + similarity.toString());
            Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            iwc.setSimilarity(similarity);
            IndexWriter indexWriter = new IndexWriter(directory, iwc);

            ArrayList<Document> documents = Parser.parse(docPath);
            indexWriter.addDocuments(documents);
            indexWriter.close();
            logger.info("Finished indexing " + documents.size() + " documents");
            logger.info("Index created at " + INDEX_PATH);

        }catch (IOException ioe){
            logger.error("Error while indexing", ioe);
        }
        return true;
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

    public static Analyzer getAnalyzer(Analyzers choice) {
        logger.debug("return instance of " + choice);
        switch (choice) {
            case SIMPLE: return new SimpleAnalyzer();
            case STANDRD: return new StandardAnalyzer();
            case WHITESPACE: return new WhitespaceAnalyzer();
            case ENGLISH: return new EnglishAnalyzer(getStopWprds());
            case CUSTOM: return null; // to be implemented
        }
        return new EnglishAnalyzer();
    }

    public enum Similarities {
        CLASSIC("classic"), BOOLEAN("boolean"), BM25("bm25"), LMDS("lmds");
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

    public static Similarity getSimilarity(Similarities choice) {
        logger.debug("return instance of " + choice);
        switch (choice) {
            case CLASSIC: return new ClassicSimilarity();
            case BOOLEAN: return new BooleanSimilarity();
            case BM25: return new BM25Similarity();
            case LMDS: return new LMDirichletSimilarity();
        }
        return new BM25Similarity();
    }

    public static CharArraySet getStopWprds(){
        CharArraySet stopwords = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(STOPWORD_FILE));
            String[] words = new String(encoded, StandardCharsets.UTF_8).split("\n");
            stopwords =  new CharArraySet(Arrays.asList(words), true);
        } catch (IOException ioe) {
            logger.error("Error Reading stopwords file" + STOPWORD_FILE, ioe);
        }
        return stopwords;
    }
}
