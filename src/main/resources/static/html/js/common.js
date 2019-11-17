
/**
 * 系统服务根地址
 */
var rootPath = function(){
    //获取当前网址，如： http://localhost:8083/proj/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： proj/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPath = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/proj
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);
    return(localhostPath + projectName);
}()

//定义一个全局变量，用以页面跳转参数传递
var platSearchParams = {};

$(function(){

	$("a[data-href],button[data-href]").click(function(){
		var url = $(this).data("href");
		//window.location.href = url;
		$("#mainDiv").load(url);
	});
	$("a.sidebar-toggle").click(function(){
		if($("aside.left-side").is(":hidden")){
			$("aside.left-side").show();
			$("aside.right-side").css("margin-left","220px");
		}else{
			$("aside.left-side").hide();
			$("aside.right-side").css("margin-left","0px");
		}
		
	});
	$("a[data-href]").css("cursor", "pointer");
	
	$("#loginOut").click(function(){
		$.ajax({
			url:'/admin/loginout',
			type:'get',
			dataType:'json',
			success:function(){
				window.location.href='/login';
			}
		});
	});
});
//关闭窗口
$.dialogClose = function(){
	$("#close").trigger("click");
	$("div.modal-backdrop").remove();
	$("#modal").remove();
}
/**
 * dialog
 */
$.dialog = function(title, content, fn, params, height){
	var modalObj = $("#modal");
	if(modalObj.size() == 0){
			modalObj = $('<div class="modal fade" data-backdrop="static" id="modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
					'<div class="modal-dialog">'+
					'<div class="modal-content">'+
						'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">'+
								'&times;'+
							'</button>'+
							'<h4 class="modal-title">'+
								'提示'+
							'</h4>'+
						'</div>'+
						'<div class="modal-body">'+
							''+
						'</div>'+
						'<div class="modal-footer">'+
							'<button type="button" id="close" class="btn btn-default" data-dismiss="modal">关闭'+
							'</button>'+
							'<button type="button" id="submit" class="btn btn-primary">'+
								'提交'+
							'</button>'+
						'</div>'+
					'</div>'+
				'</div>'+
					'</div>');
		 
		 $("body").append(modalObj);
	}
	if(title){
		modalObj.find("h4.modal-title").html(title);
	}
	if(content){
		modalObj.find("div.modal-body").html(content);
	}
	$("#submit").unbind("click").bind("click", function(){
		if(fn){
			fn(params);

			$.dialogClose();
		}
	});
	if(height){
		modalObj.find("div.modal-body").height(height);
	}
	modalObj.modal({
        keyboard: true
    });
	$("#submit").show();
}
    /**
     * 选择框
     */
    $.confirm = function(content, fn, params,title){
    	if(!title){
    		title = '提示';
    	}
    	$.dialog(title, content, fn, params);
	}
    /**
     * 提示
     */
    $.alert = function(content, title){
    	$.confirm(content,null,null,title);
    	$("#submit").hide();
    }

    //修改密码
    $(function(){
		$("#changePassword").click(function(){
			var html = '<div class="form-group">'+
		    '<label for="lastname" class="col-sm-3 control-label">新密码:</label>'+
		    '<div class="col-sm-7">'+
		      '<input type="password" class="form-control" id="newPass" placeholder="请输入新密码">'+
		    '</div>'+
		  '</div>';
		  
			$.dialog("重置密码", html,function(){

				var newPass = $("#newPass").val().trim();

				$.ajax({
					url:'/admin/changePass',
					type:'post',
					dataType:'json',
					data:{
						"newPass":newPass
					},
					success:function(event,res){
						alert(res.msg);
						if(res.code == '0000'){
							$.dialogClose();
							window.location.href ='/login';
						}

					},error:function(e){
						$.alert("操作失败:"+e);
					}
				});
			},null,50);

		});
	})


  /**
	** 分页
	**/
	function showPage(url, currentPage, totalPages, fn, thisObj){
		if(!currentPage){
			currentPage = 1;
		}
		if(!totalPages){
			totalPages = 1;
		}
		$('#pageLimit').bootstrapPaginator({
		      currentPage: currentPage,//当前的请求页面。
		      totalPages: totalPages,//一共多少页。
		      size:"normal",//应该是页眉的大小。
		      bootstrapMajorVersion: 3,//bootstrap的版本要求。
		      alignment:"right",
		      numberOfPages:10,//一页列出多少数据。
		      itemTexts: function (type, page, current) {//如下的代码是将页眉显示的中文显示我们自定义的中文。
		         switch (type) {
		         case "first": return "首页";
		         case "prev": return "上一页";
		         case "next": return "下一页";
		         case "last": return "末页";
		         case "page": return page;
		         }
		     },
		     onPageClicked: function (event, originalEvent, type, page){
		    	 thisObj.search(url, fn, page);
		     }
		 });
	}


