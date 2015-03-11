<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<sec:authentication property="principal.username" var="id"/>
<html>
<head>
    <title></title>
</head>
<body>
    <span class="chatUser-body">
        <div class="group"><span>나</span></div>
        <div class="list"><span>${id}</span></div>
        <div class="group"><span>친구</span></div>
    </span>
    <span class="chat-logout">
        <i class="fa fa-times"></i>
    </span>
</body>
<script>
    var json = JSON.parse('${param.users}');
    for(var i=0; i<json.users.length; i++) {
        var username =json.users[i].id;
        var div = $('<div>').addClass('list');
        var nameSpan = $('<span>').text(username);
        var chatSpan = $('<span>').addClass('plus').append($('<i>').addClass('fa fa-weixin'));
        div.append(nameSpan, chatSpan);
        $('.chatUser-body').append(div);
    }
    $(document).ready(function() {
        $('.user-header').find('.kind').removeClass('active');
        $('.user-header').find('.kind:eq(0)').addClass('active');
        $('.user-header').find('.user-plus').addClass('hide');
        $('.user-header').find('.users').removeClass('hide');

        $('.plus').on('click', function() {
            var to = $(this).closest('div').find('span:eq(0)').text();
            $('.user-body').load('resources/chat/roomsChat.jsp', {"to": to}, function() {
                stomp.send('/app/messageChk', {}, to);
            });
        });
        $('.chat-logout').on('click', function() {
            window.location.href="/logout";
        })
    })
</script>
</html>
