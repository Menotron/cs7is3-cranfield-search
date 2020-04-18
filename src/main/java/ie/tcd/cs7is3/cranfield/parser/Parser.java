package ie.tcd.cs7is3.cranfield.parser;

import ie.tcd.cs7is3.cranfield.model.CranDocument;
import ie.tcd.cs7is3.cranfield.model.CranField;
import ie.tcd.cs7is3.cranfield.model.Query;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parser {

    private static Logger logger = LoggerFactory.getLogger(Parser.class);

    private static ArrayList<Document> cranDocuments = new ArrayList<Document>();
    private static ArrayList<Query> queries = new ArrayList<Query>();
    private static final char DOT = '.';
    private static final char SPACE = ' ';

    public static void main(String[] args) throws IOException{
        parse("data/cran.all.1400");
        parseQuery("data/cran.qry");
        System.out.println(cranDocuments.size());
        System.out.println(queries.size());
    }

    public static ArrayList<Document> parse(String docPath) throws IOException {
        try {
            List<String> fileData = Files.readAllLines(Paths.get(docPath), StandardCharsets.UTF_8);

            String text = "";
            CranDocument cranDocument = null;
            CranField fieldToAdd = null;

            for (String line : fileData) {
                if (line.trim().length() > 0 && line.charAt(0) == DOT) {
                    if (fieldToAdd != null) {
                        switch (fieldToAdd) {
                            case T: cranDocument.setTitle(text); break;
                            case A: cranDocument.setAuthor(text); break;
                            case B: cranDocument.setBiblio(text); break;
                            case W: cranDocument.setWords(text);break;
                            default: break;
                        }
                    }
                    text = "";
                    CranField field = Objects.requireNonNull(CranField.fromName(line.substring(0, 2)));
                    switch (field) {
                        case I:
                            if (cranDocument != null)
                                cranDocuments.add(createLuceneDocument(cranDocument));
                            cranDocument = new CranDocument();
                            cranDocument.setDocid(line.substring(3));
                            break;
                        case T: case A: case B: case W:
                            fieldToAdd = field; break;
                        default: break;
                    }
                } else
                    text += line + SPACE;
            }
            if (cranDocument != null) {
                cranDocument.setWords(text);
                cranDocuments.add(createLuceneDocument(cranDocument));
            }
        } catch (IOException ioe) {
            logger.error("Error while parsing document", ioe);
        }
        return cranDocuments;
    }


    public static ArrayList<Query> parseQuery(String queryPath) throws IOException {
        try {
            List<String> fileData = Files.readAllLines(Paths.get(queryPath), StandardCharsets.UTF_8);

            String text = "";
            Query query = null;
            CranField fieldToAdd = null;

            for (String line : fileData) {
                if (line.trim().length() > 0 && line.charAt(0) == DOT) {
                    if (fieldToAdd != null) {
                        query.setQuery(text);
                    }
                    text = "";
                    CranField field = Objects.requireNonNull(CranField.fromName(line.substring(0, 2)));
                    switch (field) {
                        case I:
                            if (query != null)
                                queries.add(query);
                            query = new Query();
                            query.setQueryid(line.substring(3));
                            break;
                        case W:
                            fieldToAdd = field; break;
                        default: break;
                    }
                } else
                    text += line + SPACE;
            }
            if (query != null) {
                query.setQuery(text);
                queries.add(query);
            }
        } catch (IOException ioe) {
            logger.error("Error while parsing query", ioe);
        }
        return queries;
    }

    private static Document createLuceneDocument(CranDocument doc) {
        Document document = new Document();
        document.add(new StringField("docid", doc.getDocid(), Field.Store.YES));
        document.add(new TextField("title", doc.getTitle(), Field.Store.YES));
        document.add(new TextField("biblio", doc.getBiblio(), Field.Store.YES));
        document.add(new TextField("author", doc.getAuthor(), Field.Store.YES));
        document.add(new TextField("words", doc.getWords(), Field.Store.YES));
        return document;
    }
}

