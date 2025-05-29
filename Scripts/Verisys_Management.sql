CREATE SEQUENCE verisys_mgmt_seq
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

-- 2) Create the verisys_management table
CREATE TABLE  verisys_management (
  management_id   NUMBER         PRIMARY KEY,       -- will be filled by trigger
  cnic            VARCHAR2(20)   NOT NULL,
  created_at      TIMESTAMP      DEFAULT SYSTIMESTAMP NOT NULL,
  status          CHAR(1)        DEFAULT 'P'          NOT NULL
                        CHECK (status IN ('P','A','F')),
  session_id      VARCHAR2(50),
  retry_count     NUMBER         DEFAULT 0            NOT NULL,
  contact_number  VARCHAR2(20),
  issue_date      DATE
);

-- 3) Trigger to assign sequence value on insert
CREATE OR REPLACE TRIGGER  trg_verisys_mgmt_bi
  BEFORE INSERT ON  verisys_management
  FOR EACH ROW
BEGIN
  IF :NEW.management_id IS NULL THEN
    SELECT  verisys_mgmt_seq.NEXTVAL
      INTO :NEW.management_id
      FROM DUAL;
  END IF;
END;
/
