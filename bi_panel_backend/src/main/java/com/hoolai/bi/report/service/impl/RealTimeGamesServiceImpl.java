package com.hoolai.bi.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoolai.bi.report.dao.RealTimeGamesDao;
import com.hoolai.bi.report.dao.UserRetentionLtvDao;
import com.hoolai.bi.report.model.entity.RealTimeNoClientResult;
import com.hoolai.bi.report.model.entity.RealTimeGame;
import com.hoolai.bi.report.model.entity.UserRetentionLtv;
import com.hoolai.bi.report.service.RealTimeGamesService;
import com.hoolai.bi.report.vo.RealTimeGameVo;
import com.hoolai.dao.GenericDao;
import com.hoolai.dao.pagination.AbstractObjectVO;
import com.hoolai.dao.pagination.Pagination;
import com.jian.service.impl.GenericServiceImpl;
import com.jian.service.pagination.PaginationResult;
@Service
public class RealTimeGamesServiceImpl  extends GenericServiceImpl<RealTimeGame, Long> implements RealTimeGamesService {
	
	@Autowired
	private RealTimeGamesDao entityDao;
	
	@Override
    public GenericDao<RealTimeGame, Long> getGenricDao() {
            return this.entityDao;
    }
	
	@Override
	public int saveRealtTimeDataList(List<RealTimeGame> realtTimeGameVo) {
		
		return entityDao.saveRealtTimeDataList(realtTimeGameVo);
	}
	@Override
	public List<RealTimeGame> getSelectGamesList(RealTimeGameVo realtTimeGameVo) {
		
		return entityDao.getSelectGamesList(realtTimeGameVo);
	}
	@Override
	public List<RealTimeGame> getSelectGamesAllList(
			RealTimeGameVo realtTimeGameVo) {
		
		return entityDao.getSelectGamesAllList(realtTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesIsList(RealTimeGameVo realTimeGameVo) {
		
		return entityDao.getSelectGamesIsList(realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesIsDateList(
			RealTimeGameVo realTimeGameVo) {
		return entityDao.getSelectGamesIsDateList(realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectNewDate(RealTimeGameVo realTimeGameVo) {
		
		return entityDao.getSelectNewDate(realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesList2(RealTimeGameVo realTimeGameVo) {
		return entityDao.getSelectGamesList2(realTimeGameVo);
	}

	@Override
	public List<RealTimeGameVo> getSelectGamesTotalList(
			RealTimeGameVo realTimeGameVo) {
		return entityDao.getSelectGamesTotalList(realTimeGameVo);
	}

	@Override
	public RealTimeGame selectRealTimeData(RealTimeGame realTimeGame) {
		return entityDao.selectRealTimeData(realTimeGame);
	}

	@Override
	public List<RealTimeGameVo> getSelectGamesHourList(
			RealTimeGameVo realTimeGameVo) {
		return entityDao.getSelectGamesHourList(realTimeGameVo);
	}

	@Override
	public List<RealTimeGame> getSelectGamesListyesterday_day(
			RealTimeGameVo realTimeGameVo) {
		return entityDao.getSelectGamesListyesterday_day(realTimeGameVo);
	}



}
