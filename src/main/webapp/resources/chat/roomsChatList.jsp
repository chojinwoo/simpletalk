<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<sec:authentication property="principal.username" var="id"/>
<html>
<head>
    <title></title>
</head>
<body>
<div class="chatList-body">
    <div class="chatList hide">
        <div class="chatList-name">
            <span style="font-size:15px; font-weight:bold"></span><span style="position: absolute;right: 0;margin-right: 11px;"></span>
        </div>
        <div class="chatList-content">
            <p></p>
        </div>
    </div>
</div>
</body>
<script>

    $.chatListInit = function() {
        $.ajax({
                url:'/roomList',
                dataType:'json',
                type:'post',
                success:function(data) {
                for(k in data) {
                    var rm = data[k];
                    var writerCount = 0;
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
                            $('.chatList-body').append(clone);
                        }
                    }
                }
                $('.chatList').off();
                $('.chatList').click(function() {
                    var to = $(this).find('.chatList-name span:first').text();
                    $('.user-body').load('resources/chat/roomsChat.jsp', {"to": to}, function() {
                        $.writerSync();
                        stomp.send('/app/messageChk', {}, to);
                    });
                })
            }
        })
    }

    $(document).ready(function() {
        $('.user-header').find('.kind').removeClass('active');
        $('.user-header').find('.kind:eq(1)').addClass('active');
        $('.user-header').find('.user-plus').addClass('hide');
        $('.user-header').find('.users').removeClass('hide');
        $.chatListInit();
    })
</script>
</html>
