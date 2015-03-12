<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<security:authentication property="principal.username" var="id"/>
<security:authentication property="principal.regId" var="regId"/>

<html>
<head>
    <script type="text/javascript" src="/resources/js/stompSet.js"></script>
    <title>Simple Web Chatting</title>
</head>

<!-- Modal -->
<div class="modal fade" id="emoModal" style="z-index:1050;" tabindex="-1" role="dialog" aria-labelledby="emoModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true" style="  position: relative; top: 18px;right: 18px;">&times;</span></button>
                <ul class="nav nav-tabs" id="emoKind">
                    <li role="presentation" class="active"><a href="#" page="/resources/emoticon/sinjjang/sinjjang.jsp" ><img src="/resources/emoticon/mainImage/sinjjang.png" class="emo-preview"></a></li>
                    <li role="presentation"><a href="#" page="/resources/emoticon/sinjjang2/sinjjang2.jsp" ><img src="/resources/emoticon/mainImage/sinjjang2.png" class="emo-preview"></a></li>
                    <li role="presentation"><a href="#" page="/resources/emoticon/prodoAndNeo/prodoAndNeo.jsp"><img src="/resources/emoticon/mainImage/prodoAndNeo.png" class="emo-preview"></a></li>
                    <li role="presentation"><a href="#" page="/resources/emoticon/poi/poi.jsp"><img src="/resources/emoticon/mainImage/poi.png" class="emo-preview"></a></li>
                    <%--<li role="presentation"><a href="#">Messages</a></li>--%>
                </ul>
                <div class="emo-paging"><i class="fa fa-circle-thin active"></i><i class="fa fa-circle-thin"></i><i class="fa fa-circle-thin"></i><i class="fa fa-circle-thin"></i></div>
            </div>
            <div class="modal-body emo-body">
            </div>
        </div>
    </div>
</div>

<body>
<div>
    <div class="chat">
        <div class="chat-detail hide" style="width:100%; padding:10px 10px 0px 10px;;">
            <div class="chat-name" >
            </div>
            <div style="height:auto;">
                <div class="chat-emoticon">
                    <img src=""/>
                </div>
                <div class="chat-content">
                    <p>
                    </p>
                </div>
                <div class="chat-date">
                </div>
            </div>
        </div>
        <div class="chat-detail hide" style="width:100%; padding:10px 10px 0px 10px;;text-align:right;">
            <div class="chat-name" >
            </div>
            <div style="height:auto;">
                <div class="chat-emoticon">
                    <img src=""/>
                </div>
                <div class="chat-content chat-content-reverse">
                    <p>
                    </p>
                </div>
                <div class="chat-date-reverse">
                </div>
            </div>
        </div>
        <div style="width:100%;position:fixed; bottom:0px;">
            <div class="input-group" style="background-color:#F0F0F0;height:42px;padding:4px 4px 4px 40px;">
                <span class="file-btn" aria-hidden="true"><i class="fa fa-plus"></i></span>
                <input type="text" class="form-control message">
                <span class="emo-btn" aria-hidden="true"><i class="fa fa-smile-o fa-smile"></i></span>
                <span class="input-group-addon btn btn-default send-btn">전송</span>
            </div>
            <style>

                .console {
                    display:none;
                }

                .console > .row {
                    padding-top:15px;
                    padding-bottom:15px;
                }
                .console > .row > .col-xs-4 {
                    font-size: 2.5em;
                }
            </style>
            <div class="console" style="height:100%;background-color:#F0F0F0;">
                <div class="row" style="text-align:center;">
                    <div class="col-xs-4">
                        <i class="fa fa-picture-o"><input class="fa-picture-hidden" type="file"></i>
                    </div>
                    <div class="col-xs-4">
                        <i class="fa fa-picture-o"></i>
                    </div>
                    <div class="col-xs-4">
                        <i class="fa fa-picture-o"></i>
                    </div>
                </div>
                <div class="row" style="text-align:center;">
                    <div class="col-xs-4">
                        <i class="fa fa-picture-o"></i>
                    </div>
                    <div class="col-xs-4">
                        <i class="fa fa-picture-o"></i>
                    </div>
                    <div class="col-xs-4">
                        <i class="fa fa-picture-o"></i>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="emo-chat" aria-hidden="true">
        <img src=""/>
        <span id="emoCancel" class="glyphicon glyphicon-remove-sign"></span>
    </div>
