<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>数据同步系统-数据同步</title>
<%@include file="/WEB-INF/jsp/common/headConfig.jsp"%>
<style type="text/css">
.p-input{
 width: 200px;
 height: 25px;
 border: none;
 border: 1px solid #ebebeb;
}
.p-ul {
height: 260px;
width:100%
}
.form_fields {
	height: 260px;
	width:100%;
}
.form_fields li{
	width:100%;
	float:left;
	text-align:left;
	margin:0px 0px 10px 0px;
}
.form_fields li span{
	float:left;
	width:160px;
	overflow:hidden;
}
.form_fields li span input{
	width:13px;
	height:21px;
	margin:0px 2px 0px 0px;
	overflow:hidden;
}
.form_fields li span label{
	width:135px;
	overflow:hidden;
	margin:0px;
	white-space:nowrap;
}
label{
	font-weight:normal;
}
.errorMsg{
  color: red;
  display: none;
}
</style>
<script type="text/javascript" src="/js/date.js"></script>
<script type="text/javascript" src="/manage/js/sync_datas.js"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
	<div class="lp-container">
		<jsp:include page="/WEB-INF/jsp/common/left_auth.jsp" >
		  <jsp:param value="authli_data" name="auth_page"/>
		</jsp:include>
		<div class="data-container">
			<div class="detail">
				<ol class="lp-breadcrumb">
				<li><a href="/panel_manage/toMain.ac">主页</a></li>
				<li>></li>
		    	<li class="active green">数据同步</li>
			    </ol>
			    <div class="person-data">
			        <div class="msgcss">
			        	<c:choose>
			        		<c:when test="${status eq 'succ' }">
			        			同步数据提交成功
			        		</c:when>
			        	</c:choose>
			        </div>
			      <form:form id="submitForm" name="submitForm" method="post" modelAttribute="syncDatasVO" action="/panel_manage/syncDatas.ac" enctype="multipart/form-data">
					<p class="warn-btn">
					<span class="pt10 ml30" style="display:inline-block;"><span>选择同步游戏、开始、结束时间</span>
					</span>
					<button id="add" class="btn btn-sm warn-set" style="margin-left:400px;">提交</button>
				    </p>
				    
				    <ul class="form_fields mt20">
						<li class="clear-fix">
							<div>
								<form:checkboxes items="${games}" path="gameids" itemLabel="name" itemValue="id"/>
							</div>
						</li>
						<li class="clear-fix">
							<div>
								<label for="beginDate">开始时间</label>
								<form:input path="beginDate" cssClass="b-radius5 p-input" required="required" id="beginDate"/>
							</div>
						</li>
						<li class="clear-fix">
							<div>
								<label for="endDate">结束时间</label>
								<form:input path="endDate" cssClass="b-radius5 p-input" required="required" id="endDate"/>
							</div>
						</li>
						
					</ul>
				    
			    	</form:form>	
			    </div>
			</div>
		</div>
	</div>
</body>
</html>