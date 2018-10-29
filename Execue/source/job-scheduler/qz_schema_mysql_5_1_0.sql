
--***********************************************************
--*********** Quartz scheduler schema **************
--***********************************************************

--** The following section should be uncommented and run for dropping
--** an existing schema 

--delete from qrtz_job_listeners;
--delete from qrtz_trigger_listeners;
--delete from qrtz_fired_triggers;
--delete from qrtz_simple_triggers;
--delete from qrtz_cron_triggers;
--delete from qrtz_blob_triggers;
--delete from qrtz_triggers;
--delete from qrtz_job_details;
--delete from qrtz_calendars;
--delete from qrtz_paused_trigger_grps;
--delete from qrtz_locks;
--delete from qrtz_scheduler_state;

--drop table qrtz_calendars;
--drop table qrtz_fired_triggers;
--drop table qrtz_trigger_listeners;
--drop table qrtz_blob_triggers;
--drop table qrtz_cron_triggers;
--drop table qrtz_simple_triggers;
--drop table qrtz_triggers;
--drop table qrtz_job_listeners;
--drop table qrtz_job_details;
--drop table qrtz_paused_trigger_grps;
--drop table qrtz_locks;
--drop table qrtz_scheduler_state;

--** The following schema creates the tables for Quartz **********

---------------- BEGIN TABLE CREATION --------------------------- 


CREATE TABLE qrtz_job_details
  (
    JOB_NAME  VARCHAR(80) NOT NULL,
    JOB_GROUP VARCHAR(80) NOT NULL,
    DESCRIPTION VARCHAR(120) NULL,
    JOB_CLASS_NAME   VARCHAR(128) NOT NULL, 
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    IS_STATEFUL VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP)
)ENGINE=INNODB DEFAULT CHARSET=latin1;

CREATE TABLE qrtz_job_listeners
  (
    JOB_NAME  VARCHAR(80) NOT NULL, 
    JOB_GROUP VARCHAR(80) NOT NULL,
    JOB_LISTENER VARCHAR(80) NOT NULL,
    PRIMARY KEY (JOB_NAME,JOB_GROUP,JOB_LISTENER),
    FOREIGN KEY (JOB_NAME,JOB_GROUP) 
	REFERENCES QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;

CREATE TABLE qrtz_triggers
  (
    TRIGGER_NAME VARCHAR(80) NOT NULL,
    TRIGGER_GROUP VARCHAR(80) NOT NULL,
    JOB_NAME  VARCHAR(80) NOT NULL, 
    JOB_GROUP VARCHAR(80) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    DESCRIPTION VARCHAR(120) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INT(13) NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(80) NULL,
    MISFIRE_INSTR INT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (JOB_NAME,JOB_GROUP) 
	REFERENCES QRTZ_JOB_DETAILS(JOB_NAME,JOB_GROUP) 
	ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=latin1;;

CREATE TABLE qrtz_simple_triggers
  (
    TRIGGER_NAME VARCHAR(80) NOT NULL,
    TRIGGER_GROUP VARCHAR(80) NOT NULL,
    REPEAT_COUNT INT(7) NOT NULL,
    REPEAT_INTERVAL INT(12) NOT NULL,
    TIMES_TRIGGERED INT(7) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
	ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=latin1;;

CREATE TABLE qrtz_cron_triggers
  (
    TRIGGER_NAME VARCHAR(80) NOT NULL,
    TRIGGER_GROUP VARCHAR(80) NOT NULL,
    CRON_EXPRESSION VARCHAR(80) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;
CREATE TABLE qrtz_blob_triggers
  (
    TRIGGER_NAME VARCHAR(80) NOT NULL,
    TRIGGER_GROUP VARCHAR(80) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
        REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;
CREATE TABLE qrtz_trigger_listeners
  (
    TRIGGER_NAME  VARCHAR(80) NOT NULL, 
    TRIGGER_GROUP VARCHAR(80) NOT NULL,
    TRIGGER_LISTENER VARCHAR(80) NOT NULL,
    PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_LISTENER),
    FOREIGN KEY (TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES QRTZ_TRIGGERS(TRIGGER_NAME,TRIGGER_GROUP)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;
CREATE TABLE qrtz_calendars
  (
    CALENDAR_NAME  VARCHAR(80) NOT NULL, 
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (CALENDAR_NAME)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;
CREATE TABLE qrtz_paused_trigger_grps
  (
    TRIGGER_GROUP  VARCHAR(80) NOT NULL, 
    PRIMARY KEY (TRIGGER_GROUP)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;
CREATE TABLE qrtz_fired_triggers 
  (
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(80) NOT NULL,
    TRIGGER_GROUP VARCHAR(80) NOT NULL,
    IS_VOLATILE VARCHAR(1) NOT NULL,
    INSTANCE_NAME VARCHAR(80) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    PRIORITY INT(13) NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(80) NULL,
    JOB_GROUP VARCHAR(80) NULL,
    IS_STATEFUL VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (ENTRY_ID)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;
CREATE TABLE qrtz_scheduler_state 
  (
    INSTANCE_NAME VARCHAR(80) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL INT(13) NOT NULL,
    PRIMARY KEY (INSTANCE_NAME)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;
CREATE TABLE qrtz_locks
  (
    LOCK_NAME  VARCHAR(40) NOT NULL, 
    PRIMARY KEY (LOCK_NAME)
)ENGINE=INNODB DEFAULT CHARSET=latin1;;


INSERT INTO qrtz_locks values('TRIGGER_ACCESS');
INSERT INTO qrtz_locks values('JOB_ACCESS');
INSERT INTO qrtz_locks values('CALENDAR_ACCESS');
INSERT INTO qrtz_locks values('STATE_ACCESS');
INSERT INTO qrtz_locks values('MISFIRE_ACCESS');


create index idx_qrtz_j_req_rqrtz_job_listenersecovery on qrtz_job_details(REQUESTS_RECOVERY);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(NEXT_FIRE_TIME);
create index idx_qrtz_t_state on qrtz_triggers(TRIGGER_STATE);
create index idx_qrtz_t_nft_st on qrtz_triggers(NEXT_FIRE_TIME,TRIGGER_STATE);
create index idx_qrtz_t_volatile on qrtz_triggers(IS_VOLATILE);
create index idx_qrtz_ft_trig_name on qrtz_fired_triggers(TRIGGER_NAME);
create index idx_qrtz_ft_trig_group on qrtz_fired_triggers(TRIGGER_GROUP);
create index idx_qrtz_ft_trig_nm_gp on qrtz_fired_triggers(TRIGGER_NAME,TRIGGER_GROUP);
create index idx_qrtz_ft_trig_volatile on qrtz_fired_triggers(IS_VOLATILE);
create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(INSTANCE_NAME);
create index idx_qrtz_ft_job_name on qrtz_fired_triggers(JOB_NAME);
create index idx_qrtz_ft_job_group on qrtz_fired_triggers(JOB_GROUP);
create index idx_qrtz_ft_job_stateful on qrtz_fired_triggers(IS_STATEFUL);
create index idx_qrtz_ft_job_req_recovery on qrtz_fired_triggers(REQUESTS_RECOVERY);

commit;
