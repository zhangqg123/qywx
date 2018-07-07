SELECT * FROM qywx_group
<#if groupIds ?exists && groupIds ?length gt 0>
	where id in (${groupIds})
</#if> 