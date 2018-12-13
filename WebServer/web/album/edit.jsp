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
<s:form action="album_edit_post" >
    <s:textfield value="album.name" label='Name(%{album.name})' />
    <s:textfield value="album.info" label='Info(%{album.info})' />
    <s:textfield value="album.releaseDateString" label="Release date(%{album.releaseDateString})" />
    <s:submit value="Update" />
</s:form>
</body>
</html>