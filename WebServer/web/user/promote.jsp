<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<s:include value="/websocket.jsp" />

<h1>Promote user to editor</h1>

<s:include value="/user/partials/promote.jsp" />

<p><a href="<s:url action="user_dashboard" />">Back</a></p>

<script>
    window.onload = function() {
        $('.promote_button').click(function () {
            $.post("<s:url action="user_promote_post" />", { id: $(this).attr('id') }, function(data) {
                $('#container').html(data)
            })
        })
    }
</script>
</body>
</html>