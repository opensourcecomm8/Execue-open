/*
  Script to cleanup the Data Source table during installation process
  This script is specific to Enterprise Installation

Process.,
  1. Delete unwanted data source entries
  2. Set password encrypted flag on answerscatalog entry to be 'N'
  3. Delete uploaded entry
  4. Insert entry for uploaded from answerscatalog entry

Uploaded entry is made same as answerscatalog in order to avoid asking another entry details from user
  for Enterprise.
  
*/

delete from data_source where id in (1001, 1002, 1003, 1004, 1005);

update data_source set password_encrypted = 'N' where id = 1;

delete from data_source where id = 2;

insert into data_source (id,name,description,provider,conn_type,jndi_name,
    user_name,password,password_encrypted,location,port,schema_name,owner,
    jndi_conn_factory,jndi_provider_url,type,display_name)
select 2,'uploaded','uploaded',provider,conn_type,jndi_name,
    user_name,password,password_encrypted,location,port,schema_name,owner,
    jndi_conn_factory,jndi_provider_url,'UPLOADED','Uploaded' 
from data_source where id = 1;
