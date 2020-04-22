# cs7is3-cranfield-search

## Project developed as part of CS7IS3-Information-Retrieval-and-Web-Search coursework

A search engine implementation for cranfield collection using Apache Lucene(Java) Library.
The dataset is a collection of 1400 documents, and the query file consists of 225 queries.

### Getting Started

Instructions to setup the project and to run the search engine

### Requirements

```txt
Ubuntu ~18.04
OpenJDK 1.8.0_242
Maven 3.6.0
GCC 7.4.0
```

Extract the project to your home path

```sh
$ tar xzvf cs7is3-cranfield-search.tar.gz ~/
$ cd ~/cs7is3-cranfield-search
```

### Compile

 ```sh
$ mvn clean package -DskipTests
```

### Run

```sh
$ java -server -Xmx2g -cp ./target/cranfield-1.0.jar ie.tcd.cs7is3.cranfield.App
```
The default analyzer used is the EnglishAnalyzer and the similarity used is BM25Similarity.
The data document path, query path, analyzer and similarity can be changed by the `-d`, `-q`, `-a`, `-s` command line options respectively. Run the program with the `-h` flag for more details.

The output of the execution is stored in `./output/results.txt`

### Evaluate

To score the search engine trec-eval is used. 

```sh
$ cd trec_eval-9.0.7
$ make
$ ./trec_eval ./data/QRelsCorrectedforTRECeval ./output/results.txt 2>&1 | tee ./output/trec_eval_score.txt
```

### Or

Run the `execute.sh` to do all three in one go.

```sh
$ sh ~/cs7is3-cranfield-search/execute.sh
```



