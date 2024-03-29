package com.hoolai.bi.report.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hoolai.bi.report.dao.RealTimeGameSourceDao;
import com.hoolai.bi.report.model.entity.RealTimeGameClient;
import com.hoolai.bi.report.model.entity.RealTimeGameSource;
import com.hoolai.bi.report.vo.RealTimeGameClientVo;
import com.hoolai.bi.report.vo.RealTimeGameSourceVo;
import com.hoolai.bi.report.vo.RealTimeGameVo;
import com.hoolai.dao.impl.GenericDaoImpl;
import com.hoolai.dao.pagination.AbstractObjectVO;
import com.hoolai.dao.pagination.Pagination;
@Repository
public class RealTimeGameSourceDaoImpl extends GenericDaoImpl<RealTimeGameSource, Long> implements RealTimeGameSourceDao {

	@Override
	public int saveRealTimeSourceDataList(
			List<RealTimeGameSource> realTimeGameSource) {
		return this.sqlSessionTemplate.insert("saveRealTimeSourceDataList", realTimeGameSource);
	}

	@Override
	public List<RealTimeGameSourceVo> getSelectGamesSourceList(
			RealTimeGameSourceVo realTimeGameSourceVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesSourceList",realTimeGameSourceVo);
	}

	@Override
	public long getSelectGamesSourceCount(
			RealTimeGameSourceVo realTimeGameSourceVo) {
		return this.sqlSessionTemplate.selectOne("getSelectGamesSourceCount",realTimeGameSourceVo);
	}

	@Override
	public void deleteRealTimeSourceData(
			RealTimeGameSourceVo realTimeGameSourceVo) {
		this.sqlSessionTemplate.selectOne("deleteRealTimeSourceData",realTimeGameSourceVo);
		
	}

	@Override
	public long getSelectCountSourcedata(
			RealTimeGameSourceVo realTimeGameSourceVo) {
		return this.sqlSessionTemplate.selectOne("getSelectCountSourcedata",realTimeGameSourceVo);
	}

	@Override
	public List<RealTimeGameSourceVo> getSelectGamesSourceIconList(
			RealTimeGameSourceVo realTimeGameSourceVo) {
		return this.sqlSessionTemplate.selectList("getSelectGamesSourceIconList",realTimeGameSourceVo);
	}

	@Override
	public String getSelectMaxDau(RealTimeGameSourceVo realTimeGameSourceVo) {

		return this.sqlSessionTemplate.selectOne("getSelectMaxDau",realTimeGameSourceVo);
	}

	

	@Override
	public List<String> selectGameSources(RealTimeGameSourceVo realTimeGameSourceVo) {
		return this.sqlSessionTemplate.selectList("selectGameSources2", realTimeGameSourceVo);
	}
}
