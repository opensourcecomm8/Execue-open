## Batch for killing Semantifi batch jobs
##

## Stop all semnatifciaton batches
for ((  pid = 5 ;  pid <= 6;  pid++  ))
do
        batch_pid=`cat /apps/batchscripts/portal-batch-node$pid/batchpid`;
        echo "Trying to Kill Node $pid PID: $batch_pid";
        Batch_Process_EXITS=`ps --no-header $batch_pid | awk '{ print $1 }'`;
        if [ -z $Batch_Process_EXITS ];
        then
            echo "              No PID to Kill"
        else
                kill -9 $batch_pid;
                echo "          Killed PID"
        fi
done
