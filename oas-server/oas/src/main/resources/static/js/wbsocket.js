
var socket,timer;
function connect(selector) {
	var topicName = selector.value;
    //Connect WebSocket server
    socket =new WebSocket("ws://127.0.0.1:8080/wbSocket/"+topicName);
    //open
    socket.onopen = function () {
    	console.log("WebSocket is open");
    }
    //Get message
    socket.onmessage = function (msg) {
        console.log(msg.data)
    }
    //close
    socket.onclose = function () {
    	console.log("WebSocket is closed");
    }
    //error
    socket.onerror = function (e) {
    	console.log("Error is " + e);
    }
}

function closeWs() {
    socket.close();
}

function sendMsg(message) {
	if(socket && socket.readyState == 1){
		socket.send(message);
	}else{
		console.log("webSocket is closed");
	}
		
}
/**
 * 发送信息至kafka
 * @param data 传输的数据
 * @param topicName 主题
 * @param sendM 发送按钮id
 * @returns
 */
function sendMsgToKafka(data,selector,sendM){
	var topicName = selector.value;
	data || (data = sendM.value)
	var info = {"value":data}
	$.ajax({
		   type: 'post',
		   url: '/api/v1/userCenter/sendKafkaInfo/'+topicName,
		   data: JSON.stringify(info),
		   contentType : 'application/json;charset=utf8',
		   dataType: 'json',
		   cache: false,
		   success: function (res) {}
		  });
}
//x上限，y下限 
function generaterRandom(x,y){    
    return rand = Math.random() * (x - y + 1) + y; 
}

function setTimer(selector,sendM){
	timer = setInterval (function(){
     	sendMsgToKafka(undefined,selector,sendM);//generaterRandom(2,1)
    }, 1000);
}

function stopInterval(){
    clearInterval(timer);
}

Date.prototype.Format = function (fmt) { 
	var o = { 
	"MM": this.getMonth() + 1, //月份 
	"dd": this.getDate(), //日 
	"HH": this.getHours(), //小时 
	"mm": this.getMinutes(), //分 
	"ss": this.getSeconds(), //秒 
	"qq": Math.floor((this.getMonth() + 3) / 3), //季度 
	"SS": this.getMilliseconds() //毫秒 
	}; 
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)); 
	for (var k in o) 
	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length))); 
	return fmt; 
}

    