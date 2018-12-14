<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="%{errors.size() != 0}">
    <ul>
        <s:iterator value="errors">
            <li><s:property /></li>
        </s:iterator>
    </ul>
</s:if>