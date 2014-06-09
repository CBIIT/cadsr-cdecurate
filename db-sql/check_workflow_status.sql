SELECT 
--DISTINCT a.asl_name --production sql
DISTINCT a.asl_name, c.actl_name  --just for test
	FROM   ac_status_lov_view a, asl_actl_view_ext c
	WHERE  a.asl_name = c.asl_name (+)
	and    (c.actl_name = nvl('DATAELEMENT', c.actl_name) OR c.actl_name = nvl('DE_CONCEPT', c.actl_name))  --just for test
--and c.asl_name = 'RELEASED' --just for test
	ORDER BY upper(a.asl_name);

  
select * FROM ac_status_lov_view;

select * FROM asl_actl_view_ext;