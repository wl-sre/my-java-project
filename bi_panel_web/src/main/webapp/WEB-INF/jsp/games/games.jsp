<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="robots" content="noodp, noydir" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
<title>系列总览</title>
<%@include file="/WEB-INF/jsp/common/headConfig.jsp"%>
<link rel="stylesheet" href="/css/bootstrap.min.css"/>
<link rel="stylesheet" href="/css/laydate.css"/>
<link rel="stylesheet" href="/css/skin_laydate.css"/>
<link rel="stylesheet" href="/css/page.css?v=${version}"/>
<link rel="stylesheet" href="/css/main/main.css?v=${version}"  />

<style>
#selectDate span.ui-selected,#tabs li.ui-selected {
	background: #56C887;
	color: white;
}

#selectDate span.last,#tabs li.last {
	border-right: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
	border-top: 1px solid #ebebeb;
}

#selectDate span,#tabs li {
	float: left;
	width: 85px;
	margin: -3px auto;
	text-align: center;
	font-size: 14px;
	line-height: 27px;
	border-left: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
	border-top: 1px solid #ebebeb;
}

#tabs {
	min-height: 450px;
	margin-top: 20px;
}

.data_table {
	width: 100%;
	overflow: hidden;
	margin-top: 20px;
}

.zhibiao_left {
	width: 20%;
	height: 100px;
	float: left;
	margin-top: 20px;
	border-left: 1px solid #ebebeb;
	border-top: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
	border-radius: 10px;
}

.zhibiao_right {
	width: 20%;
	height: 100px;
	float: left;
	margin-top: 20px;
	border-radius: 10px;
	border: 1px solid #ebebeb;
}

.data_ul li {
	padding-top: 10px;
}

.data_ul title {
	font-size: 20px;
	text-align: center;
}

.dataTables_wrapper .dataTables_filter {
	margin-left: 10px;
	z-index: 1;
	position: absolute;
}

caption {
	font-style: normal;
	text-align: right;
	margin-right: 30px;
}

#data3 td {
    min-width: 150px;
}

.detail-info {
	margin-top:10px;
	min-height:35px;
	padding-bottom: 10px;
	
}

.navbar {
	background: url(/images/logo_banner_repeat.png)  repeat-x;
}


</style>
<script type="text/javascript" src="/js/games/games.js?v=${version}"></script>
</head>
<body class="dt-example">
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<form:form id="downLoadForm" method="post" modelAttribute="gamesVO" action="/panel_bi/gameRealTime/downloadSeriesGameData.ac" enctype="multipart/form-data">
<input type="hidden" id="gameTypeId" name="gameTypeId" value="${gameTypeId}" />
<input type="hidden" id="gamesTypeName" name="gamesTypeName" value="${gamesTypeName}" />
<input type="hidden" id="dataType" name="dataType" value="3" />
<input type="hidden" id="allGame" name="allGame" value="3" />
</form:form>

