function getQueryString(name) { 
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
        var r = window.location.search.substr(1).match(reg); 
        if (r != null) return unescape(r[2]); 
        return null; 
}

function getCart(){
	var cart = localStorage.getItem("myCart");
	if(!cart){
		cart = {num:0,goods:[]};
		return cart;
	}else{
		try{
			return JSON.parse(localStorage.getItem("myCart"));
		}catch(e){
			localStorage.clear();
		}
		return {num:0,goods:[]};
	}
}
function addCacheCart(goods){
	var cart = getCart();
	var flag = false;
	var num = 0;
	for(var g in cart.goods ){
		var gg = cart.goods[g];
		num = num+gg.num;
		if(gg.id == goods.id){
			gg.num+=1;
			flag= true;
		}
	}
	if(!flag){
		cart.goods.push(goods);
		num +=1;
	}
	cart.num = num;
	localStorage.setItem("myCart",JSON.stringify(cart));
}
