<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="/general/errors.jsp" />

<div id="container">
    <s:if test="%{users.size() == 0}">
        <p>No normal users available</p>
    </s:if>
    <s:else>
        <s:iterator value="users" var="user" >
            <div class="promote_button" id="<s:property value="#user.id"/>">
                <s:property value="#user.username"/>
                <button>Promote</button>
            </div>
        </s:iterator>
    </s:else>
</div>