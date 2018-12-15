<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
  </head>

  <body>
    <h1>Login</h1>

    <s:include value="/general/errors.jsp" />

    <s:form action="user_login_post">
      <s:textfield name="user.username" label="Username" />
      <s:password name="user.password" label="Password" />
      <s:submit value="Login"/>
    </s:form>

    <p><a href="<s:url action="general_index" />">Back</a></p>
  </body>
</html>