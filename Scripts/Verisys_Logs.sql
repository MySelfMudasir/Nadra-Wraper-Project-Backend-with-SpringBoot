-- 1) Sequence for log_id
CREATE SEQUENCE verisy_logs_seq
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

-- 2) verisy_logs table
CREATE TABLE verisy_logs (
  log_id               NUMBER        PRIMARY KEY,               -- auto from sequence
  cnic                 VARCHAR2(20)  NOT NULL,
  jp_request           CLOB          NOT NULL,                  -- raw JP form-data JSON/text
  NADRA_VERIFY_CITIZEN_REQUEST  CLOB not null,
  NADRA_VERIFY_CITIZEN_RESPONSE CLOB not null,
  nadra_request        CLOB          NOT NULL,                  -- JSON sent to NADRA
  nadra_response       CLOB         NOT NULL,                  -- JSON returned by NADRA
  request_timestamp    TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
  response_timestamp   TIMESTAMP
);

-- 3) Trigger to fill log_id
CREATE OR REPLACE TRIGGER trg_verisy_logs_bi
  BEFORE INSERT ON verisy_logs
  FOR EACH ROW
BEGIN
  IF :NEW.log_id IS NULL THEN
    SELECT verisy_logs_seq.NEXTVAL
      INTO :NEW.log_id
      FROM DUAL;
  END IF;
END;
/