</div>
<script>
    $.readURL = function(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('.emo-chat img').attr('src', e.target.result);
                $('.console').hide('blind', 200);
                $('.emo-chat').show('slow');
            }

            reader.readAsDataURL(input.files[0]);
        }
    }
    $('.fa-picture-hidden').on('change', function() {
        $.readURL(this);
    })

    $.emoticonChange = function(firstEmoIdx) {
        $('#emoModal .modal-body').load($('#emoKind .active a').attr('page')+" #emoPage" +firstEmoIdx, function() {
            $('.emo-body a').off();
            $('.emo-body a').click(function() {
                $('#emoCancel').closest('p').remove();
                var src = $(this).find('img').attr('src');
                $('.emo-chat').find('img').attr('src', src);
                $('.emo-chat').show('slow');
                $('#emoModal').modal('hide');
            })
        });
    }

    var roomname = '${param.to}';
    $.chatInit = function() {
        $('.chat .chat-detail:not(.hide)').remove();
        $.ajax({
            url:'/roomMsg',
            dataType:'json',
            type:'post',
            data:'roomname='+roomname,
            success:function(data) {
                if(data.msg != null && data.msg != "") {
                    for(var i=0; i<data.msg.length; i++) {
                        var msg = data.msg[i];
                        var name = msg.name;
                        var message = msg.message;
                        var time = msg.time;
                        var emoticon = msg.emoticon;
                        if(name == '${id}') {
                            var clone = $('.chat').find('.hide:eq(0)').clone();
                            clone.removeClass('hide');
                            clone.find('.chat-name').text(name);
                            clone.find('.chat-emoticon img').attr('src', emoticon);
                            clone.find('.chat-content p').text(message);
                            clone.find('.chat-date').text(time);
                            $('.chat').append(clone);
                        } else {
                            var clone = $('.chat').find('.hide:eq(1)').clone();
                            clone.removeClass('hide');
                            clone.find('.chat-name').text(name);
                            clone.find('.chat-emoticon img').attr('src', emoticon);
                            clone.find('.chat-content p').text(message);
                            clone.find('.chat-date-reverse').text(time);
                            $('.chat').append(clone);
                        }
                    }
                }
                $('body').scrollTop($('.chat').height());
            }
        })
    }

    $.messageSend = function() {
        var msg = $('.message').val();
        var emoticon = $('.emo-chat > img').attr('src');
        stomp.send("/app/message", {}, JSON.stringify({"from":'${id}', "to":"${param.to}", "emoticon":emoticon, "message":msg, "regId":'${regId}'}));
        $('.message').val("");
        $('.emo-chat img').removeAttr('src');
        $('.emo-chat').hide('slow')
    }

    $(document).ready(function() {
        $('.user-header').find('.kind').removeClass('active');

        /*chat */
        $.chatInit();


        $('.message').on('keyup', function(e) {
            if(e.keyCode == 13) {
                $.messageSend();
            }
        })

        $('.send-btn').on('click', function() {
            $.messageSend();
        })


        /* 사진 업로드 및 파일 선택 버튼 클릭시 이벤트*/
        $('.file-btn').on('click', function() {
            var display = $('.console').css('display');
            if(display == 'none') {
                $('.emo-chat').hide('slow');
                $('.console').show("blind", 200);
            } else {
                $('.console').hide("blind", 200);
            }

        })

        $('.fa-picture-o').on('click', function() {
        })


        var firstEmoIdx = 1;
        $.emoticonChange(firstEmoIdx);

        $('.emo-body').on('swipeleft', function() {
            var sel = $('.emo-paging .active').index();
            var last =  $('.emo-paging i:last').index();
            if(sel < last) {
                $('.emo-paging .active').removeClass('active');
                $('.emo-paging i').eq(firstEmoIdx).addClass('active');
                ++firstEmoIdx;
                $.emoticonChange(firstEmoIdx);
            }
        })

        $('.emo-body').on('swiperight', function() {
            var sel = $('.emo-paging .active').index();
            var first =  $('.emo-paging i:first').index();
            if(sel > first) {
                --firstEmoIdx;
                $.emoticonChange(firstEmoIdx);
                $('.emo-paging .active').removeClass('active');
                $('.emo-paging i').eq(firstEmoIdx-1).addClass('active');
            }
        })

        $('#emoKind li').on('click', function() {
            firstEmoIdx = 1;
            $('#emoKind li').removeClass('active');
            $(this).addClass('active');
            $('.emo-paging .active').removeClass('active');
            $('.emo-paging i').eq(0).addClass('active');
            $.emoticonChange(firstEmoIdx);
        })

        $('.emo-paging i').on('click', function() {
            var idx = $(this).index();
            $('.emo-paging .active').removeClass('active');
            $('.emo-paging i').eq(idx).addClass('active');
            firstEmoIdx = idx +1;
            $.emoticonChange(firstEmoIdx);
        })


        $('#emoCancel').on('click',function() {
            $(this).prev().removeAttr('src');
            $('.emo-chat').hide('slow');
        })

        $('.fa-smile').on('click', function() {
            $('#emoModal').modal({
                show:true,
                backdrop:false
            })
        })
    })


</script>
</body>
</html>
