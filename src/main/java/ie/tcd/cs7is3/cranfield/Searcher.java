package ie.tcd.cs7is3.cranfield;

import ie.tcd.cs7is3.cranfield.model.QueryModel;
import ie.tcd.cs7is3.cranfield.parser.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Searcher {

    private static Logger logger = LoggerFactory.getLogger(Searcher.class);
    private static String INDEX_PATH = "index/";
    private static String OUTPUT_FILE = "output/results.txt";
    private static int NUM_RESULTS = 1;

    public static void runQueries(String queryPath, int numResults, Indexer.Analyzers analyserChoice,
                                  Indexer.Similarities similarityChoice) {
        try {
            Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
            DirectoryReader indexReader = DirectoryReader.open(directory);

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            indexSearcher.setSimilarity(Indexer.getSimilarity(similarityChoice));
            Analyzer analyzer = Indexer.getAnalyzer(analyserChoice);

            File resultFile = new File(OUTPUT_FILE);
            resultFile.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(resultFile, StandardCharsets.UTF_8.name());

            ArrayList<QueryModel> queries = Parser.parseQuery(queryPath);
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                    new String[]{"title", "author", "biblio", "words"}, analyzer);

            NUM_RESULTS = numResults;
            for (QueryModel element : queries) {
                String queryString = QueryParser.escape(element.getQuery());
                Query query = queryParser.parse(queryString);
                search(indexSearcher, query, writer, Integer.parseInt(element.getQueryid()));
            }

            indexReader.close();
            writer.close();
            directory.close();

        } catch (IOException ioe) {
            logger.error("Error while running queries", ioe);
        } catch (ParseException pe) {
            logger.error("Error while parsing query", pe);
        }
    }

    public static void search(IndexSearcher is, Query query, PrintWriter writer, int queryID) throws IOException{
        ScoreDoc[] hits = is.search(query, NUM_RESULTS).scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            Document hitDocument = is.doc(hits[i].doc);
            writer.println(queryID + " 0 " + hitDocument.get("docid") + " 0 " + hits[i].score + " STANDARD");
        }
    }


}
