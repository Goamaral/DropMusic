<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Edit artist</h1>

<s:include value="/general/errors.jsp" />

<s:form action="artist_edit_post" >
    <s:hidden name="artist.id" />
    <s:textfield name="artist.name" label='Name' />
    <s:textfield name="artist.info" label='Info' />
    <s:submit value="Update" />
</s:form>

<a href="<s:url action="artist_show" />?artist_id=<s:property value="artist.id" />">Back</a>
</body>
</html>