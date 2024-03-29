package com.hoolai.bi.report.service;

import java.util.List;

import com.hoolai.bi.report.model.entity.RealTimeNoClientResult;
import com.hoolai.bi.report.model.entity.RealTimeGame;
import com.hoolai.bi.report.vo.RealTimeGameVo;
import com.jian.service.GenericService;

public interface RealTimeGamesService  extends GenericService<RealTimeGame, Long>{
	
	
	int saveRealtTimeDataList(List<RealTimeGame> realTimeGameListVo);

	List<RealTimeGame> getSelectGamesList(RealTimeGameVo realTimeGameVo);
	
	//每天的所有数据
	List<RealTimeGame> getSelectGamesAllList(RealTimeGameVo realTimeGameVo);
	
	//如果服务因故障停了，停止时间段补充数据为0
    List<RealTimeGame> getSelectGamesIsDateList(RealTimeGameVo realTimeGameVo);
	
	//检测某个时间段是否有数据
    List<RealTimeGame> getSelectGamesIsList(RealTimeGameVo realTimeGameVo);
    
    //获取今天最新数据的时间
    List<RealTimeGame> getSelectNewDate(RealTimeGameVo realTimeGameVo);
    
    //查询近2天的数据
    List<RealTimeGame> getSelectGamesList2(RealTimeGameVo realTimeGameVo);
    
    //总览汇总
    List<RealTimeGameVo> getSelectGamesTotalList(RealTimeGameVo realTimeGameVo);
    
    RealTimeGame selectRealTimeData(RealTimeGame realTimeGame);
    
    //按小时
    List<RealTimeGameVo> getSelectGamesHourList(RealTimeGameVo realTimeGameVo);
    
   //按小时中的昨日，今日
    List<RealTimeGame> getSelectGamesListyesterday_day(RealTimeGameVo realTimeGameVo);
   
}
