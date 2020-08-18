function login(){
    console.log("request...");

    let datav = {
        "username":$("input[name='username']").val() ,
        "password":$("input[name='password']").val() ,
        "code": $("input[name='code']").val()
    }
    $.ajax({
        url:"/user/login?t=" + new Date().getTime(),
        type:"POST",
        dataType:"json",
        async:false,
        contentType:"application/json;charset=utf-8",
        data: JSON.stringify(datav),
        success:function(data){
            if(data.header.retCode == "0"){
                setCookie("token", data.body.token, data.body.expTime)
                window.location.href = "/view/index";
            }

        },
    });
}

function setCookie(name, value, time) {
    var exp = new Date();
    exp.setTime(exp.getTime() + time);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";
}

function redirect(url) {
    axios({
        method: 'get',
        url: cookurl(url),
        headers: {'token': getAdminToken()}
    }).then(function(resp) {
        consolog.log('跳转到' + url)
    }).catch(function(error) {
        console.log(error)
    })
}