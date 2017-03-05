DROP TABLE IF EXISTS mtUser;

CREATE TABLE mtuser(
   userId text,
   email text,
   password text,
   PRIMARY KEY( userId )
);

CREATE UNIQUE INDEX user_key ON mtuser USING btree (userId);
CREATE UNIQUE INDEX email_key ON mtuser USING btree (email);

DROP TABLE IF EXISTS device;
CREATE TABLE device(
   deviceId text,
   deviceCode text,
   userId text,
   PRIMARY KEY( deviceId )
);

CREATE UNIQUE INDEX device_pkey ON device USING btree (deviceId);
CREATE UNIQUE INDEX deviceCode_pkey ON device USING btree (deviceCode);

DROP TABLE IF EXISTS message;
CREATE TABLE message(

   deviceId text,
   channel integer,
   message text,
   messageId text,
   PRIMARY KEY( messageId )
);

CREATE UNIQUE INDEX message_key ON message USING btree (messageId);