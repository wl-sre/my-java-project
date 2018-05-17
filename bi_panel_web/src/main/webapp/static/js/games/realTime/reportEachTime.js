$(function(){
	
	
	
	$(document).ready(function() {
	    $.reportEachTime.init();
	    $.timeZone.showTimeZone();
	});
	
	var todayArr_install = new Array();
    var todayArr_dau = new Array();
    var todayArr_amount = new Array();
    var todayArr_arpu = new Array();
    var todayArr_arppu = new Array();
    
    var yesterdayArr_install = new Array();
    var yesterdayArr_dau = new Array();
    var yesterdayArr_amount = new Array();
    var yesterdayArr_arpu = new Array();
    var yesterdayArr_arppu = new Array();
    
    var todayCountArr = [0,0,0,0,0,0];
    var yesterdayCountArr = [0,0,0,0,0,0];
    
    var todayArr_source_install = new Array();
    var todayArr_source_dau = new Array();
    var todayArr_source_amount = new Array();
    var todayArr_source_arpu = new Array();
    var todayArr_source_arppu = new Array();
    
    var yesterdayArr_source_install = new Array();
    var yesterdayArr_source_dau = new Array();
    var yesterdayArr_source_amount = new Array();
    var yesterdayArr_source_arpu = new Array();
    var yesterdayArr_source_arppu = new Array();
    
    var todayCountArr_source = [0,0,0,0,0,0];
    var yesterdayCountArr_source = [0,0,0,0,0,0];
    
    var game = null;
    var currency = null;
    var maxDau = null;
    var game_source = null;
    var currency_source = null;
    
	 var today = null;
	 var yesterday = null;
	 var last7Day = null;
	 var lastMonthSToday = null;
	 
	 var today_source = null;
	 var yesterday_source = null;
	 
	 var predictDataArr = new Array();
	 var predictDataArr_source = new Array();
	 
	$.reportEachTime={
		init:function(){
			$.reportEachTime.initEvent();
			$.reportEachTime.queryTablesDataSubmit();
			$.reportEachTime.queryTablesDataSubmit2();
			$.reportEachTime.submit();
			$.reportEachTime.queryData();
			$.reportEachTime.change();
		},
		initEvent:function(){
			$("#query").click(function(){
				$.reportEachTime.submit();
		    });
			
			$(".detail-nav li").each(function(i,n){
				$(n).click(function(){
					$.reportEachTime.redict($(n).attr("id"));
				});
			});
			
			$("input[name=data_radio_in]").change(function(){
//				if($("#chartDiv .dataShap li").first().attr("class") == 'onChoose'){
					$.reportEachTime.brokenLine();
//				}else{
//					$.reportEachTime.brokenChartTable();
//				}
			});
			
			$("input[name=data_radio_source_in]").change(function(){
//				if($("#chartDiv .dataShap li").first().attr("class") == 'onChoose'){
					$.reportEachTime.brokenLine2();
//				}else{
//					$.reportEachTime.brokenChartTable();
//				}
			});
			$("#chartDiv .dataShap li").each(function(i,n){
				$(n).click(function(){
					$("#chartDiv .dataShap li").removeClass("onChoose");
					$(n).addClass("onChoose");
					
					if($("em",n).attr("class") == 'columnar'){
						$("#chartDiv .dataShap ul").css('right','10px');
						$("#chartDiv .dataShap ul").css('top','35px');
						$("#chart").fadeIn();
						$("#chart_data").fadeOut();
						$.reportEachTime.brokenLine();
					}else{
//						$("#chart").fadeOut();
//						$("#chartDiv .dataShap ul").css('right','105px');
//						$("#chartDiv .dataShap ul").css('top','30px');
//						$.reportEachTime.brokenChartTable();
//						$("#chart_data").fadeIn();
					}
				});
			});
			
			$("#chartDiv2 .dataShap li").each(function(i,n){
				$(n).click(function(){
					$("#chartDiv2 .dataShap li").removeClass("onChoose");
					$(n).addClass("onChoose");
					
					if($("em",n).attr("class") == 'columnar'){
						$("#chartDiv2 .dataShap ul").css('right','10px');
						$("#chartDiv2 .dataShap ul").css('top','35px');
						$("#chart2").fadeIn();
						$("#chart_data2").fadeOut();
						$.reportEachTime.brokenLine2();
					}else{
//						$("#chart").fadeOut();
//						$("#chartDiv .dataShap ul").css('right','105px');
//						$("#chartDiv .dataShap ul").css('top','30px');
//						$.reportEachTime.brokenChartTable();
//						$("#chart_data").fadeIn();
					}
				});
			});
			
			$("#indicators").change(function(){
				$.reportEachTime.change();
			});
			
			
			$.reportEachTime.dataTypeChange();
			$.reportEachTime.dateSelectChange();
			
			
			/*$(".detail-info").hide();
			$("#data_source_radio").hide();
			$("#chartDiv2").hide();*/
			
		},
		redict:function(view){
			var snid = $("input[name='snid']").val();
			var gameId = $("input[name='gameId']").val();
			window.location.href='/panel_bi/gameRealTime/toGameRealTime.ac?view='+view+'&snid='+snid+'&gameId='+gameId;
		},
		queryTablesDataSubmit:function(){
			var ps = $.gameUtil.checkParam();
			if(ps == null){
				return;
		    }
			$.ajax({
				  async : false,
				  type: "POST",
				  url: "/panel_bi/gameRealTime/getGameHourRealTime.ac",
				  data: $.param(ps),
				  dataType: "json",
				  success: function(data){
					  game = data.game;
					  currency = data.game.currency;
					// 今天
					 var d =  new Date();
                     today = $.date.getDateFullStr(d);
                     
                     // 昨天
                     d.setDate(d.getDate()-1);
                     yesterday = $.date.getDateFullStr(d);
					  
					 $.reportEachTime.initData();
					 $.each(data.reports,function(i,v){
						 $.reportEachTime.forData(todayArr_equips,todayArr_install,todayArr_dau,todayArr_amount,todayArr_arpu,todayArr_arppu,
								 yesterdayArr_equips,yesterdayArr_install,yesterdayArr_dau,yesterdayArr_amount,yesterdayArr_arpu,yesterdayArr_arppu,v);
	                 });
				  }
			});
			
            $.reportEachTime.brokenLine();
		},
		queryTablesDataSubmit2:function(){
			var ps = $.reportEachTime.checkParam();
			//var ps = $.gameUtil.checkChannelChangeParam();
			if(ps == null){
				return;
		    }
			$.ajax({
				  async : false,
				  type: "POST",
				  url: "/panel_bi/gameRealTime/getGameHourSourceRealTime.ac",
				  data: $.param(ps),
				  dataType: "json",
				  success: function(data){
					  game_source = data.game;
					   maxDau = data.maxDau;
					  currency_source = data.game.currency;
					// 今天
					 var d =  new Date();
                     today_source = $.date.getDateFullStr(d);
                     
                     // 昨天
                     d.setDate(d.getDate()-1);
                     yesterday_source = $.date.getDateFullStr(d);
					  
					 $.reportEachTime.initData2();
					 $.each(data.reports,function(i,v){
						 $.reportEachTime.forData2(todayArr_source_equips,todayArr_source_install,todayArr_source_dau,todayArr_source_amount,todayArr_source_arpu,todayArr_source_arppu,
								 yesterdayArr_source_equips,yesterdayArr_source_install,yesterdayArr_source_dau,yesterdayArr_source_amount,yesterdayArr_source_arpu,yesterdayArr_source_arppu,v);
	                 });
				  }
			});
			
            $.reportEachTime.brokenLine2();
		},
		forData:function(todayArr_equips,todayArr_install,todayArr_dau,todayArr_amount,todayArr_arpu,todayArr_arppu,
				yesterdayArr_equips,yesterdayArr_install,yesterdayArr_dau,yesterdayArr_amount,yesterdayArr_arpu,yesterdayArr_arppu,v){
			if(todayArr_install != null){
	   			var time = parseInt(v.start_time.substring(0,2));
	   			todayArr_equips[time]=(v.equips == null ? 0 : v.equips);
	   			todayArr_install[time]=(v.dnu == null ? 0 : v.dnu);
	      		todayArr_dau[time]=(v.dau == null ? 0 : v.dau);
	      		todayArr_amount[time]=(v.total_amount == null ? 0 : v.total_amount);
	      		if(v.dau == null || v.dau == 0){
	      			todayArr_arpu[time]=0;
	      		}else{
	      			todayArr_arpu[time]=(Math.round((v.total_amount/v.dau)*100)/100);
	      		}
	      		if(v.payer == null || v.payer == 0){
	      			todayArr_arppu[time] = 0;
	      		}else{
	      			todayArr_arppu[time]=(Math.round((v.total_amount/v.payer)*100)/100);
	      		}
	      		
	      		yesterdayArr_equips[time]=(v.yesterday_equips == null ? 0 : v.yesterday_equips);
	      		yesterdayArr_install[time]=(v.yesterday_dnu == null ? 0 : v.yesterday_dnu);
	      		yesterdayArr_dau[time]=(v.yesterday_dau == null ? 0 : v.yesterday_dau);
	      		yesterdayArr_amount[time]=(v.yesterday_total_amount == null ? 0 : v.yesterday_total_amount);
	      		if(v.yesterday_dau == null || v.yesterday_dau == 0){
	      			yesterdayArr_arpu[time]=0;
	      		}else{
	      			yesterdayArr_arpu[time]=(Math.round((v.yesterday_total_amount/v.yesterday_dau)*100)/100);
	      		}
	      		if(v.yesterday_payer == null || v.yesterday_payer == 0){
	      			yesterdayArr_arppu[time] = 0;
	      		}else{
	      			yesterdayArr_arppu[time]=(Math.round((v.yesterday_total_amount/v.yesterday_payer)*100)/100);
	      		}
	   	    }
		},
		forData2:function(todayArr_source_equips,todayArr_source_install,todayArr_source_dau,todayArr_source_amount,todayArr_source_arpu,todayArr_source_arppu,
				yesterdayArr_source_equips,yesterdayArr_source_install,yesterdayArr_source_dau,yesterdayArr_source_amount,yesterdayArr_source_arpu,yesterdayArr_source_arppu,v){
			if(todayArr_source_install != null){
	   			var time_source = parseInt(v.start_time.substring(0,2));
	   			//var time = parseInt(v.start_time.substring(0,2));
	   			todayArr_source_equips[time_source]=(v.equips == null ? 0 : v.equips);
	   			todayArr_source_install[time_source]=(v.dnu == null ? 0 : v.dnu);
	      		todayArr_source_dau[time_source]=(v.dau == null ? 0 : v.dau);
	      		todayArr_source_amount[time_source]=(v.total_amount == null ? 0 : v.total_amount);
	      		if(v.dau == null || v.dau == 0){
	      			todayArr_source_arpu[time_source]=0;
	      		}else{
	      			todayArr_source_arpu[time_source]=(Math.round((v.total_amount/v.dau)*100)/100);
	      		}
	      		if(v.payer == null || v.payer == 0){
	      			todayArr_source_arppu[time_source] = 0;
	      		}else{
	      			todayArr_source_arppu[time_source]=(Math.round((v.total_amount/v.payer)*100)/100);
	      		}
	      		
	      		yesterdayArr_source_equips[time_source]=(v.yesterday_equips == null ? 0 : v.yesterday_equips);
	      		yesterdayArr_source_install[time_source]=(v.yesterday_dnu == null ? 0 : v.yesterday_dnu);
	      		yesterdayArr_source_dau[time_source]=(v.yesterday_dau == null ? 0 : v.yesterday_dau);
	      		yesterdayArr_source_amount[time_source]=(v.yesterday_total_amount == null ? 0 : v.yesterday_total_amount);
	      		if(v.yesterday_dau == null || v.yesterday_dau == 0){
	      			yesterdayArr_source_arpu[time_source]=0;
	      		}else{
	      			yesterdayArr_source_arpu[time_source]=(Math.round((v.yesterday_total_amount/v.yesterday_dau)*100)/100);
	      		}
	      		if(v.yesterday_payer == null || v.yesterday_payer == 0){
	      			yesterdayArr_source_arppu[time_source] = 0;
	      		}else{
	      			yesterdayArr_source_arppu[time_source]=(Math.round((v.yesterday_total_amount/v.yesterday_payer)*100)/100);
	      		}
	   	    }
		},
		submit:function(){
			var ps = $.reportEachTime.checkParam();
			if(ps == null){
				return;
		    }
			
			$.ajax({
				  type: "POST",
				  url: "/panel_bi/gameRealTime/getReportEachTime.ac",
				  data: $.param(ps),
				  dataType: "json",
				  success: function(data){
                     // 今天
					 var d =  new Date();
                     today = $.date.getDateFullStr(d);
                     
                     // 昨天
                     d.setDate(d.getDate()-1);
                     yesterday = $.date.getDateFullStr(d);
                     
//                     $.reportEachTime.initData();
                     game = data.game;
                     currency = data.game.currency;
                     var curHour = data.curHour;
                     
                     //数据排列顺序 今天/昨天/前7天/前30天  0~23小时
                     $.each(data.reports,function(i,v){
                    	 if(v.day == today){
                    		 predictDataArr.push({
                    			 hour:v.times,
                    			 ispredict:v.ispredict
                    		 });
                			 $.reportEachTime.processData(todayCountArr,v);
                    	 }else if(v.day == yesterday){
                    		 $.reportEachTime.processData(yesterdayCountArr,v);
                    	 }
                     });
                     
                     $.reportEachTime.brokenTable();
//                     $.reportEachTime.brokenLine('install');
				  }
				});
		},
		checkParam:function(){
			var type = $("input[name='data_radio_in']").val();
			var type_source = $("input[name='data_radio_source_in']").val();
			var snid = $("input[name='snid']").val();
			var gameId = $("input[name='gameId']").val();
			var source = $("input[name='gameId']").val();
			var source_one = $("#s_c").val();
			if(type == null || type == ''){
				type='install';
			}
			if(type_source == null || type_source == ''){
				type_source='install';
			}
			$("input[name=data_radio_in]:eq(0)").attr("checked",'checked'); 
			$("input[name=data_radio_source_in]:eq(0)").attr("checked",'checked'); 
			var ps = {
					type:type,
					snid:snid,
					gameId:gameId,
					type_source:type_source,
					source_one:source_one
			};
			return ps;
		},
		initData:function(){
			todayArr_install = new Array();
			todayArr_equips = new Array();
		    todayArr_dau = new Array();
		    todayArr_amount = new Array();
		    todayArr_arpu = new Array();
		    todayArr_arppu = new Array();
		    
		    yesterdayArr_install = new Array();
		    yesterdayArr_equips = new Array();
		    yesterdayArr_dau = new Array();
		    yesterdayArr_amount = new Array();
		    yesterdayArr_arpu = new Array();
		    yesterdayArr_arppu = new Array();
		    
		    predictDataArr = new Array();
		    
			for(var i=0;i<24;i++){
				todayArr_install.push('-');
				todayArr_equips.push('-');
				todayArr_dau.push('-');
				todayArr_amount.push('-');
				todayArr_arpu.push('-');
				todayArr_arppu.push('-');
				
				yesterdayArr_install.push('-');
				yesterdayArr_equips.push('-');
			    yesterdayArr_dau.push('-');
			    yesterdayArr_amount.push('-');
			    yesterdayArr_arpu.push('-');
			    yesterdayArr_arppu.push('-');
			}
		    
		    todayCountArr = [0,0,0,0,0,0,0];
		    yesterdayCountArr = [0,0,0,0,0,0,0];
		},
		initData2:function(){
			todayArr_source_install = new Array();
			todayArr_source_equips = new Array();
		    todayArr_source_dau = new Array();
		    todayArr_source_amount = new Array();
		    todayArr_source_arpu = new Array();
		    todayArr_source_arppu = new Array();
		    
		    yesterdayArr_source_install = new Array();
		    yesterdayArr_source_equips = new Array();
		    yesterdayArr_source_dau = new Array();
		    yesterdayArr_source_amount = new Array();
		    yesterdayArr_source_arpu = new Array();
		    yesterdayArr_source_arppu = new Array();
		    
		    predictDataArr_source = new Array();
		    
			for(var i=0;i<24;i++){
				todayArr_source_install.push('-');
				todayArr_source_equips.push('-');
				todayArr_source_dau.push('-');
				todayArr_source_amount.push('-');
				todayArr_source_arpu.push('-');
				todayArr_source_arppu.push('-');
				
				yesterdayArr_source_install.push('-');
				yesterdayArr_source_equips.push('-');
			    yesterdayArr_source_dau.push('-');
			    yesterdayArr_source_amount.push('-');
			    yesterdayArr_source_arpu.push('-');
			    yesterdayArr_source_arppu.push('-');
			}
		    
		    todayCountArr_source = [0,0,0,0,0,0,0];
		    yesterdayCountArr_source = [0,0,0,0,0,0,0];
		},
		processData:function(todayCountArr,v){
			if(todayCountArr != null){
				todayCountArr[0] += v.dnu;
				todayCountArr[1] += v.equips;
		   		todayCountArr[2] += v.dau_total;
		   		todayCountArr[3] += v.total_amount;
		   		todayCountArr[4] += v.total_amount/v.dau_total;//废弃
		   		todayCountArr[5] += v.total_amount/v.payer;//废弃
		   		todayCountArr[6] += v.payer_total;
			}
		},
		brokenTable:function(){
			$("#dt").remove();
			
			var table = $(".template_cache .dataTable_temp").clone(true);
			table.attr("id", "dt");
			table.attr("class", "table data_table");
			
			var tr = $(".first", table);
			$.reportEachTime.writeTr(tr,todayCountArr,true);
			
			tr = $(".second", table);
			$.reportEachTime.writeTr(tr,yesterdayCountArr,false);
			
			$("#data").append(table);
		},
		writeTr:function(tr,todayCountArr,isToday){
			for(var i=0;i<=5;i++){
				var td = isToday ? $(".bold", tr).first().clone(true) : $("td", tr).first().clone(true);
				if(i<4){
					$(td).text(Math.round(todayCountArr[i]*100)/100);
				}else if(i == 4){
					if(todayCountArr[2] == 0){
						$(td).text(0);
					}else{
						$(td).text(Math.round(todayCountArr[3]/todayCountArr[2]*100)/100);
					}
				}else if(i == 5){
					if(todayCountArr[6] == 0){
						$(td).text(0);
					}else{
						$(td).text(Math.round(todayCountArr[3]/todayCountArr[6]*100)/100);
					}
				}
				
				tr.append(td);
			}
			$("td",tr).eq(1).remove();
		},
		brokenLine:function(){
			require.config({
				paths: {
					echarts: '/js/echarts/dist/',
					theme:'/js/echarts/theme/'
				}
			});
			require(
				[
					'echarts',
					'echarts/chart/bar',  
					'echarts/chart/line',
					'echarts/chart/scatter',
					'echarts/chart/k',
					'echarts/chart/pie',
					'echarts/chart/radar',
					'echarts/chart/force',
					'echarts/chart/chord',
					'echarts/chart/gauge',
					'echarts/chart/funnel',
					'echarts/chart/eventRiver'
				],
				DrawEChart
			);
			
			
			function DrawEChart(ec){
				require(['theme/macarons'], function(tarTheme){
					var myChart = ec.init($("#chart")[0],tarTheme);
					
					var option = {
							title:{
								text:$('input:radio[name="data_radio_in"]:checked').attr("text")+'-趋势对比图',
								subtext:'',
								textStyle: {
									fontSize: 14
									}},
						    legend: {
						        data:['今日','昨日']
						    },
						    toolbox: {
						        show : true,orient:'vertical',x:'right',y:'center',
						        feature : {
						            mark : {show: true},
						            magicType : {show: true, type: ['line', 'bar']},
						            restore : {show: true},
						            saveAsImage : {show: true}
						        }
						    },
						    calculable : false
					};
					
					var xAxis_data = new Array();
					for(var i=0;i<24;i++){
						xAxis_data.push(i);
					}
					option.xAxis = [{data:xAxis_data,type:'category',
						        	splitLine:{
				            	         show:true,
				            	         lineStyle:{
				            		         type:"dashed"
				            	           }
							        }}];
					option.yAxis = [
					        {type:'value',
					        	splitLine:{
			            	         show:true,
			            	         lineStyle:{
			            		         type:"dashed"
			            	           }
						        }}
		            ];
					
					var ret = $.reportEachTime.processTableOrChartData();
					var arr_today = ret.arr_today;
					
					var ispredict = 0;
					var arr = new Array();
					var starIndex = 0;
					var tooltip = new Array();
					option.series = new Array();
					for(var i=0;i<24;i++){
							arr.push(arr_today[i]);
							if(i != 0){
								// 数据段前后数据补'-'占位
								var first = new Array();
								for(var k=0;k<starIndex-1;k++){
									first.push('-');
								}
								if(starIndex > 0){
									first.push(arr_today[starIndex-1]);
								}
								
								arr = $.merge(first,arr);
								for(var j=i+1;j<24;j++){
									arr.push('-');
								}
								if(ispredict == 0){
									option.series.push({
										name:'今日',
										type:'line',
										smooth:true,
										data:arr
									});
								}
								arr = new Array();
								tooltip.push({
									index:option.series.length - 1,
									start:starIndex-1,
									end:i,
									ispredict:ispredict
								});
								starIndex = i+1;
							}
					}
					
					option.series.push({
						name:'昨日',
						type:'line',
						data:ret.arr_yes
					});
					option.title.subtext = ret.subtext;
					option.tooltip = {
					        trigger: 'axis',
					        formatter:function(params){
					        	var str = '';
					        	$.each(tooltip,function(i,v){
					        		var hour = Number(params[0].name);
					        		if(hour >= v.start && hour <= v.end){
				        				str = params[v.index].seriesName + ' : ' + params[v.index].value + '<br/>';
					        			if(option.series.length - 1 >=0 && params[option.series.length - 1] != undefined
					        					&& params[option.series.length - 1].seriesName == '昨日'){
				        					str += params[option.series.length - 1].seriesName + ' : ' + params[option.series.length - 1].value + '<br/>';
				        				}
					        		}
					        	});
					        	return str;
					        }
					    };
					
					myChart.setOption(option,true);
		        });
			}
		},
		brokenLine2:function(){
			require.config({
				paths: {
					echarts: '/js/echarts/dist/',
					theme:'/js/echarts/theme/'
				}
			});
			require(
				[
					'echarts',
					'echarts/chart/bar',  
					'echarts/chart/line',
					'echarts/chart/scatter',
					'echarts/chart/k',
					'echarts/chart/pie',
					'echarts/chart/radar',
					'echarts/chart/force',
					'echarts/chart/chord',
					'echarts/chart/gauge',
					'echarts/chart/funnel',
					'echarts/chart/eventRiver'
				],
				DrawEChart
			);
			
			
			function DrawEChart(ec){
				require(['theme/macarons'], function(tarTheme){
					var myChart = ec.init($("#chart2")[0],tarTheme);
					
					var option_source = {
							title:{
								text:$('input:radio[name="data_radio_source_in"]:checked').attr("text")+'-趋势对比图',
								subtext:'',
								textStyle: {
									fontSize: 14
									}},
						    legend: {
						        data:['今日','昨日']
						    },
						    toolbox: {
						        show : true,orient:'vertical',x:'right',y:'center',
						        feature : {
						            mark : {show: true},
						            magicType : {show: true, type: ['line', 'bar']},
						            restore : {show: true},
						            saveAsImage : {show: true}
						        }
						    },
						    calculable : false
					};
					
					var xAxis_data_source = new Array();
					for(var i=0;i<24;i++){
						xAxis_data_source.push(i);
					}
					option_source.xAxis = [{data:xAxis_data_source,type:'category',
						        	splitLine:{
				            	         show:true,
				            	         lineStyle:{
				            		         type:"dashed"
				            	           }
							        }}];
					option_source.yAxis = [
					        {type:'value',
					        	splitLine:{
			            	         show:true,
			            	         lineStyle:{
			            		         type:"dashed"
			            	           }
						        }}
		            ];
					
					var ret = $.reportEachTime.processTableOrChartData2();
					var arr_today_source = ret.arr_today_source;
					
					var ispredict = 0;
					var arr_source = new Array();
					var starIndex_source = 0;
					//var starIndex = 0;
					var tooltip = new Array();
					option_source.series = new Array();
					for(var i=0;i<24;i++){
							arr_source.push(arr_today_source[i]);
							if(i != 0){
								// 数据段前后数据补'-'占位
								var first_source = new Array();
								for(var k=0;k<starIndex_source-1;k++){
									first_source.push('-');
								}
								if(starIndex_source > 0){
									first_source.push(arr_today_source[starIndex_source-1]);
								}
								
								arr_source = $.merge(first_source,arr_source);
								for(var j=i+1;j<24;j++){
									arr_source.push('-');
								}
								if(ispredict == 0){
									option_source.series.push({
										name:'今日',
										type:'line',
										smooth:true,
										data:arr_source
									});
								}
								arr_source = new Array();
								tooltip.push({
									index:option_source.series.length - 1,
									start:starIndex_source-1,
									end:i,
									ispredict:ispredict
								});
								starIndex_source = i+1;
							}
					}
					
					option_source.series.push({
						name:'昨日',
						type:'line',
						data:ret.arr_yes_source
					});
					option_source.title.subtext = ret.subtext_source;
					option_source.tooltip = {
					        trigger: 'axis',
					        formatter:function(params){
					        	var str_source = '';
					        	$.each(tooltip,function(i,v){
					        		var hour_source = Number(params[0].name);
					        		if(hour_source >= v.start && hour_source <= v.end){
				        				str_source = params[v.index].seriesName + ' : ' + params[v.index].value + '<br/>';
					        			if(option_source.series.length - 1 >=0 && params[option_source.series.length - 1] != undefined
					        					&& params[option_source.series.length - 1].seriesName == '昨日'){
				        					str_source += params[option_source.series.length - 1].seriesName + ' : ' + params[option_source.series.length - 1].value + '<br/>';
				        				}
					        		}
					        	});
					        	return str_source;
					        }
					    };
					
					myChart.setOption(option_source,true);
		        });
			}
		},
		processTableOrChartData:function(){
			var type = $('input:radio[name="data_radio_in"]:checked').val();
			var arr_today = new Array();
			var arr_yes = new Array();
			var subtext = '单位（个）';
			if('install' == type){
				arr_today = todayArr_install;
				arr_yes = yesterdayArr_install;
				subtext = '单位（个）';
			}else if('equips' == type){
				arr_today = todayArr_equips;
				arr_yes = yesterdayArr_equips;
				subtext = '单位（个）';
			}else if('dau' == type){
				arr_today = todayArr_dau;
				arr_yes = yesterdayArr_dau;
				subtext = '单位（个）';
			}else if('payment' == type){
				arr_today = todayArr_amount;
				arr_yes = yesterdayArr_amount;
				subtext = '单位（'+currency+'）';
			}else if('arpu' == type){
				arr_today = todayArr_arpu;
				arr_yes = yesterdayArr_arpu;
				subtext = '单位（'+currency+'）';
			}else if('arppu' == type){
				arr_today = todayArr_arppu;
				arr_yes = yesterdayArr_arppu;
				subtext = '单位（'+currency+'）';
			}
			var ret = {
					arr_today:arr_today,
					arr_yes:arr_yes,
					subtext:subtext
			};
			
			return ret;
		},
		processTableOrChartData2:function(){
			var type = $('input:radio[name="data_radio_source_in"]:checked').val();
			var arr_today_source = new Array();
			var arr_yes_source = new Array();
			var subtext_source = '单位（个）';
			if('install' == type){
				arr_today_source = todayArr_source_install;
				arr_yes_source = yesterdayArr_source_install;
				subtext_source = '单位（个）';
			}else if('equips' == type){
				arr_today_source = todayArr_source_equips;
				arr_yes_source = yesterdayArr_source_equips;
				subtext_source = '单位（个）';
			}else if('dau' == type){
				arr_today_source = todayArr_source_dau;
				arr_yes_source = yesterdayArr_source_dau;
				subtext_source = '单位（个）';
			}else if('payment' == type){
				arr_today_source = todayArr_source_amount;
				arr_yes_source = yesterdayArr_source_amount;
				subtext_source = '单位（'+currency+'）';
			}else if('arpu' == type){
				arr_today_source = todayArr_source_arpu;
				arr_yes_source = yesterdayArr_source_arpu;
				subtext_source = '单位（'+currency+'）';
			}else if('arppu' == type){
				arr_today_source = todayArr_source_arppu;
				arr_yes_source = yesterdayArr_source_arppu;
				subtext_source = '单位（'+currency+'）';
			}
			var ret = {
					arr_today_source:arr_today_source,
					arr_yes_source:arr_yes_source,
					subtext_source:subtext_source
			};
			
			return ret;
		},
		//总览
		queryData:function(){
			var ps = $.gameUtil.checkParam();
			if(ps == null){
				return;
		    }
			
			$.ajax({
				  type: "POST",
				  url: "/panel_bi/wap/gameView/getGameTotalRealTime.ac",
				  data: $.param(ps),
				  dataType: "json",
				  success: function(data){
					    game = data.game;
					    
					    $.reportEachTime.brokenTable_noClient(data.noClientResultList);
				    	isCanTotalSubmit = true;
				    	isCanHourSubmit = true;
				    	$("[css='tabs']").removeClass("ui-selected");
				    	$("[css='tabs']").first().addClass("ui-selected");
				    	$("#tabs-2").show();
				  }
				});
		},
		brokenTable_noClient:function(reports){
			$('#dt2_wrapper').remove();
			var pay_count = 0,install_count = 0,dau_count=0,pu_count = 0,arpu = 0,arppu = 0;
			
			var table2 = $(".template_cache .table_2").clone(true);
			table2.attr("id","dt2");
			
			$.each(reports,function(i,v){
				
				
				var trTemp = $("tbody tr",table2).first().clone(true);
				
				$("td.date",trTemp).text(v.start_time.substring(0,5)+"~"+v.finish_time.substring(0,5));
				$("td.install",trTemp).text(v.dnu == null ? 0 : v.dnu);
				$("td.yesterday_install",trTemp).text(v.yesterday_dnu == null ? 0 : v.yesterday_dnu);
				$("td.equips",trTemp).text(v.equips == null ? 0 : v.equips);
				$("td.yesterday_equips",trTemp).text(v.yesterday_equips == null ? 0 : v.yesterday_equips);
				$("td.dau",trTemp).text(v.dau_total);
				$("td.yesterday_dau",trTemp).text(v.yesterday_dau_total == null ? 0 : v.yesterday_dau_total);
				$("td.pay",trTemp).text(v.total_amount == null ? 0 : v.total_amount);
				$("td.yesterday_pay",trTemp).text(v.yesterday_total_amount == null ? 0 : v.yesterday_total_amount);
//				$("td.pu",trTemp).text(v.payer);
				$("td.arpu",trTemp).text(v.total_amount == null ? 0 :Math.round(v.total_amount/v.dau_total*100)/100);
				$("td.yesterday_arpu",trTemp).text(v.yesterday_total_amount == null ? 0 :Math.round(v.yesterday_total_amount/v.yesterday_dau_total*100)/100);
				
				$("td.arppu",trTemp).text(v.total_amount == null ? 0 : Math.round(v.total_amount/v.payer*100)/100);
				$("td.yesterday_arppu",trTemp).text(v.yesterday_total_amount == null ? 0 : Math.round(v.yesterday_total_amount/v.yesterday_payer*100)/100);
				
				
				$('tbody',table2).append(trTemp); 
			});
			
			arpu = dau_count == 0 ? 0 : Math.round(pay_count/dau_count*100)/100;
			arppu = pu_count == 0 ? 0 : Math.round(pay_count/pu_count*100)/100;
			
			table2.removeClass("table_2").addClass("display");
			$("tbody tr",table2).first().remove();
			
			$('#data2').append(table2); 
			
			$.biDataTables.dataTables2($('#dt2'));
			
			$('#dt2 tbody tr').click(function (){
		        $(this).toggleClass('highlight');
		    });
			
		//	return [install_count,dau_count,pay_count,arpu,arppu];
		},
		//按小时
		hour_DataSubmit:function(){
			var ps = $.gameUtil.checkParam();
			if(ps == null){
				return;
		    }
			$.ajax({
				  type: "POST",
				  url: "/panel_bi/gameRealTime/getGameHourRealTime.ac",
				  data: $.param(ps),
				  dataType: "json",
				  success: function(data){
					  $.reportEachTime.brokenTable_HourData(data.reports);
				  }
				});
		},
		brokenTable_HourData:function(reports){
			$('#dt7_wrapper').remove();
			
			var table7 = $(".template_cache .table_7").clone(true);
			table7.attr("id","dt7");
			
			$.each(reports,function(i,v){
				var trTemp = $("tbody tr",table7).first().clone(true);
				$("td.date",trTemp).text(v.start_time.substring(0,5)+"~"+v.finish_time.substring(0,5));
				$("td.install",trTemp).text(v.dnu == null ? 0 : v.dnu);
				$("td.yesterday_install",trTemp).text(v.yesterday_dnu == null ? 0 : v.yesterday_dnu);
				$("td.equips",trTemp).text(v.equips == null ? 0 : v.equips);
				$("td.yesterday_equips",trTemp).text(v.yesterday_equips == null ? 0 : v.yesterday_equips);
				$("td.dau",trTemp).text(v.dau == null ? 0 : v.dau);
				$("td.yesterday_dau",trTemp).text(v.yesterday_dau == null ? 0 : v.yesterday_dau);
				$("td.pay",trTemp).text(v.total_amount == null ? 0 : v.total_amount);
				$("td.yesterday_pay",trTemp).text(v.yesterday_total_amount == null ? 0 : v.yesterday_total_amount);
				$("td.arpu",trTemp).text((v.total_amount == null || v.total_amount == 0) ? 0 :Math.round(v.total_amount/v.dau*100)/100);
				$("td.yesterday_arpu",trTemp).text(v.yesterday_total_amount == null ? 0 : Math.round(v.yesterday_total_amount/v.yesterday_dau*100)/100);
				
				$("td.arppu",trTemp).text((v.total_amount == null || v.total_amount == 0) ? 0 : Math.round(v.total_amount/v.payer*100)/100);
				$("td.yesterday_arppu",trTemp).text((v.yesterday_total_amount == null || v.yesterday_total_amount == 0) ? 0 : Math.round(v.yesterday_total_amount/v.yesterday_payer*100)/100);
				
				
				$('tbody',table7).append(trTemp); 
			});
			table7.removeClass("table_7").addClass("display");
			$("tbody tr",table7).first().remove();
			
			$('#data7').append(table7); 
			
			$.biDataTables.dataTables2($('#dt7'));
			
			$('#dt7 tbody tr').click(function (){
		        $(this).toggleClass('highlight');
		    });
			
		},
		
		//总览汇总
		total_DataSubmit:function(){
			var ps = $.gameUtil.checkParam();
			if(ps == null){
				return;
		    }
			$.ajax({
				  type: "POST",
				  url: "/panel_bi/gameRealTime/getGameTotalRealTime.ac",
				  data: $.param(ps),
				  dataType: "json",
				  success: function(data){
					  $.reportEachTime.brokenTable_TotalData(data.reports);
				  }
				});
		},
		brokenTable_TotalData:function(reports){
			$('#dt6_wrapper').remove();
			
			var table6 = $(".template_cache .table_6").clone(true);
			table6.attr("id","dt6");
			
			$.each(reports,function(i,v){
				var trTemp = $("tbody tr",table6).first().clone(true);
				$("td.date",trTemp).text(v.start_time.substring(0,5)+"~"+v.finish_time.substring(0,5));
				$("td.install",trTemp).text(v.dnu == null ? 0 : v.dnu);
				$("td.yesterday_install",trTemp).text(v.yesterday_dnu == null ? 0 : v.yesterday_dnu);
				$("td.equips",trTemp).text(v.equips == null ? 0 : v.equips);
				$("td.yesterday_equips",trTemp).text(v.yesterday_equips == null ? 0 : v.yesterday_equips);
				$("td.dau",trTemp).text(v.dau == null ? 0 : v.dau);
				$("td.yesterday_dau",trTemp).text(v.yesterday_dau == null ? 0 :v.yesterday_dau);
				$("td.pay",trTemp).text(v.total_amount == null ? 0 : v.total_amount);
				$("td.yesterday_pay",trTemp).text(v.yesterday_total_amount == null ? 0 : v.yesterday_total_amount);
				$("td.arpu",trTemp).text((v.total_amount == null || v.total_amount == 0) ? 0 : Math.round(v.total_amount/v.dau*100)/100);
				$("td.yesterday_arpu",trTemp).text((v.yesterday_total_amount == null || v.yesterday_total_amount == 0) ? 0 : Math.round(v.yesterday_total_amount/v.yesterday_dau*100)/100);
				
				$("td.arppu",trTemp).text((v.total_amount == null || v.total_amount == 0) ? 0 : Math.round(v.total_amount/v.payer*100)/100);
				$("td.yesterday_arppu",trTemp).text((v.yesterday_total_amount == null || v.yesterday_total_amount == 0) ? 0 : Math.round(v.yesterday_total_amount/v.yesterday_payer*100)/100);
				$('tbody',table6).append(trTemp); 
			});
			table6.removeClass("table_6").addClass("display");
			$("tbody tr",table6).first().remove();
			
			$('#data6').append(table6); 
			
			$.biDataTables.dataTables2($('#dt6'));
			
			$('#dt6 tbody tr').click(function (){
		        $(this).toggleClass('highlight');
		    });
			
		},
		dateSelectChange:function(){
			var dateArr = $.date.getBeforeTodayDate(0);
			$("#beginTime_rt").text(dateArr[0]);
			$("#endTime_rt").text(dateArr[1]);
			$("input[name='beginTime']").val(dateArr[0]);
			$("input[name='endTime']").val(dateArr[1]);
			$(".template_cache").hide();
			
			$($("[css='dateSelect']")).each(function(i,n){
				$(n).click(function(){
					$("[css='dateSelect']").removeClass("ui-selected");
					$("[css='tabs']").removeClass("ui-selected");
					$(n).addClass("ui-selected");
					
					var dateArr = $.date.getBeforeTodayDate(Number($(n).attr("val")));
					$("#beginTime_rt").text(dateArr[0]);
					$("#endTime_rt").text(dateArr[1]);
					
					
					$("input[name='beginTime']").val(dateArr[0]);
					$("input[name='endTime']").val(dateArr[1]);

					$("[css='tabs']").removeClass("ui-selected");
					$("[css='tabs']").first().addClass("ui-selected");
					
					$('#dt2_wrapper').remove(); 
					$('#dt6_wrapper').remove(); 
					$('#dt7_wrapper').remove(); 
					$('#dt3_wrapper').remove(); 
					$('#dt1_wrapper').remove(); 
					$("#tabs-6").hide();
					$("#tabs-2").show();
					$("#tabs-7").hide();
					$("#tabs-3").hide();
					$("#tabs-1").hide();
					$.reportEachTime.queryData();
				});
			});
		},
		dataTypeChange:function(){
			$("#tabs-2").hide();
			$("#tabs-6").hide();
			$("#tabs-7").hide();
			$("#tabs-3").hide();
			$("#tabs-1").hide();
			$($("[css='tabs']")).each(function(i,n){
				$(n).click(function(){
					$("[css='tabs']").removeClass("ui-selected");
					$(n).addClass("ui-selected");
					
					
					$("#tabs-2").hide();
					$("#tabs-6").hide();
					$("#tabs-7").hide();
					$("#tabs-3").hide();
					$("#tabs-1").hide();
					if($(n).attr("val")=='tabs-6'){
						if(isCanTotalSubmit){
							$.reportEachTime.total_DataSubmit();
						}
						$("#tabs-6").show();
					}else if($(n).attr("val")=='tabs-7'){
						if(isCanHourSubmit){
							$.reportEachTime.hour_DataSubmit();
						}
						$("#tabs-7").show();
						
					}else if($(n).attr("val")=='tabs-2'){
						var dateArr = $.date.getBeforeTodayDate(0);
						$("#beginTime_rt").text(dateArr[0]);
						$("#endTime_rt").text(dateArr[1]);
						$("input[name='beginTime']").val(dateArr[0]);
						$("input[name='endTime']").val(dateArr[1]);
						$(".template_cache").hide();
						
						var dateArr = $.date.getBeforeTodayDate(Number($(n).attr("val")));
						$("#beginTime_rt").text(dateArr[0]);
						$("#endTime_rt").text(dateArr[1]);
						
						$('#dt2_wrapper').remove(); 
						$('#dt6_wrapper').remove(); 
						$('#dt7_wrapper').remove(); 
						$('#dt3_wrapper').remove(); 
						$('#dt1_wrapper').remove(); 
						$.reportEachTime.queryData();
						
						$("input[name='beginTime']").val(dateArr[0]);
						$("input[name='endTime']").val(dateArr[1]);
						
						
						$("#tabs-2").show();
					}else if($(n).attr("val")=='tabs-3'){
						//if(isCanSourceSubmit){
						//$.reportEachTime.queryTablesDataSubmit2();
						/*$(".detail-info").show();
						$("#data_source_radio").show();
						$("#chartDiv2").show();*/
						$.reportEachTime.sourceDataSubmit();
						
					
						
					//}
						$("#tabs-3").show();
					}else if($(n).attr("val")=='tabs-1'){
						//if(isCanClientSubmit){
						$.reportEachTime.clientDataSubmit();
					//}
					
						$("#tabs-1").show();
					}else{
						$("#tabs-2").show();
					}
				});
			});
		},
		brokenChartTable:function(){
            $("#dt_chart_wrapper").remove();
            
            var type = $('input:radio[name="data_radio_in"]:checked').attr("text");
			
			var table = $(".template_cache .chart_table_templete").clone(true);
			table.attr("id", "dt_chart");
			table.removeClass();
			$("#chart_data").append(table);
			
			var tr = $("thead tr", table);
			var td = $("td", tr).first();
			$(td).text("小时/"+type);
			
			var ret = $.reportEachTime.processTableOrChartData();
			
			for(var i=0;i<ret.arr_today.length;i++){
				if(ret.arr_today[i]=='-'){
					continue;
				}
				var tr = $("tbody tr", table).first().clone(true);
				$("td", tr).eq(0).text(i);
				$("td", tr).eq(1).text(ret.arr_today[i]);
				$("td", tr).eq(2).text(ret.arr_yes[i]);
				table.append(tr);
			}
			$("tbody tr", table).eq(0).remove();
			
			$('#dt_chart').DataTable({
				"sPaginationType" : "simple_numbers",
	            "dom" : 'T<"clear"><"top">rt<"bottom"ip><"clear">' ,
	            "bDestroy" : true,
	            "bPaginate" : true,
	            "bInfo" : true,
				"bSort" : true,
				"bScrollCollapse" : true,
				"bScrollInfinite" : true,
			    "bFilter" : false,
			    "paging": true, //翻页功能
			    "lengthChange": false, //改变每页显示数据数量
			    "searching": false, //过滤功能
			    "ordering": true, //排序功能
			    "order": [[ 0, "asc" ]],
			    "processing": true,
	            "lengthMenu": [ 10, 20, 50, 100],
			    "pageLength": 5,
			    "language":{
			    	"url": "/dataTables/chinese_chart.json"
			    },
			    tableTools: {
			    	"sSwfPath": "/dataTables/extensions/TableTools/swf/copy_csv_xls.swf",
	                                         "aButtons":[ 
	                            ]
		        }
				}
			  );
			
			$('#dt_chart tbody tr').click(function (){
		           $(this).toggleClass('highlight');
		       });
				
			 table.removeClass().addClass('table table-striped');
				
		},
		sourceDataSubmit:function(){
			
			
			var ps = $.gameUtil.checkParam();
			if(ps == null){
				return;
		    }
			$('#dt3_wrapper').remove();
			var selected = [];
			var rate = $("#gameRate").val();
			
			var table= $(".template_cache .table_3").clone(true);
			table.attr("id", "dt3");
			$('#data3').append(table);
			
			$('#dt3').dataTable( {
		        "processing": true,
		        "serverSide": true,
		        ajax: {
				          data: function(d){
				        	  d.id=ps.id;
				        	  d.gameId=ps.gameId;
				        	  d.snid=ps.snid;
				        	  d.indicators=ps.indicators;
				        	  d.queryType=ps.queryType;
				        	  d.beginTime=ps.beginTime;
				        	  d.endTime=ps.endTime;
				        	  d.source=ps.source;
				        	  d.clientid=ps.clientid;
				        	  d.view=ps.view;
				          },
						  type: "POST",
						  url:"/panel_bi/gameRealTime/getGameRealTimeHourSourceView.ac"
		              },
		        "pageLength": 10,
			    "language":{
			    	"url": "/dataTables/chinese.json"
			    },
		        "deferRender": true,
			    "sPaginationType" : "full_numbers",
		        "dom" : '<"clear"><"top">frt<"bottom"ip><"clear">' ,
				"bSort" : false,
			    "bFilter" : true,
			    "paging": true, //缈婚〉鍔熻兘
			    "lengthChange": false,//鏀瑰彉姣忛〉鏄剧ず鏁版嵁鏁伴噺
			    "searching": false, //杩囨护鍔熻兘
			    "ordering": true, //鎺掑簭鍔熻兘
			    "order": [[ 0, "asc" ]],
			    rowCallback:function(row, data) {
                    if ( $.inArray(data.DT_RowId, selected) !== -1 ) {
                        $(row).addClass('highlight');
                    }
                },
                columns: [
							{"data": null},
							{"data": "install_creative"},
							{"data": "dnu"},
							//{"data": "yesterday_dnu"},
							{"data": null},
							{"data": "equips"},
							{"data": null},
							{"data": "dau"},
							{"data": null},
							{"data": "total_amount"},
							{"data": null},
//							{"data": "payer"},
//							{"data": "yesterday_payer"},
							{"data": null},
							{"data": null},
							{"data": null},
							{"data": null}
	                  ],
            columnDefs: [
						{
						    targets: 0,
						    render: function (a, b, v, d) {
						         return v.start_time.substring(0,5)+ '~' +v.finish_time.substring(0,5);
						      }
						   },
						{
						    targets: 3,
						    render: function (a, b, v, d) {
						         return v.yesterday_dnu == '' || v.yesterday_dnu==null ||v.yesterday_dnu == 0.0 ? 0 :v.yesterday_dnu;
						      }
						   },
						   {
							    targets: 5,
							    render: function (a, b, v, d) {
							         return v.yesterday_equips == '' || v.yesterday_equips==null ||v.yesterday_equips == 0.0 ? 0 :v.yesterday_equips;
							      }
							},
						   {
							    targets: 7,
							    render: function (a, b, v, d) {
							         return v.yesterday_dau == '' || v.yesterday_dau==null ||v.yesterday_dau == 0.0 ? 0 :v.yesterday_dau;
							      }
						   },
						 {
						    targets: 9,
						    render: function (a, b, v, d) {
						         return v.yesterday_total_amount == '' || v.yesterday_total_amount==null ||v.yesterday_total_amount == 0.0 ? 0 :v.yesterday_total_amount;
						      }
						 },
					   
	                      {
	                        targets: 10,
	                        render: function (a, b, v, d) {
	                             return v.dau == '' || v.dau == null || v.dau == 0 ? 0 : Math.round((v.total_amount/v.dau)*100)/100;
	                          }
	                       },
	                       {
		                        targets: 11,
		                        render: function (a, b, v, d) {
		                             return  v.yesterday_dau == '' || v.yesterday_dau == null || v.yesterday_dau == 0  ? 0 : Math.round((v.yesterday_total_amount/v.yesterday_dau)*100)/100;
		                          } 
		                    },
		                    {
		                        targets: 12,
		                        render: function (a, b, v, d) {
		                             return v.payer == '' || v.payer == null || v.payer == 0  ? 0 :  Math.round((v.total_amount/v.payer)*100)/100;
		                          }
		                    },
	                       {
		                        targets: 13,
		                        render: function (a, b, v, d) {
		                             return v.yesterday_payer == '' || v.yesterday_payer == null || v.yesterday_payer == 0  ? 0 :  Math.round((v.yesterday_total_amount/v.yesterday_payer)*100)/100;
		                          } 
		                    }
	                     ]
		    });

			$('#dt3').removeClass().addClass('table table-striped');
				    
		    $('#dt3 button').click(function (){
		        $("#downType").val("source");
		        $(this).attr('disabled',true);
		        $(this).val(2);
		        window.btnTimeout=setInterval(function(){
					$.gameUtil.btnTimeout($('#dt3 button'),window.btnTimeout);
				  },1000);
		        $("#downLoadForm").submit();
		    });
			
			$.gameUtil.trHighLight($('#dt3 tbody'),selected);
			$("[css='dateSelect']").removeClass("ui-selected");
			isCanSourceSubmit = false;
		},
		change:function(){
			var ps = $.gameUtil.checkChannelChangeParam();
		    if(ps == null){
			  return;
	        }
			var value = $("#indicators").val();
			if(value == 'source'){
				$("#s_c").remove();
				$.ajax({
					  type: "POST",
					  url: "/panel_bi/gameRealTime/getGameSource.ac",
					  data: $.param(ps),
					  dataType: "json",
					  success: function(data){
					    var span = $("#s_c_span");
					    var str = '<select id="s_c" style="max-width:240px"><option value="-1">请选择...</option>';
					   // var str = '';
					    $.each(data.gameSources,function(i,v){
					    	
					    	v = v.toUpperCase();
					    	if(v == maxDau){
					    		str += '<option value="' + v +'" selected="selected">' + v +'</option>';
					    	}else{
					    		str += '<option value="' + v +'">' + v +'</option>';
					    	}
					    });
					    
						
					    str += '</select>';
					    span.append(str);
					    
					   $("#s_c").change(function(){
						   $.reportEachTime.queryTablesDataSubmit2();
						   $.reportEachTime.sourceDataSubmit();
						});
					  }
				});
			}else if(value == 'client'){
				
				$("#s_c").remove();
				$.ajax({
					  type: "POST",
					  url: "/panel_bi/game/getGameClient.ac",
					  data: $.param(ps),
					  dataType: "json",
					  success: function(data){
					    var span = $("#s_c_span");
					    var str = '<select id="s_c" style="max-width:240px"><option value="-1">请选择...</option><option value="-99">所有服务器</option>';
					    $.each(data.gameClients,function(i,v){
					    	str += '<option value="' + v +'">' + v +'</option>';
					    });
					    str += '</select>';
					    span.append(str);
					    
					  $("#s_c").change(function(){
					    	$.reportEachTime.clientDataSubmit();
						});
					  }
				});
				$.reportEachTime.clientDataSubmit();
				$("#tabs-1").show();
				$("#tabs-3").hide();
			}
		},
		
		clientDataSubmit:function(){
			$("[css='dateSelect']").removeClass("ui-selected");
			var ps = $.gameUtil.checkParam();
			
			if(ps == null){
				return;
		    }
			var selected = [];
			$('#dt1_wrapper').remove();
			
			var rate = $("#gameRate").val();
			
			var table= $(".template_cache .table_1").clone(true);
			table.attr("id", "dt1");
			$('#data1').append(table);
			
			$('#dt1').dataTable( {
		        "processing": true,
		        "serverSide": true,
		        ajax: {
				          data: function(d){
				        	  d.id=ps.id;
				        	  d.gameId=ps.gameId;
				        	  d.snid=ps.snid;
				        	  d.indicators=ps.indicators;
				        	  d.queryType=ps.queryType;
				        	  d.beginTime=ps.beginTime;
				        	  d.endTime=ps.endTime;
				        	  d.source=ps.source;
				        	  d.clientid="";
				        	  d.view=ps.view;
				          },
						  type: "POST",
						  url:"/panel_bi/gameRealTime/getGameRealTimeHourClientView.ac"
		              },
		        "pageLength": 10,
			    "language":{
			    	"url": "/dataTables/chinese_realTime.json"
			    },
		        "deferRender": true,
			    "sPaginationType" : "full_numbers",
		        "dom" : '<"clear"><"top">frt<"bottom"ip><"clear">' ,
				"bSort" : false,
			    "bFilter" : true,
			    "paging": true, //缈婚〉鍔熻兘
			    "lengthChange": false, //鏀瑰彉姣忛〉鏄剧ず鏁版嵁鏁伴噺
			    "searching": true, //杩囨护鍔熻兘
			    "ordering": true, //鎺掑簭鍔熻兘
			    "order": [[ 0, "asc" ]],
			    rowCallback:function(row, data) {
                    if ( $.inArray(data.DT_RowId, selected) !== -1 ) {
                        $(row).addClass('highlight');
                    }
                },
                columns: [
							{"data": null},
							{"data": "clientid"},
							{"data": "dnu"},
							{"data": "yesterday_dnu"},
							{"data": "dau"},
							{"data": "yesterday_dau"},
							{"data": "total_amount"},
							{"data": "yesterday_total_amount"},
							//{"data": "payer"},
							//{"data": "yesterday_payer"},
							{"data": null},
							{"data": null},
							{"data": null},
							{"data": null}
	                  ],
	        columnDefs: [
	                     
//	                      {
//	                        targets: 6,
//	                        render: function (a, b, v, d) {
//	                             return Math.round((v.total_amount/v.dau/rate)*100)/100;
//	                          }
//	                       },
//	                       {
//		                        targets: 7,
//		                        render: function (a, b, v, d) {
//		                             return  Math.round((v.total_amount/v.payer/rate)*100)/100;
//		                          }
//		                    }
							{
	                        targets: 0,
	                        render: function (a, b, v, d) {
	                             return v.start_time.substring(0,5)+ '~' +v.finish_time.substring(0,5);
	                          }
	                       },
							{
	                        targets: 8,
	                        render: function (a, b, v, d) {
	                             return v.dau == '' || v.dau == null || v.dau == 0 ? 0 : Math.round((v.total_amount/v.dau)*100)/100;
	                          }
	                       },
	                       {
		                        targets: 9,
		                        render: function (a, b, v, d) {
		                             return  v.yesterday_dau == '' || v.yesterday_dau == null || v.yesterday_dau == 0  ? 0 : Math.round((v.yesterday_total_amount/v.yesterday_dau)*100)/100;
		                          } 
		                    },
		                    {
		                        targets: 10,
		                        render: function (a, b, v, d) {
		                             return v.payer == '' || v.payer == null || v.payer == 0  ? 0 :  Math.round((v.total_amount/v.payer)*100)/100;
		                          }
		                    },
	                       {
		                        targets: 11,
		                        render: function (a, b, v, d) {
		                             return v.yesterday_payer == '' || v.yesterday_payer == null || v.yesterday_payer == 0  ? 0 :  Math.round((v.yesterday_total_amount/v.yesterday_payer)*100)/100;
		                          } 
		                    }
		                   
	                     ]
		    });

			$('#dt1').removeClass().addClass('table table-striped');
			
			isCanClientSubmit = false;
			
			$('#dt1 button').click(function (){
			    $("#downType").val("client");
		        $(this).attr('disabled',true);
		        $(this).val(2);
		        window.btnTimeout=setInterval(function(){
					$.gameUtil.btnTimeout($('#dt1 button'),window.btnTimeout);
				  },1000);
		        $("#downLoadForm").submit();
		    });
			
			$.gameUtil.trHighLight($('#dt1 tbody'),selected);
		}
		
		
	};
});
	                    