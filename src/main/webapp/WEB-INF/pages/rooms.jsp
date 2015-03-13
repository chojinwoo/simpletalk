<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<sec:authentication property="principal.username" var="id"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Simple Web Chatting</title>
    <link rel="stylesheet" href="//fonts.googleapis.com/earlyaccess/hanna.css">
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/font-awesome.min.css">
    <link rel="stylesheet" href="/resources/css/chatStyles.css">
    <script type="text/javascript" src="/resources/js/jquery-2.1.3.min.js"></script>
    <%--<script type="text/javascript" src="/resources/js/jquery.mobile-1.4.5.min.js"></script>--%>
    <script type="text/javascript" src="/resources/js/jquery.mobile-events.min.js"></script>
    <script type="text/javascript" src="//code.jquery.com/ui/1.11.3/jquery-ui.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jqueryui-touch-punch/0.2.2/jquery.ui.touch-punch.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="/resources/js/sockJs.js"></script>
    <script type="text/javascript" src="/resources/js/stomp.js"></script>
    <script type="text/javascript" src="/resources/js/stompSet.js"></script>
</head>
<body>
<div class="user-content">
    <div class="user-header">
        <span class="kind active"><i class="fa fa-user" style=""></i></span>
        <span class="kind"><i class="fa fa-weixin fa-chatList"></i></span>

        <span class="users"><i class="fa fa-users"></i></span>
        <span class="user-plus hide"><i class="fa fa-user-plus"></i></span>
    </div>
    <div class="user-body">
    </div>
</div>
<script>
    $(document).ready(function() {
        var ws = new SockJS("/ws");
        stomp = Stomp.over(ws);
        stomp.connect([], function(frame) {
            console.log(frame);
            stomp.subscribe("/topic/message", function(data) {
                var json = JSON.parse(data.body);
                var to;
                if(json.from == '${id}') {
                    to = json.to;
                    var clone = $('.chat').find('.hide:eq(0)').clone();
                    clone.removeClass('hide');
                    clone.find('.chat-name').text(json.from);
                    if(json.emoticon == "" || json.emoticon == null) {
                        clone.find('.chat-emoticon > img').addClass('hide');
                    } else {
                        clone.find('.chat-emoticon > img').attr('src', json.emoticon);
                    }

                    clone.find('.chat-content').text(json.message);
                    clone.find('.chat-date').text(json.time);
                    $('.chat').append(clone);
                }else if(json.to == '${id}') {
                    to = json.from;
                    var clone = $('.chat').find('.hide:eq(1)').clone();
                    clone.removeClass('hide');
                    clone.find('.chat-name').text(json.from);
                    if(json.emoticon == "" || json.emoticon == null) {
                        clone.find('.chat-emoticon > img').addClass('hide');
                    } else {
                        clone.find('.chat-emoticon > img').attr('src', json.emoticon);
                    }
                    clone.find('.chat-content').text(json.message);
                    clone.find('.chat-date-reverse').text(json.time);
                    $('.chat').append(clone);
                }
                $('.chat').find('.hide:eq(1)').clone();
                $('body').scrollTop($('.chat').height());
                var chatStayFlag = '1';
                if($('body').find('.chat').length > 0) {
                    chatStayFlag = '0';
                }

                stomp.send("/app/syncChatList", {}, JSON.stringify({"chatStayFlag":chatStayFlag, "to":to}));
            })

            stomp.subscribe("/topic/syncChatList", function(data) {
                var json = JSON.parse(data.body);
                for(k in json) {
                    var rm = json[k];
                    var writerCount = 0;
                    if(rm.this == '${id}') {
                        for(var i=0; i<rm.msg.length; i++) {
                            var writerFlag = rm.msg[i].flag;
                            if(writerFlag == 1) {
                                ++writerCount;
                            }

                            if(i == rm.msg.length -1) {
                                var msg = rm.msg[i];
                                var date = msg.date;
                                var message = msg.message;
                                var name = k;
                                $('.chatList-body .chatList:not(.hide)').remove();
                                var clone = $('.chatList-body > .hide').clone();

                                clone.removeClass('hide');
                                clone.find('.chatList-name span:last').text(date);
                                clone.find('.chatList-name span:first').text(name);
                                clone.find('.chatList-content p').text(message);
                                if(writerCount > 0) {
                                    var countP = $('<span>').addClass('chatList-badge');
                                    countP.text(writerCount);
                                    clone.find('.chatList-content p').append(countP);
                                }
                                console.log(clone.html());
                                $('.chatList-body').append(clone);
                            }
                        }
                    }
                }
                $('.chatList').off();
                $('.chatList').click(function() {
                    var to = $(this).find('.chatList-name span:first').text();
                    $('.user-body').load('resources/chat/roomsChat.jsp', {"to": to}, function() {
                        stomp.send('/app/messageChk', {}, to);
                    });
                })
            })
        })

        $(document).on('swiperight', function() {
            if($('.chatList-body').length > 0) {
                $('.user-body').load('/resources/chat/roomsChatUser.jsp',{'users':'${users}'});
            }
        })
        $(document).on('swipeleft', function() {
            if($('.chatUser-body').length > 0) {
                $('.user-body').load('/resources/chat/roomsChatList.jsp');
            }
        })

        $('.user-body').load('/resources/chat/roomsChatUser.jsp', {'users':'${users}'});

        $('.fa-user').on('click', function() {
            $('.user-body').load('/resources/chat/roomsChatUser.jsp',{'users':'${users}'});
        })
        $('.fa-chatList').on('click', function() {
            $('.user-body').load('/resources/chat/roomsChatList.jsp');
        })

    })

</script>
</body>
</html>
