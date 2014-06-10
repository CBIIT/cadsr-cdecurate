SELECT 
DISTINCT a.asl_name --production sql
--DISTINCT a.asl_name, c.actl_name  --just for test
	FROM   ac_status_lov_view a, asl_actl_view_ext c
	WHERE  a.asl_name = c.asl_name (+)
	and    c.actl_name in ('DATAELEMENT','DE_CONCEPT')  --just for test
--and c.asl_name = 'RELEASED' --just for test
	ORDER BY upper(a.asl_name);

  
select 
--count(*)
* 
FROM ac_status_lov_view where asl_name in (
'APPRVD FOR TRIAL USE'
,'CMTE APPROVED'
,'CMTE SUBMTD'
,'CMTE SUBMTD USED'
,'DRAFT MOD'
,'DRAFT NEW'
,'RELEASED'
,'RELEASED-NON-CMPLNT'
,'RETIRED ARCHIVED'
,'RETIRED DELETED'
,'RETIRED PHASED OUT'
,'RETIRED WITHDRAWN'
);

select 
--count(*)
* 
FROM ac_status_lov where asl_name in (
'APPRVD FOR TRIAL USE'
,'CMTE APPROVED'
,'CMTE SUBMTD'
,'CMTE SUBMTD USED'
,'DRAFT MOD'
,'DRAFT NEW'
,'RELEASED'
,'RELEASED-NON-CMPLNT'
,'RETIRED ARCHIVED'
,'RETIRED DELETED'
,'RETIRED PHASED OUT'
,'RETIRED WITHDRAWN'
);

select 
--count(*)
* 
FROM asl_actl_view_ext where asl_name in (
'APPRVD FOR TRIAL USE'
,'CMTE APPROVED'
,'CMTE SUBMTD'
,'CMTE SUBMTD USED'
,'DRAFT MOD'
,'DRAFT NEW'
,'RELEASED'
,'RELEASED-NON-CMPLNT'
,'RETIRED ARCHIVED'
,'RETIRED DELETED'
,'RETIRED PHASED OUT'
,'RETIRED WITHDRAWN'
)
and    actl_name in ('DATAELEMENT','DE_CONCEPT');

select 
--count(*)
* 
FROM asl_actl_ext where asl_name in (
'APPRVD FOR TRIAL USE'
,'CMTE APPROVED'
,'CMTE SUBMTD'
,'CMTE SUBMTD USED'
,'DRAFT MOD'
,'DRAFT NEW'
,'RELEASED'
,'RELEASED-NON-CMPLNT'
,'RETIRED ARCHIVED'
,'RETIRED DELETED'
,'RETIRED PHASED OUT'
,'RETIRED WITHDRAWN'
)
and    actl_name in ('DATAELEMENT','DE_CONCEPT');