/**
 * 分页查询
 * @param url  请求地址
 * @param formId  入参form
 * @param titleList
 * [
 * {
 * text:'姓名',
 * title:'userName'
 * format:function(obj){
 *  return obj.userName;
 * }
 * }
 * ]
 *  @param callBack 回调函数
 */
$.fn.search2 = function(url, formId, titleList,callBack){
	var table = $(this);
	if(!titleList){
		alert("标题数组不能为空!");
		return false;
	}
	//标题显示
	var thead = table.find("thead");
	if(thead.size()==0){
       thead = $('<thead></thead>');
        table.append(thead);
	}
    thead.html("");
    var theadTr = $("<tr class=\"bg-success\"></tr>");
    thead.append(theadTr);
    theadTr.append("<th>序号</th>");
	$(titleList).each(function(i,obj){
        theadTr.append("<th>"+obj.text+"</th>");
	});
	//内容显示
	var fn = function(pageNo, pageSize, dataList){
        if (!pageNo) {
            pageNo = 1;
        }
        var tbody = table.find("tbody");
        if(tbody.size()==0){
        	tbody = $("<tbody></tbody>");
            table.append(tbody);
		}
        tbody.html("");
        $(dataList).each(function(index, obj){
            var tr = $("<tr></tr>");
            tr.append($("<td></td>").html((pageSize * (pageNo - 1)) + (index) + 1));
            var ll = titleList.length;
            for(var j=0;j<ll;j++){
                var obj = dataList[index];
                var title = titleList[j].title;
                var format =  titleList[j].format;
                var html = "";
                if(format){
                	html = format(obj);
				}else{
                	var tt = title.split("\.");
                	var l = tt.length;
                	var target = obj;
                	for(var a=0;a<l;a++){
                		target = target[tt[a]];
					}
                	html = target;
				}
				if(!html){
                	html = "--";
				}
                tr.append("<td>"+html+"</td>");
                tbody.append(tr);
			}
		});
	}
    $.fn.search(url, fn, null, formId, table, callBack);
}

/**
 * 根据formId，进行简单的赋值
 */
function setDataByFormId(formId, data){
	if(!data){
		$.alert("对象不能为空")
	}else{
		for(key in data){
			try {
				$("#"+formId).find("[name="+key+"]").val(data[key]);
			}catch (e) {

			}

		}
	}
}
/**
 * 根据formid获取表单内容
 * @param formId
 */
function getParamsByFormId(formId){
	var params = {};
	if(formId) {
		var form = $("#" + formId);
		////所有的input//////////////
		var inputs = form.find("input[name]");
		inputs.each(function (index, obj) {
			var name = $(obj).attr("name");
			var value = $(obj).val().trim();
			if (value) {
				params[name] = value;
			}
		});
		//////////////所有的select/////////////////////////
		var selects = form.find("select[name]");
		selects.each(function (index, obj) {
			var name = $(obj).attr("name");
			var value = $(obj).val();
			if (value) {
				params[name] = value;
			}
		});

		//////////////所有的textarea/////////////////////////
		var textAreas = form.find("textArea[name]");
		textAreas.each(function (index, obj) {
			var name = $(obj).attr("name");
			var value = $(obj).val();
			if (value) {
				params[name] = value;
			}
		});
	}
		return params;
}

