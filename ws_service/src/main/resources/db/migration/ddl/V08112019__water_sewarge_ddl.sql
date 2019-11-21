DROP TABLE IF EXISTS water_service_connection;
DROP TABLE IF EXISTS sewarage_service_connection;
DROP TABLE IF EXISTS connection;

CREATE TABLE connection
(
  id character varying(64) NOT NULL,
  property_id character varying(64) NOT NULL,
  applicationno character varying(64) NOT NULL,
  applicationstatus character varying(256) NOT NULL,
  status character varying(64) NOT NULL,
  connectionno character varying(256) NOT NULL,
  oldconnectionno character varying(64) NOT NULL,
  documents_id character varying(256) NOT NULL,
  CONSTRAINT connection_pkey PRIMARY KEY (id)
);

CREATE TABLE water_service_connection
(
  connection_id character varying(64) NOT NULL,
  connectioncategory character varying(32) NOT NULL,
  rainwaterharvesting boolean NOT NULL,
  connectiontype character varying(32) NOT NULL,
  watersource character varying(64) NOT NULL,
  meterid character varying(64) NOT NULL,
  meterinstallationdate bigint NOT NULL,
  CONSTRAINT water_service_connection_connection_id_fkey FOREIGN KEY (connection_id) REFERENCES connection (id) 
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

