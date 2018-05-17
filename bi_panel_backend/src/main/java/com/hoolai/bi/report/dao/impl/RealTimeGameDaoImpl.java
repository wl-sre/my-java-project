package com.hoolai.bi.report.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.report.dao.RealTimeGamesDao;
import com.hoolai.bi.report.model.entity.RealTimeNoClientResult;
import com.hoolai.bi.report.model.entity.RealTimeGame;
import com.hoolai.bi.report.vo.RealTimeGameVo;
import com.hoolai.dao.impl.GenericDaoImpl;
import com.hoolai.dao.pagination.AbstractObjectVO;
import com.hoolai.dao.pagination.Pagination;
@Repository
public  class RealTimeGameDaoImpl  extends GenericDaoImpl<RealTimeGame, Long>  implements RealTimeGamesDao {

	
	@Override
	public int saveRealtTimeDataList(List<RealTimeGame> realTimeGameVo) {
		return this.sqlSessionTemplate.insert("saveRealTimeDataList", realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesList(RealTimeGameVo realtTimeGameVo) {
	
		return this.sqlSessionTemplate.selectList("getSelectGamesList",realtTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesAllList(
			RealTimeGameVo realtTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesAllList",realtTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesIsList(RealTimeGameVo realTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesIsList",realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesIsDateList(
			RealTimeGameVo realTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesIsDateList",realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectNewDate(RealTimeGameVo realTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectNewDate",realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesList2(RealTimeGameVo realTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesListday2",realTimeGameVo);
	}

	@Override
	public List<RealTimeGameVo> getSelectGamesTotalList(
			RealTimeGameVo realTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesTotalList",realTimeGameVo);
	}
	
	@Override
	public RealTimeGame selectRealTimeData(RealTimeGame realTimeGame) {
		return this.sqlSessionTemplate.selectOne("selectRealTimeData", realTimeGame);
	}

	@Override
	public List<RealTimeGameVo> getSelectGamesHourList(
			RealTimeGameVo realTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesHourList",realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesListyesterday_day(
			RealTimeGameVo realTimeGameVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesListyesterday_day",realTimeGameVo);
	}

	
}
