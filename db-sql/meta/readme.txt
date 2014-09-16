*** Launching

1. Decide if you need to run what type of script, say designation
2. invoke sdes.cmd
3. Sample output should be like:

SQL*Loader: Release 10.2.0.1.0 - Production on Tue Sep 16 11:40:20 2014

Copyright (c) 1982, 2005, Oracle.  All rights reserved.

Commit point reached - logical record count 2
Commit point reached - logical record count 3

Press any key to continue . . .

*** Testing

To validate that the data are inserted correctly, execute the following SQL:

--- designation test ---
SELECT d.date_modified, MODIFIED_BY, d.lae_name, d.name, d.detl_name FROM sbr.designations_view d
where
d.date_modified is not NULL
--rownum < 31
order by d.date_modified desc
--order by d.date_created desc

--- permissible values test ---
select pv.VALUE, vm.vm_id, vm.version, vm.vm_idseq, vm.PREFERRED_NAME, vm.LONG_NAME, vm.SHORT_MEANING, vm.DESCRIPTION
--, pv.SHORT_MEANING, pv.MEANING_DESCRIPTION,
from SBR.VALUE_MEANINGS_VIEW vm, SBR.PERMISSIBLE_VALUES_VIEW pv where vm.VM_IDSEQ = pv.VM_IDSEQ
and vm.vm_id = '4211591'
and pv.date_modified is not NULL
order by d.date_modified desc
--order by pv.date_created desc
