#!/bin/sh

mysql -f -v -u root << EOSQL
drop database nakamura;
create database nakamura  default character set utf8;
grant all on sakaiuser.* to sakaiuser@'127.0.0.1' identified by 'ironchef';
grant all on sakaiuser.* to sakaiuser@'localhost' identified by 'ironchef';
exit
EOSQL


