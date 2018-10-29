#!/bin/sh

## Check for feature count batch

##
##Checks the batch Process status STARTS
##

LOG_HOME="/apps/logs/offline-batch/qacc-node@@NODE_ID@@"
BATCH_HOME="/apps/batchscripts/offline-batch-qacc-node@@NODE_ID@@"
cd $BATCH_HOME
if [ -e $BATCH_HOME/batchpid-image-url-processing-model-3 ]; then
        Process_PID=`cat $BATCH_HOME/batchpid-image-url-processing-model-3`;
        tnow=`date +%s`
        tfile=`date +%s -r $BATCH_HOME/batchpid-image-url-processing-model-3`
        age=$((($tnow-$tfile)/60))
        echo "Age "$age;
        echo $Process_PID;
        if [ $age -ge 120 ] ; then
                echo "delete process";
                rm $BATCH_HOME/batchpid-image-url-processing-model-3;
                kill -9 $Process_PID;
        else
                Process_EXITS=`ps --no-header $Process_PID | awk '{ print $1 }'`;
                echo $Process_EXITS;
                if [ -z $Process_EXITS ]; then
                        echo "Previous Process may be terminated"
                else
                        echo "Process already running";
                        exit 0
                fi
        fi
fi

echo "Previous Process may be terminated";
echo "New batch Process started";


##
# Checks the batch Process status ENDS
##

echo "start image url processing batch Script"
cd $BATCH_HOME
if [ -z "$JAVA_HOME" ]; then
    echo "The JAVA_HOME environment variable is not defined"
    JAVA_HOME="/usr/java/jdk1.6.0_17"
fi
BATCH_CLASSPATH="";

if [ -d "$BATCH_HOME" ]; then
    ## Addd config to classpath
        BATCH_CLASSPATH="$BATCH_HOME/config"
    for i in "$BATCH_HOME"/lib/*.jar; do
      BATCH_CLASSPATH="$BATCH_CLASSPATH":"$i"
    done

fi

## BATCH_CLASSPATH="$BATCH_CLASSPATH":""$BATCH_HOME"/config"

_RUNJAVA="$JAVA_HOME"/bin/java

echo "$_RUNJAVA -cp $BATCH_CLASSPATH com.execue.offline.batch.imageprocessing.PopulateImageURLsOfflineBatch 114 3 &"
$_RUNJAVA -cp $BATCH_CLASSPATH -Xms1024m -Xmx2048m com.execue.offline.batch.imageprocessing.PopulateImageURLsOfflineBatch 114 3 >> $LOG_HOME/log-image-url-processing-model-3.txt & 
echo $! > batchpid-image-url-processing-model-3
