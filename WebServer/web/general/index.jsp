<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DropMusic</title>
    </head>

    <body>
        <h1>DropMusic</h1>

        <s:include value="/general/errors.jsp" />

        <p><a href="<s:url action="user_register" />">Register</a></p>
        <p><a href="<s:url action="user_login" />">Login</a></p>
        <p>If you already associated your account with dropbox you can login with dropbox: <a href="<s:url action="dropboxoauth" />">Login with Dropbox</a></p>
    </body>
</html>