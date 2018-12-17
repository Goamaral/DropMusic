<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<s:include value="/websocket.jsp" />

<h1>Create artist</h1>

<s:include value="/general/errors.jsp" />

<s:form action="artist_create_post">
    <s:textfield name="artist.name" label="Name" />
    <s:textfield name="artist.info" label="Info" />
    <s:submit value="Create"/>
</s:form>

<a href="<s:url action="artist_index" />">Back</a>
</body>
</html>