/**
 * 分页查询
 * @param url  请求地址
 * @param fn  回调函数
 * @param pageNo 当前页码
 * @param formId  入参form
 */
	$.fn.search = function(url, fn,pageNo, formId, table, callBak){

		var pageDiv = $("#pageDiv");
		if(pageDiv.size()==0){
			pageDiv = $('<div id="pageDiv" style="text-align: center"> <ul id="pageLimit"></ul> </div>');
			if(!table){
				table = $(this);
			}
			table.after(pageDiv);
		}
		if(!pageNo){
			pageNo = 0;
		}

		var attrNum = 0;
		  var params = {"pageNo":pageNo, "pageSize":5};
		if(formId){
			var form = $("#"+formId);
			var inputs = form.find("input[name]");
			inputs.each(function(index, obj){
				var name = $(obj).attr("name");
				var value = $(obj).val().trim();
				if(value &&  !params[name]){
                    params[name] = value
                    attrNum++;
                }
			});
			var selects = form.find("select[name]");
			selects.each(function(index, obj){
				var name = $(obj).attr("name");
				var value = $(obj).val();
				if(value){
                    params[name] = value;
                    attrNum++;
                }
			});
		}


		//获取页面参数，表示当前路径的(返回时参数不丢失)
//		var page = getQueryString("page");
//		if(!page){
//			page =CRC.ToModbusCRC16(url, true);
//			var href = window.location.href+"?page="+page;
//            window.history.pushState(null, null, href);
//		}

		//曾经保存过参数，且当前表单参数为空，则用之前的参数查询
//		if(attrNum == 0 && params.pageNo == 0 && sessionStorage.getItem(page)){
//			params = JSON.parse(sessionStorage.getItem(page)) ;
//		}
        if(!params){
            params =  {"pageNo":1, "pageSize":5};
        }else if(params.pageNo == 0){
			params.pageNo = 1;
		}
//		sessionStorage.setItem(page, JSON.stringify(params));

		$.ajax({
			"url":url,
			type:'get',
			dataType:'json',
			data: params,
			success:function(pageInfo){
				if(pageInfo){
					showPage(url, pageInfo.pageNo, pageInfo.pages, fn, $(this));
					var pageSize = pageInfo.pageSize;
					var pageNo = pageInfo.pageNo;
					if(fn){
						fn(pageNo, pageSize, pageInfo.dataList);
					}
				}

				if(callBak)callBak();
			},error:function(e){
				alert("系统繁忙，请稍后再试")
			}
		});
	}
	/**
	 * 从浏览器获取参数
	 * @param name
	 * @returns
	 */
	function getQueryString(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) return unescape(r[2]); return null;
	 }
	/**
	 * 时间格式化
	 * @param times
	 * @returns
	 */
	Date.prototype.format = function(fmt) {
	     var o = {
	        "M+" : this.getMonth()+1,                 //月份
	        "d+" : this.getDate(),                    //日
	        "h+" : this.getHours(),                   //小时
	        "m+" : this.getMinutes(),                 //分
	        "s+" : this.getSeconds(),                 //秒
	        "q+" : Math.floor((this.getMonth()+3)/3), //季度
	        "S"  : this.getMilliseconds()             //毫秒
	    };
	    if(/(y+)/.test(fmt)) {
	            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	    }
	     for(var k in o) {
	        if(new RegExp("("+ k +")").test(fmt)){
	             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	         }
	     }
	    return fmt;
	}
	function date_format(times){
		if(!times){
			return "";
		}
		var time2 = new Date(times).format("yyyy-MM-dd hh:mm:ss");
	    return time2;
	}
	/**
	 * 光标位置插入html
	 * @param dom
	 * @returns
	 */
	function crusor_insert(dom){
		var range;//记录光标位置对象
		var node = window.getSelection().anchorNode;
		// 这里判断是做是否有光标判断，因为弹出框默认是没有的
		if(node!=null){
		    range = window.getSelection().getRangeAt(0);// 获取光标起始位置
		}else{
		　　range = undefined;
		}
		range.insertNode(dom);// 在光标位置插入该对象
	}
	/**
	 * 获取域名
	 * @returns
	 */
	function getHost(){
		return window.location.host;
	}

/**
 * form表单name集合
 * @param formId
 */
function formNames(formId){
    var result = [];
    var jsonObj = formToJson(formId);
    for(var x in jsonObj){
        result.push(x);
    }
    return result;
}

/**
 * form表单到json对象的转换
 * @param formId
 */
