select t1.install_creative,t1.dau,
case when t2.install is null then 0 else t2.install end as dnu,
case when t4.equips is null then 0 else t4.equips end as equips,
case when t3.payer is null then 0 else t3.payer end as payer,
case when t3.total_amount is null then 0 else  t3.total_amount  end as total_amount,
case when t5.dau_total is null then 0 else t5.dau_total end as dau_total,
case when t6.payer_total is null then 0 else t6.payer_total end as payer_total,
'${snid}' as snid,'${gameid}' as gameid,'${ds}' as day,'${finish_time}' as finish_time,
'${start_time}' as start_time
from 




(select case when b.install_creative is null then 'unknown' else b.install_creative end as install_creative,count(distinct a.userid) as dau
from 
(select userid,date1,time1
from 
(select  userid,install_date as date1,install_time as time1 from default.nt_install where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and install_time <='${finish_time}' and  install_time >'${start_time}'
union all 
select  userid,dau_date as date1,dau_time as time1  from default.nt_dau where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and dau_time <='${finish_time}' and dau_time >'${start_time}'
union all
select userid ,counter_date as date1,counter_time from default.nt_counter where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and counter_time <='${finish_time}' and counter_time>'${start_time}'
union all
select userid,economy_date as date1,economy_time as time1  from default.nt_economy where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and economy_time <='${finish_time}' and economy_time>'${start_time}')t 
group by userid,date1,time1)a

left outer join 


(select userid,split(min(concat(first_date,'=',install_creative)),'=')[2] as install_creative
from
(select userid,first_date,install_creative from aggr.a_user_new 
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-2,date('${ds}')) as varchar)

union all

select userid,concat(install_date,' ',install_time) as first_date,case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-1,date('${ds}')) as varchar)


union all
select userid,concat(install_date,' ',install_time) as first_date,
case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and install_time <='${finish_time}')t
group by userid)b
on a.userid=b.userid
group by case when b.install_creative is null then 'unknown' else b.install_creative end)t1


left outer join 



(select install_creative,count(distinct userid) as install
from 
(select t1.userid,t1.install_creative
from 
(select userid,concat(install_date,' ',install_time) as first_date,case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install
where  snid='${snid}' and gameid='${gameid}' and ds='${ds}'   and install_time <='${finish_time}' and  install_time >'${start_time}')t1
left outer join 
(select userid
from aggr.a_user_new
where snid='${snid}' and gameid='${gameid}' and date(ds)=date_add('day',-2,date'${ds}')
union all
select userid
from default.nt_install
where  snid='${snid}' and gameid='${gameid}'  and  date(ds)=date_add('day',-1,date'${ds}')
union all
select userid
from default.nt_install
where  snid='${snid}' and gameid='${gameid}' and ds='${ds}' and  install_time <='${start_time}'
)t2
on t1.userid=t2.userid
where t2.userid is null)t
group by install_creative)t2
on upper(t1.install_creative)=upper(t2.install_creative)

left outer join 


(select case when t3.install_creative is null then 'unknown' else t3.install_creative end as install_creative,
count(distinct t1.userid) as payer,
sum(cast(amount as double)/cast(rate as double)/if(exchange_rate is null,1,exchange_rate)) as total_amount
from 
(select snid,gameid,userid,amount,currency
from default.nt_payment
where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and payment_time <='${finish_time}' and payment_time>'${start_time}' and  cast(amount as double)>0)t1
left outer join 
(select snid,gameid,rate
from dimension.all_game_name)t2
on t1.snid=t2.snid and t1.gameid=t2.gameid
left outer join
(select currency,cast(exchange_rate as double) as exchange_rate
from dimension.d_exchange_rate where date(ds)=date_add('day',-1,date'${ds}') and currency<>'CNY')c
on t1.currency=c.currency
left outer join 
(select userid,split(min(concat(first_date,'=',install_creative)),'=')[2] as install_creative
from
(select userid,first_date,install_creative from aggr.a_user_new 
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-2,date('${ds}')) as varchar)
union all
select userid,concat(install_date,' ',install_time) as first_date,case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-1,date('${ds}')) as varchar)
union all
select userid,concat(install_date,' ',install_time) as first_date,
case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and install_time <='${finish_time}')t
group by userid)t3
on t1.userid=t3.userid
group by case when t3.install_creative is null then 'unknown' else t3.install_creative end )t3




