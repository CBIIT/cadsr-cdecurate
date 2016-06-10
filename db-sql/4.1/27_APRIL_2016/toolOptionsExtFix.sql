-- On the Production database this row was missing.  One of the initialization scripts (customDownload.sql) inserts data for the field "value" in that row,
-- This will insert the missing row, and updates the "value" field.

DECLARE
sCount NUMBER(5);
BEGIN
    select count (*) INTO sCount from tool_options_ext where property ='CUSTOM.COLUMN.EXCLUDED' AND tool_name = 'CURATION';
    if sCount < 1  then
        INSERT INTO tool_options_ext (TOOL_NAME, PROPERTY) VALUES ('CURATION', 'CUSTOM.COLUMN.EXCLUDED');
        update tool_options_ext set value= 'CDE_IDSEQ,DEC_IDSEQ,VD_IDSEQ,Conceptual Domain Public ID,Conceptual Domain Short Name,Conceptual Domain Version,Conceptual Domain Context Name,Classification Scheme Public ID,DDE Methods,Representation Concept Origin,Value Domain Concept Origin,Property Concept Origin,Object Class Concept Origin,Derivation Type Description,DDE Preferred Name,DDE Preferred Definition,Document Organization,Value Domain Workflow Status,Data Element Concept Workflow Status,Property Workflow Status,Object Class Workflow Status,Data Element Concept Registration Status,Value Domain Registration Status' where property ='CUSTOM.COLUMN.EXCLUDED' AND tool_name = 'CURATION';
    END IF;
END;
/