<div class="data-container-main">
	<div class="detail-info">
		<ul class="detail-ul">
			<li class="detail-li"><span class="detail-left"> <span
					id="selectDate" style="padding-left: 20px; overflow: hidden;">
						<span css="dateSelect" id="today" val="3" class="ui-selected">近3天</span>
						<!-- <span css="dateSelect" id="lst2day" val="7" class="last">近7天</span> -->
				</span>
				<input type="text" class="b-radius5 p-input" style="padding-left:10px;width:105px;"
					value="${beginTime }" id="beginTime" name="beginTime" /> ~ 
				<input type="text" class="b-radius5 p-input" style="padding-left:10px;width:105px;"
					value="${endTime}" id="endTime" name="endTime" />
			</span> <span class="detail-right"
				style="margin-right: 2.5%; padding-top: 6px; text-align: right;">
					<button id="query" type="button" class="btn btn-default btn-sm"
						style="width: 80px; padding-top: 6px;">查询</button>
			</span></li>
		</ul>
	</div>
	
	<div style="text-align: left;font-size: larger;margin: 10px 0 0 10px;">
			${gamesTypeName}系列
	</div>
	<div class="data">
	<div class="data_table">
		<div id="install_count" class="zhibiao_left">
			<ul class="data_ul">
				<li style="text-align: center; font-size: 14px;">累计新注册用户数</li>
				<li id="c_ul_dnu"
					style="text-align: center; font-size: 16px; font-weight: bold;">0</li>
			</ul>
		</div>
		<div id="dau_count" class="zhibiao_left">
			<ul class="data_ul">
				<li style="text-align: center; font-size: 14px;">累计活跃用户数</li>
				<li id="c_ul_dau"
					style="text-align: center; font-size: 16px; font-weight: bold;">0</li>
			</ul>
		</div>
		<div id="pay_count" class="zhibiao_left">
			<ul class="data_ul">
				<li style="text-align: center; font-size: 14px;">累计付费(元)</li>
				<li id="c_ul_payment"
					style="text-align: center; font-size: 16px; font-weight: bold;">0</li>
			</ul>
		</div>
		<div id="arpu_count" class="zhibiao_left">
			<ul class="data_ul">
				<li style="text-align: center; font-size: 14px;">平均ARPU(元)</li>
				<li id="c_ul_arpu"
					style="text-align: center; font-size: 16px; font-weight: bold;">0</li>
			</ul>
		</div>
		<div id="arppu_count" class="zhibiao_right">
			<ul class="data_ul">
				<li style="text-align: center; font-size: 14px;">平均ARPPU(元)</li>
				<li id="c_ul_arppu"
					style="text-align: center; font-size: 16px; font-weight: bold;">0</li>
			</ul>
		</div>
	</div>
	
	<div id="tabs">
		<ul class="clear-fix">
			<li css="tabs" id="h_1" val="tabs-1" class="ui-selected">按系列</li>
			<li css="tabs" id="h_2" val="tabs-2" class="last">按游戏</li>
		</ul>
		<div id="tabs-1">
		   <div id="data1" class="detail-table">
			
		   </div>
		</div>
		<div id="tabs-2">
		   <div id="data2" class="detail-table">
			
		   </div>
		</div>
	</div>
	</div>
	<div class='ajax_loading' hidden="hidden" style='height:60px;width:100%;text-align:center;line-height:60px;font-size:16px;display:none;'>
	   	<span class="ajax_loading_span">数据正在计算中,请等待大约<span style="padding: 0px 5px;color: #F13D6D">2</span>分钟</span>
	   	<img class="ajax_loading_img" src="/images/loading.gif" />
	</div>
</div>

<div hidden="hidden" class="template_cache">
	<table class="table_1">
		<!-- <caption>
			<button type="button" class="btn btn-default btn-sm" style="width:80px;">下载数据</button>
        </caption> -->
		<thead>
			<tr>
				<td>日期</td>
				<td>新注册</td>
				<td>活跃</td>
				<td>收入(元)</td>
				<td>arpu(元)</td>
				<td>arppu(元)</td>
				<td>激活设备数</td>
				<td>付费人数</td>
			</tr>
		</thead>
		<tbody>
            <tr>
				<td class="endDate"></td>
				<td class="dnu"></td>
				<td class="dau"></td>
				<td class="paymentAmount"></td>
				<td class="arpu"></td>
				<td class="arppu"></td>
				<td class="equipment"></td>
				<td class="payer"></td>
			</tr>
		</tbody>
		<tfoot></tfoot>
	</table>
	
	<table class="table_2">
		<!-- <caption>
			<button type="button" class="btn btn-default btn-sm" style="width:80px;">下载数据</button>
        </caption> -->
		<thead>
			<tr>
				<td>日期</td>
				<td>游戏名</td>
				<td>新注册</td>
				<td>活跃</td>
				<td>收入(元)</td>
				<td>arpu(元)</td>
				<td>arppu(元)</td>
				<td>激活设备数</td>
				<td>付费人数</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="endDate"></td>
				<td class="gameName"></td>
				<td class="dnu"></td>
				<td class="dau"></td>
				<td class="paymentAmount"></td>
				<td class="arpu"></td>
				<td class="arppu"></td>
				<td class="equipment"></td>
				<td class="payer"></td>
			</tr>
		</tbody>
		<tfoot></tfoot>
	</table>
</div>

</body>
</html>
