#!/bin/sh

MEM="-Xms1g -Xmx1g"

STARTUP_WAIT=10
SHUTDOWN_WAIT=10

CT_USER="root"
CT_NAME="otus-palevo-application"
CT_HOME=/srv/otus-palevo
CT_OUT=${CT_HOME}/logs/service.out
CT_PID=${CT_HOME}/run.pid

CT_PROGRAM="$JAVA_HOME/bin/java -server $MEM -jar palevo.jar"

case $1 in
    start)
        echo "START $CT_NAME: "
        cd ${CT_HOME} || exit

        # check PID file
        if [ -f ${CT_PID} ]; then
            ppid=$(cat ${CT_PID} 2> /dev/null)
            if [ "$(ps --pid "${ppid}" 2> /dev/null | grep -c "${ppid}" 2> /dev/null)" -eq '1' ]; then
                echo "failure, already running"
                exit 1
            else
                rm -f ${CT_PID} 2> /dev/null
            fi
        fi

        # prepare out log file
        su ${CT_USER} -c "mkdir -p $(dirname ${CT_OUT})"
        su ${CT_USER} -c "cat /dev/null > $CT_OUT"

        # start service
        su ${CT_USER} -c "$CT_PROGRAM &>$CT_OUT &"

        # wait for startup
        count=0
        until [ -f ${CT_PID} ] || [ ${count} -gt ${STARTUP_WAIT} ]; do
            sleep 1
            count=$((count + 1))
        done

        # service can't start
        if [ ! -f ${CT_PID} ]; then
            echo "fail to start"
            exit 1
        fi

        echo "done."
        ;;

    stop)
        echo "STOP $CT_NAME: "
        if [ -f ${CT_PID} ]; then
            # read PID
            ppid=$(cat ${CT_PID} 2> /dev/null)

            # issuing SIGTERM
            kill "${ppid}" 2> /dev/null

            # wait for shutdown
            count=0
            kill_wait=${SHUTDOWN_WAIT}
            until [ "$(ps --pid "${ppid}" 2> /dev/null | grep -c "${ppid}" 2> /dev/null)" -eq '0' ] || [ ${count} -gt ${kill_wait} ]; do
                sleep 1
                count=$((count + 1))
            done

            if [ ${count} -gt ${kill_wait} ]; then
                # issuing KILL
                kill -9 "${ppid}" 2> /dev/null
            fi
        fi
        rm -f ${CT_PID} 2> /dev/null
        echo "done."
        ;;

    restart)
        $0 stop
        $0 start
        ;;

    *)
        echo "Usage: $0 {start|stop|restart}"
        exit 1
        ;;
esac
