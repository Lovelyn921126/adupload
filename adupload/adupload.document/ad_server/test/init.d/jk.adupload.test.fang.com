#!/bin/bash

### BEGIN INIT INFO
# Provides:          resin
# Required-Start:    $remote_fs $network
# Required-Stop:     $remote_fs $network
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: The Resin Java Application Server
### END INIT INFO
#
### BEGIN Fedora SysV
#
# chkconfig: 2345 90 10
# description: The Resin Java Application Server
#
### END Fedora SysV
#
# To install, configure this file as needed and copy init.resin
# to /etc/rc.d/init.d as resin.  Then use "# /sbin/chkconfig resin reset"
#

JAVA_HOME="/usr/java/jdk"
RESIN_HOME="/usr/local/resin"
CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
PATH=$JAVA_HOME/bin:$PATH
LANG=en_US.UTF8

export JAVA_HOME CLASSPATH RESIN_HOME PATH

JAVA_EXE="${JAVA_HOME}/bin/java"

RESIN_EXE="${RESIN_HOME}/bin/resinctl"

if ! test -f "${RESIN_EXE}"; then
  RESIN_EXE="${JAVA_EXE} -jar ${RESIN_HOME}/lib/resin.jar"
fi

#
# Set to the server id to start and use "start" command instead of
# "start-all"
#
SERVER="-server adupload-jk-0"
#
# -resin-root /var/resin         -- the main data directory
#
RESIN_ROOT="-root-directory $RESIN_HOME"
#
# -conf /etc/resin/resin.xml   -- the main configuration file
#
RESIN_CONF="-conf ${RESIN_HOME}/conf/jk.adupload.test.fang.com.xml"
#
# -log-directory /var/log/resin -- the main log directory
#
RESIN_LOG="--log-directory /indexdata/aduploadlogs/resin"

ARGS="$ARGS $RESIN_CONF $RESIN_LOG $RESIN_ROOT $SERVER"

# start cmds: start start-all
START_CMD="start"

# stop cmds: stop shutdown kill
STOP_CMD="stop"

GC_LOG="/indexdata/aduploadlogs/resin/gc.adupload.jk.log"
WATCHDOG_PID=/var/run/olap.watchdog.pid
PROCESS_PID=/var/run/olap.pid

if test -r /lib/lsb/init-functions; then
  . /lib/lsb/init-functions
fi

type log_daemon_msg 1> /dev/null 2> /dev/null
if test "$?" != 0; then
  log_daemon_msg () {
      if [ -z "$1" ]; then
          return 1
      fi

      if [ -z "$2" ]; then
          echo "$1:"
          return
      fi
    
      echo "$1: $2"
  }
fi  

type log_end_msg 1> /dev/null 2> /dev/null
if test "$?" != 0; then
  log_end_msg () {
      [ -z "$1" ] && return 1

      if [ $1 -eq 0 ]; then
        echo "ok!"
      else
        echo "failed!"
      fi
    
    return $1
  }

fi

case "$1" in
  start)
        log_daemon_msg "Starting resin"
        if test -n "$USER"; then
            su $USER -c """$RESIN_EXE $ARGS $START_CMD"""
        else
            errors=`$RESIN_EXE $ARGS $START_CMD 2>&1`
            if [ $? != 0 ]; then
                log_daemon_msg $errors
            fi
        fi
        retval=$?
        log_end_msg $retval

        if [ $retval -eq 0 ]; then
            watch_dog_pid=`$0 status | grep pid | awk -F ':' '{print $2+0}' | head -n 1`
            if [ -n "$watch_dog_pid" ]; then
                echo $watch_dog_pid > $WATCHDOG_PID
            fi
            process_pid=`$0 status | grep pid | awk -F ':' '{print $2+0}' | tail -n 1`
            if [ -n "$process_pid" ]; then
                echo $process_pid > $PROCESS_PID
            fi
        fi
        ;;
  stop)
        log_daemon_msg "Stopping resin"

        # produce a java thread dump and backup the gc log
        if [ -e "$PROCESS_PID" ]; then
            PID=$(cat $PROCESS_PID)
            if [ -n "${PID}" ]; then
                kill -3 ${PID}
            fi
        fi
        if [ -e "$GC_LOG" ]; then
            mv $GC_LOG ${GC_LOG}.`date "+%Y%m%d%H%M%S"`
        fi

        if test -n "$USER"; then
            su $USER -c """$RESIN_EXE $ARGS $STOP_CMD"""
        else
            errors=`$RESIN_EXE $ARGS $STOP_CMD 2>&1`
            if [ $? != 0 ]; then
                log_daemon_msg $errors
            fi
        fi
        retval=$?
        log_end_msg $retval

        rm -f $WATCHDOG_PID
        rm -f $PROCESS_PID
        ;;
  status)
        $RESIN_EXE $ARGS status || exit 3
        ;;
  restart)
        $0 stop
        $0 start
        ;;
  *)
        echo "Usage: $0 {start|stop|status|restart}"
        exit 1
esac

exit 0
