package ie.tcd.cs7is3.cranfield;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static Logger logger = LoggerFactory.getLogger(App.class);

    private static String HELP_OPTION = "h";
    private static String HELP_OPTION_LONG = "help";

    private static String DOC_OPTION = "d";
    private static String DOC_OPTION_LONG = "doc";

    private static String QUERY_OPTION = "q";
    private static String QUERY_OPTION_LONG = "query";

    private static String docPath = "data/cran.all.1400";
    private static String queryPath = "data/cran.qry";

    public static void main(String[] args) throws Exception {
        // parse the args
        Options options = getOption();
        CommandLineParser parser = new DefaultParser();


        try {
            CommandLine commandLine = parser.parse(options, args);
            if (args.length == 0) {
                logger.warn("No command line arguments passed. Reading files from default data path");
            } else {
                if (commandLine.hasOption(HELP_OPTION) || commandLine.hasOption(HELP_OPTION_LONG)) {
                    gethelp(options);
                    // end the program
                    return;
                }
                if (commandLine.hasOption(DOC_OPTION) || commandLine.hasOption(DOC_OPTION_LONG)) {
                    docPath = commandLine.getOptionValue(DOC_OPTION, docPath);
                }
                if (commandLine.hasOption(QUERY_OPTION) || commandLine.hasOption(QUERY_OPTION_LONG)) {
                    queryPath = commandLine.getOptionValue(QUERY_OPTION, queryPath);
                }
            }
        } catch (Exception e) {
            logger.error("Exception while parsing arguments...");
            gethelp(options);
        }
        Indexer.createIndex(docPath, Indexer.Analyzers.ENGLISH, Indexer.Similarities.BM25);
        Searcher.runQueries(queryPath, 1000, Indexer.Analyzers.ENGLISH, Indexer.Similarities.BM25);
    }

    /**
     * get the command line args parser
     * @return Options
     */
    private static Options getOption() {
        final Options options = new Options();
        options.addOption(HELP_OPTION, HELP_OPTION_LONG, false, "Help");
        options.addOption(DOC_OPTION, DOC_OPTION_LONG, false, "Document path");
        options.addOption(QUERY_OPTION, QUERY_OPTION_LONG, false, "Query File path");
        return options;
    }

    private static void gethelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(100);
        helpFormatter.printHelp("cranfield-search.jar", options);
    }
}
