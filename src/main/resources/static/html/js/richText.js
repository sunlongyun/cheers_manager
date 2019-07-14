//富文本
$.richText = function(id){
	if($("#toolbar").size()>0){
		return quill;
	}
	var toolsHtml = '<div class="type0 type" id="toolbar">'+
				'<select title="字体尺寸" class="ql-size">'+
				'<option value="small"></option>'+
				'<option selected="selected"></option>'+
				'<option value="large"></option>'+
				'<option value="huge"></option>'+
			'</select>'+
			'<select title="标题" class="ql-header">'+
				'<option value="1"></option>'+
				'<option value="2"></option>'+
				'<option value="3"></option>'+
				'<option value="4"></option>'+
				'<option value="5"></option>'+
				'<option value="6"></option>'+
				'<option  value="false"></option>'+
			'</select>'+
			  '<span class="ql-formats">'+
			      '<select class="ql-font">'+
			        '<option selected="selected"></option>'+
			        '<option value="sans-serif"></option>'+
			        '<option value="monospace"></option>'+
			      '</select>'+
			    '</span>'+
			    '<span class="ql-formats">'+
			      '<button type="button" class="ql-bold"></button>'+
			      '<button type="button" class="ql-italic"></button>'+
			      '<button type="button" class="ql-underline"></button>'+
			      '<button type="button" class="ql-strike"></button>'+
			    '</span>'+
			    '<span class="ql-formats">'+
			      '<button type="button" class="ql-list" value="ordered"></button>'+
			      '<button type="button" class="ql-list" value="bullet"></button>'+
			      '<select class="ql-align">'+
			        '<option selected="selected"></option>'+
			        '<option value="center"></option>'+
			        '<option value="right"></option>'+
			        '<option value="justify"></option>'+
			      '</select>'+
			    '</span>'+
			'<button class="ql-image" title="本地上传"></button>'+
			'<button title="本系统图片" onclick="showAddFile(1)">'+  
			   '<i class="glyphicon glyphicon-picture"></i>'+
			'</button>'+
			/*'<button title="本系统视频" onclick="showAddFile(2)">'+
				'<i class="glyphicon glyphicon-facetime-video"></i>'+
			'</button>'+
			'<button title="本系统音频" onclick="showAddFile(3)">'+
				'<i class="glyphicon glyphicon-volume-up"></i>'+
			'</button>'+*/
			'<button class="ql-script" value="sub"></button>'+
			'<button class="ql-script" value="super"></button>'+
			'</div>';
	 
	 var obj = $("#"+id);
	 obj.before(toolsHtml);
	 quill = new Quill("#"+id, {theme:'snow', modules:{"toolbar":"#toolbar"}}); 
	 quill.focus();
	 return quill;
}
/**
 * 获取富文本内容
 */
$.getRichText = function(){
	
	var html = "";
	var content = quill.container.firstChild.innerHTML;
	var conObj = $(content);
	var imgs  = conObj.find("img");
	if(imgs.size() >0){
		imgs.attr("width", "100%");
	}
	var size = conObj.size();
	for(var i=0;i<size;i++){
		var tagHtml = conObj[i];
		html += tagHtml.innerHTML;
	}
	return html;
}
/**
 * 设置富文本内容
 */
$.setRichText = function(content){
	quill.container.firstChild.innerHTML =content;
}
/**
** 本系统文件
**/
function showAddFile(type){
	var html = '<div class="form-group">'+
    '<label for="fileName" class="col-sm-3 control-label">文件名:</label>'+
    '<div class="col-sm-7">'+
      '<input type="text" class="form-control" id="fileName" placeholder="请输入文件名称">'+
    '</div>'+
  '</div>';
	
	$.dialog("选择素材", html,function(){
		var fileName = $("#fileName").val();
		if("" == fileName.trim()){
			alert("文件名称不能为空!");
			return ;
		}
	    $.dialogClose();
		var url = '/api/article/showFile/'+fileName;
		if(type == 1){
			 quill.focus();
			var index = quill.getSelection().index;
			if(!index){
				index = 1;
			}
			quill.insertEmbed(index, 'image', url) 
		}else if(type == 2){
			
			var videoHtml = "<video  width='100%'  src='"+url+"' controls='controls'>您的浏览器不支持 video 标签。</video><p>&nbsp;<br/></p>";
			 quill.focus();
			var index = quill.getSelection().index;
			if(!index){
				index = 1;
			}
			quill.pasteHTML(index, videoHtml, "");
			quill.focus();
		}else if(type == 3){
			var audioHtml = "<video height='50'  src='"+url+"' controls='controls'>您的浏览器不支持 audio 标签。</video><p>&nbsp;<br/></p>";
			 quill.focus();
			var index = quill.getSelection().index;
			if(!index){
				index = 1;
			}
			quill.pasteHTML(index, audioHtml, "");
			quill.focus();
		}
	},null, 40);
}