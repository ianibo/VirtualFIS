
mysql -u root -p <<!!!
create database localchatterprod default charset UTF8 default collate utf8-bin;
grant all on localchatterprod.* to 'localchatter'@'localhost' identified by 'localchatter';
grant all on localchatterprod.* to 'localchatter'@'localhost.localdomain' identified by 'localchatter';
grant all on localchatterprod.* to 'localchatter'@'%' identified by 'localchatter';
!!!
