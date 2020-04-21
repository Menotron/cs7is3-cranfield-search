#!/bin/bash

sh compile.sh

export SVR_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

rm -rf index
rm -rf output

JAVA_MAIN='ie.tcd.cs7is3.cranfield.App'
JAVA_TUNE='-server -Xmx2g'

java ${JAVA_TUNE} -cp .:${SVR_HOME}/cranfield-1.0.jar ${JAVA_MAIN}

./trec_eval.8.1/trec_eval ./data/QRelsCorrectedforTRECeval ./output/results.txt 2>&1 | tee ./output/trec_eval_score.txt