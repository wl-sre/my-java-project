<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>游戏管理-添加游戏</title>
<%@include file="/WEB-INF/jsp/common/headConfig.jsp"%>
<style type="text/css">
.p-input{
 width: 200px;
 height: 25px;
 border: none;
 border: 1px solid #ebebeb;
}

.p-ul {
height: 350px;
width:100%
}

.p-ul li{
 padding-bottom: 20px;
 width:100%;
 overflow: hidden;
}

.p-ul span{
 width:32%;
 text-align: left;
 float: left;
}

.errorMsg{
  color: red;
  display: none;
}
</style>
<script type="text/javascript" src="/js/ajaxfileupload.js"></script>
 
<script type="text/javascript" src="/js/timezone.js"></script>

</head>
<body>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
	<div class="lp-container">
		<jsp:include page="/WEB-INF/jsp/common/left_auth.jsp" >
		  <jsp:param value="authli_etlengineManage" name="auth_page"/>
		</jsp:include>
		<div class="data-container" style="min-height:700px">
			<div class="detail">
				<ol class="lp-breadcrumb">
				<li><a href="/panel_manage/toMain.ac">主页</a></li>
    			<li>></li>
    			<li><a href="/panel_manage/EtlengineManage/toEtlengineManage.ac">ETL模板管理</a></li>
				<li>></li>
		    	<li class="active green">编辑模板</li>
		    	
			    </ol>
			    <div class="person-data">
			      <form:form id="submitForm" name="submitForm" method="post" modelAttribute="etlengineManageVO" action="/panel_manage/EtlengineManage/EtlengineManageEdit_${prePageNo }.ac" enctype="multipart/form-data">
					
					<p class="warn-btn">
					<span class="pt10 ml30" style="display:inline-block;"><span>基本信息</span>
					</span>
					<button id="add" class="btn btn-sm warn-set" style="margin-left:400px;">保存</button>
				    </p>
					<form:input path="entity.id" hidden="hidden" />
					 
					  <ul class="p-ul mt20">
					  <li>
						   <span>
								<label for="entity.type">type</label>
								<input id="entity.type" name="entity.type" class="b-radius5 p-input" required="required" type="text" value="${etlengineManageVO.entity.type }"/>
								<font color="red">*</font>
							</span>
						     <span>
								<label for="entity.title">title</label>
								<input id="entity.title" name="entity.title" class="b-radius5 p-input" required="required" type="text" value="${etlengineManageVO.entity.title }"/>
								<font color="red">*</font>
							</span>
        		 		</li>

						<li>
							<span>
							    <label for="entity.level">层级</label>
							    <form:select path="entity.level">
							       <form:option value="1">1层</form:option>
							       <form:option value="2">2 层</form:option>
							       </form:select>
								  <font  color="red">*</font>
						     </span>
							<span>
								<label for="entity.description">description</label>
								<input id="entity.description" name="entity.description" 
								class="b-radius5 p-input" required="required" type="text" 
								value="${etlengineManageVO.entity.description }"/>
								<font color="red">*</font>
							</span>
							
						</li>	
						<li>
					
						</li>				
					  	<li>

					  		 <span style="width:100%;">
							    <label for="entity.template" style="float: left">模板语句</label>
							    <textarea id="template" name="entity.template" 
							    cssClass="b-radius5 p-input" style="height:400px;width:910px;float: left"required="required" value="">${etlengineManageVO.entity.template }</textarea>
								<font color="red" style="float: left">*</font>
						     </span>
						</li>
					</ul>
			    	</form:form>	
			    </div>
			</div>
		</div>
		
		<div hidden="hidden" class="template">
			<ul class="templateUl">
				
			</ul>
		</div>
	</div>
</body>
</html>