DROP TABLE IF EXISTS mtUser;

CREATE TABLE mtuser(
   userId text,
   email text,
   password text,
   PRIMARY KEY( userId )
);

drop index if exists user_key;
CREATE UNIQUE INDEX user_key ON mtuser USING btree (userId);
drop index if exists email_key;
CREATE UNIQUE INDEX email_key ON mtuser USING btree (email);

DROP TABLE IF EXISTS device;
CREATE TABLE device(
   deviceId text,
   deviceCode text,
   deviceName text,
   userId text,
   PRIMARY KEY( deviceId )
);

drop index if exists device_key;
CREATE UNIQUE INDEX device_key ON device USING btree (deviceId);
drop index if exists deviceCode_key;
CREATE UNIQUE INDEX deviceCode_key ON device USING btree (deviceCode);

DROP TABLE IF EXISTS message;
CREATE TABLE message(

   deviceId text,
   channel integer,
   message text,
   messageId text,
   PRIMARY KEY( messageId )
);

drop index if exists message_key;
CREATE UNIQUE INDEX message_key ON message USING btree (messageId);