function formToJson(formId){
    var result = {};
    if(!formId){
        $.alert("formId不能为空!");
    }else{
        var inputObjs  = $("#"+formId).find("input");
        var selectObjs = $("#"+formId).find("select[name]");
        var textareaObjs = $("#"+formId).find("textarea[name]");
        var radioObjs = $("#"+formId).find("input:radio[name]");
        var cheboxObjs = $("#"+formId).find("input:checkbox[name]");
        /**
         * 遍历input
         */
        $.each(inputObjs, function(i, docObj){
            var obj = $(docObj);
            var name = obj.attr("name");
            var value = obj.val();
            if(value){
                result[name] = value;
            }else{
                result[name] = '';
            }
        });
        /**
         * 遍历select
         */
        $.each(selectObjs, function(i, docObj){
            var obj = $(docObj);
            var name = obj.attr("name");
            var value = obj.val();
            if(value){
                if(typeof(value) == 'string'){
                    result[name] = value;
                }else if(typeof(value) == 'object'){
                    result[name] = value.join(",");
                }
            }else{
                result[name] = '';
            }
        });
        /**
         * 遍历textarea
         */
        $.each(textareaObjs, function(i, docObj){
            var obj = $(docObj);
            var name = obj.attr("name");
            var value = obj.val();
            if(value){
                result[name] = value;
            }else{
                result[name] = '';
            }
        });
        /**
         * 遍历radio
         */
        $.each(radioObjs, function(i, docObj){
            var obj = $(docObj);
            var name = obj.attr("name");
            var value = obj.val();
            var checked = obj.attr("checked");
            if(value &&  obj.is(":checked")){
                result[name] = value;
            }
        });
        /**
         * 遍历checkbox
         */
        $.each(cheboxObjs, function(i, docObj){
            var obj = $(docObj);
            var name = obj.attr("name");
            var value = obj.val();
            var checked = obj.attr("checked");
            if(value && obj.is(":checked")){
                if(result[name]){
                    result[name] = result[name]+","+value;
                }else{
                    result[name] = value;
                }
            }
        });
    }
    return result;
}

/**
 * 跳转至
 * @param url
 */
function redirectTo(url){
	$("#mainDiv").load(url);
}

/**
 * @param dir 文件目录
 * @param fileId 文件上传组件的id
 * @param fileName 文件名，不填写则自动生成
 * @param urlName input输入框名字，用来承载上传成功后的下载路径
 */
function initFile(fileId,dir, fileName, urlName, initFileName){
	//初始化token
	var url ='/qiniu/token';
	if(fileName){
		url +="?fileName="+fileName;
		if(dir){
			url +="&dir="+dir
		}
	}else{
		if(dir){
			url +="?dir="+dir
		}
	}

	fileConfig = {};
	$.ajax({
		type:'get',
		url:url,
		dataType:'json',
		success:function(res){
			var data = res.data;
			console.log(data)
			fileConfig["token"] = data.token;
			fileConfig["uploadUrl"] = data.uploadUrl;
			fileConfig["key"] = data.key;
			fileConfig["download"] = data.download;

			initFileUpload(fileId, urlName, initFileName)
		}
	});

}
//初始化图片上传插件
function initFileUpload(fileId, urlName, initFileName) {
	if(!initFileName) initFileName= [];
	var initFiles = [];
	$.each(initFileName, function(i, url){
		var img = "<img src='"+url+"'/>";
		initFiles.push(img)
	})
	//商品图片上传
	$('#'+fileId).fileinput({
		theme: 'fa',
		language: 'fr',
		uploadUrl:fileConfig["uploadUrl"],
		allowedFileExtensions: ['jpg', 'png', 'gif'],
		allowedFileTypes: ['image'],
		showRemove: false,
		initialPreview: initFiles,
		uploadExtraData:{
			"token":fileConfig["token"],
			"key":fileConfig["key"]
		},
		showUpload: false
	}).on("fileuploaded", function(res) {
		console.log("res---------------")
		$.alert("图片上传成功", function () {

			var url = fileConfig["download"]+fileConfig["key"];
			if(urlName){
			 var  multiple = 	$('#'+fileId).attr("multiple");
			 if(multiple){
				 var v = $("[name='"+urlName+"']").val();
				 if(v && v.trim()!=''){
					 v = v+ ",";
				 }
				 v = v+url;
				 $("[name='"+urlName+"']").val(v);
			 }else{
				 $("[name='"+urlName+"']").val(url);
			 }
			}

			//刷新fileConfig 为下一个图片上传使用
			$.ajax({
				type: 'get',
				url: url,
				dataType: 'json',
				success: function (res) {
					var data = res.data;
					fileConfig["token"] = data.token;
					fileConfig["uploadUrl"] = data.uploadUrl;
					fileConfig["key"] = data.key;
					fileConfig["download"] = data.download;
				}
			});

		})

	});
}