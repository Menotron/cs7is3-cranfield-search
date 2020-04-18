package ie.tcd.cs7is3.cranfield;

import ie.tcd.cs7is3.cranfield.model.Query;
import ie.tcd.cs7is3.cranfield.parser.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Searcher {

    private static Logger logger = LoggerFactory.getLogger(Searcher.class);
    private static String INDEX_PATH = "Index/";
    private static String OUTPUT_FILE = "results.txt";

    public static void runQueries(String queryPath, int numResults, Indexer.Analyzers analyserChoice,
                                  Indexer.Similarities similarityChoice) throws IOException {
        try {
            Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
            DirectoryReader indexReader = DirectoryReader.open(directory);

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            indexSearcher.setSimilarity(Indexer.getSimilarity(similarityChoice));
            Analyzer analyzer = Indexer.getAnalyzer(analyserChoice);

            PrintWriter writer = new PrintWriter(OUTPUT_FILE, StandardCharsets.UTF_8.name());

            ArrayList<Query> queries = Parser.parseQuery(queryPath);
        }catch (IOException ioe) {
            logger.error("Error while running queries", ioe);
        }
    }
}
