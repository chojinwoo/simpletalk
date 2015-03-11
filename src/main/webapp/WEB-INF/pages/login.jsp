<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Simple Web Chatting</title>

    <!-- Bootstrap core CSS -->
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/loginStyles.css" rel="stylesheet">

    <script src="/resources/js/jquery-2.1.3.min.js" type="text/javascript"></script>
    <script src="/resources/js/bootstrap.min.js" type="text/javascript"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<security:authorize access="isAuthenticated()">
    <script>
        window.location.href='/rooms';
    </script>
</security:authorize>
<div class="container">

    <form class="form-signin" action="/login" method="post">
        <h2 class="form-signin-heading">로그인</h2>
        <label for="inputEmail" class="sr-only">아이디</label>
        <input type="text" id="inputEmail" name="id" class="form-control" placeholder="Phone number" required autofocus>
        <label for="inputPassword" class="sr-only">비밀번호</label>
        <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
        <div class="checkbox">
            <label>
                <input type="checkbox" name="remember-me"> 기억하기
            </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
        <c:if test="${not empty regId}">
            <c:if test="${not empty phoneNum}">
                <button class="btn btn-lg btn-info btn-block" type="button" data-toggle="modal" data-target="#registerModal">회원가입</button>
            </c:if>
        </c:if>
    </form>
    <div class="hide">
        ${SPRING_SECURITY_LAST_EXCEPTION}
    </div>

    <!-- Register -->
    <div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="registerModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="registerForm" action="/register" method="post">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="registerModalLabel">회원가입</h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="url"/>
                        <input type="hidden" name="regId" value="${regId}"/>
                        <input type="hidden" name="phoneNum" value="${phoneNum}"/>
                        <div class="form-group">
                            <label for="registeId">아이디</label>
                            <input class="form-control" type="text" id="registeId" name="id">
                        </div>
                        <div class="form-group">
                            <label for="registerPassword">비밀번호</label>
                            <input class="form-control" type="password" id="registerPassword" name="password">
                        </div>
                        <div class="form-group">
                            <label for="registerRePassword">비밀번호 확인</label>
                            <input class="form-control" type="password" id="registerRePassword" name="rePassword">
                        </div>
                        <div class="form-group">
                            <label for="registerName">이름</label>
                            <input class="form-control" type="text" id="registerName" name="name">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
                        <button type="button" id="registerBtn" class="btn btn-primary">가입</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script>
        $(document).ready(function() {
            $('#registerBtn').on('click',function() {
                $.fn.inputMsg = function(msg) {
                    $(this).css('border', '1px solid red');
                    $(this).prev('label').append($('<span>').css({'padding-left':'5px', 'color':'red'}).text(msg));

                }

                $.fn.inputReset = function() {
                    $(this).css('border', '1px solid #eee');
                    $(this).prev('label').find('span').remove();
                }

                $.each($('#registerForm input:not(input[type=hidden])'), function() {
                    if($(this).val() == "") {
                        var target = $(this).prev('label').text();
                        $(this).inputMsg("는(은) 필수조건입니다.");
                        return false;
                    } else {
                        $(this).inputReset();
                    }
                })

                if($('#registerPassword').val() != $('#registerRePassword').val()) {
                    $('#registerPassword, #registerRePassword').inputMsg("비밀번호가 일치 하지 않습니다.");
                    return false;
                }

                $.ajax({
                    url:"/register",
                    type:'post',
                    data:$('#registerForm').serialize(),
                    success:function(data) {
                        alert(data);
                    }
                })
            })
        })
    </script>
</div> <!-- /container -->
</body>
</html>