on upper(t1.install_creative)=upper(t3.install_creative) 

left outer join


(select equip_creative,count(distinct udid) as equips 
from 
(select a.udid,case when a.creative is null or a.creative ='null' or a.creative='' then 'unknown' else upper(a.creative)  end  as equip_creative
from 
(select udid,creative
from default.nt_equipment where snid='${snid}' AND gameid='${gameid}' and   eq_time <='${finish_time}'  and eq_time >'${start_time}' and ds='${ds}' and udid <> '' and udid is not null and udid<>'null'
group by udid,creative)a
left outer join 
(select udid
from aggr.a_equipment
where snid='${snid}' and gameid='${gameid}' and date(ds)=date_add('day',-2,date'${ds}')
union all
select udid
from default.nt_equipment
where snid='${snid}' and gameid='${gameid}' and date(ds)=date_add('day',-1,date'${ds}')
union all
select udid
from default.nt_equipment
where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and eq_time <='${start_time}')b 
on a.udid=b.udid
where b.udid is null ) t 
group by equip_creative)t4


on upper(t1.install_creative)=upper(t4.equip_creative) 



left outer join 


(select case when b.install_creative is null then 'unknown' else b.install_creative end as install_creative,count(distinct a.userid) as dau_total
from 
(select userid,date1,time1
from 
(select  userid,install_date as date1,install_time as time1 from default.nt_install where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and install_time <='${finish_time}' 
union all 
select  userid,dau_date as date1,dau_time as time1  from default.nt_dau where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and dau_time <='${finish_time}' 
union all
select userid ,counter_date as date1,counter_time from default.nt_counter where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and counter_time <='${finish_time}' 
union all
select userid,economy_date as date1,economy_time as time1  from default.nt_economy where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and economy_time <='${finish_time}' )t 
group by userid,date1,time1)a

left outer join 


(select userid,split(min(concat(first_date,'=',install_creative)),'=')[2] as install_creative
from
(select userid,first_date,install_creative from aggr.a_user_new 
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-2,date('${ds}')) as varchar)

union all

select userid,concat(install_date,' ',install_time) as first_date,case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-1,date('${ds}')) as varchar)


union all
select userid,concat(install_date,' ',install_time) as first_date,
case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and install_time <='${finish_time}')t
group by userid)b
on a.userid=b.userid
group by case when b.install_creative is null then 'unknown' else b.install_creative end)t5

on upper(t1.install_creative)=upper(t5.install_creative) 

left outer join 

(select case when t3.install_creative is null then 'unknown' else t3.install_creative end as install_creative,
count(distinct t1.userid) as payer_total
from 
(select snid,gameid,userid,amount
from default.nt_payment
where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and payment_time <='${finish_time}'  and  cast(amount as double)>0)t1
left outer join 
(select userid,split(min(concat(first_date,'=',install_creative)),'=')[2] as install_creative
from
(select userid,first_date,install_creative from aggr.a_user_new 
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-2,date('${ds}')) as varchar)
union all
select userid,concat(install_date,' ',install_time) as first_date,case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install
where snid='${snid}' and gameid='${gameid}' and ds=cast(date_add('day',-1,date('${ds}')) as varchar)
union all
select userid,concat(install_date,' ',install_time) as first_date,
case when creative is null or creative = '' then 'unknown' else creative end as install_creative
from default.nt_install where snid='${snid}' and gameid='${gameid}' and ds='${ds}' and install_time <='${finish_time}')t
group by userid)t3
on t1.userid=t3.userid
group by case when t3.install_creative is null then 'unknown' else t3.install_creative end )t6

on upper(t1.install_creative)=upper(t6.install_creative) 