window.JLIVECHAT = {};

function ChatRequest(requestId){
    this.requestId = requestId;
}

function ChatBody(){
    this.height =
}

function JliveChat(apiUrl){
    this.apiUrl = apiUrl;
    this.isAuthorized = false;
    this.isShown = false;
    this.request = null;

    this.checkAuthorized = function(){
        if(this.request != null){
            $.get(this.apiUrl)
        } else {
            this.isAuthorized = false;
        }
    }

    this.show = function(){

    }
}

function getWindowVariable(name){
    return window.JLIVECHAT[name];
}
function setWindowVariable(name, value){
    window.JLIVECHAT[name] = value
}

function getBaseRESTPath(){
    return "http://localhost:4567";
}

function checkAuthorized(){
    if (getWindowVariable("requestId") == null) {
        return false;
    }

}