use etl;
alter table total_user_collect drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_user_info drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_user_info_noclient drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table role_choice_collect drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_role_choice_info drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table install_info_collect drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_install_info drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_install_info_noclient drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table pcu_acu_source drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table dau_info_collect drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_dau_info_noclient drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table nt_payment_tmp drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table payment_info_collect drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_payment_info drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table accumulate_payment_info_noclient drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
use aggr;
alter table a_user_day_new drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');
alter table a_user_new drop partition(snid='${snid}', gameid='${gameid}', ds='${ds}');