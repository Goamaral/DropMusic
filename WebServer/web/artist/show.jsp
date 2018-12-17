<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Artist</h1>
<p><b>Name </b><s:property value="artist.name" /></p>
<p><b>Info </b><s:property value="artist.info" /></p>


<s:if test="%{current_user.isEditor}">
    <p><a href="<s:url action="artist_edit" />?artist_id=<s:property value="artist.id" />">Edit artist</a></p>
    <p><a href="<s:url action="artist_delete" />?artist_id=<s:property value="artist.id" />">Delete artist</a></p>
</s:if>

<a href="<s:url action="artist_index" />">Back</a>

</body>
</html>