<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Edit album</h1>

<s:include value="/general/errors.jsp" />

<s:form action="album_edit_post" >
    <s:hidden value="album.id" />
    <s:textfield name="album.name" label='Name' />
    <s:textfield name="album.info" label='Info' />
    <s:textfield name="album.releaseDateString" label="Release date" />
    <s:submit value="Update" />
</s:form>
</body>
</html>