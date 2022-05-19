<%@ include file="../init.jsp" %>

<%-- For Error And Related Support --%>
<liferay-ui:error key="serviceErrorDetails">
	<liferay-ui:message key="error.assignment-service-error" arguments='<%= SessionErrors.get(liferayPortletRequest, "serviceErrorDetails") %>' />
</liferay-ui:error>

<liferay-ui:error key="assignmentTitleEmpty" message="error.assignment-title-empty" />
<liferay-ui:error key="assignmentDescriptionEmpty" message="error.assignment-description-empty" />

<%-- Generating Adding and Editing Action URL and Title Based On State Of Assignment Recieved By View --%>
<c:choose>
	<c:when test="${not empty assignment}">
		<portlet:actionURL var="assignmentActionURL" name="<%= MVCCommandNames.EDIT_ASSIGNMENT %>">
			<portlet:param name="redirect" value="${param.redirect}"/>
		</portlet:actionURL>
		<c:set var="editTitle" value="edit-assignment"/>
	</c:when>
	<c:otherwise>
		<portlet:actionURL var="assignmentActionURL" name="<%= MVCCommandNames.ADD_ASSIGNMENT %>">
			<portlet:param name="redirect" value="${param.redirect}"/>
		</portlet:actionURL>
		<c:set var="editTitle" value="add-assignment"/>
	</c:otherwise>
</c:choose>

<%-- To Handle Type and Associated Form Elements --%>
<aui:model-context bean="${assignment}" model="${assignmentClass}"/>

<div class="container-fluid-1280 edit-assignment">
	<h1>
		<liferay-ui:message key="${editTitle}"/>
	</h1>
	
	<aui:form action="${assignmentActionURL}" name="fm">
		<aui:input name="assignmentId" field="assignmentId" type="hidden"/>
		<aui:fieldset-group markupView="lexicon">
			<aui:fieldset>
				<aui:input name="title"/>
				<aui:input name="description"/>
				<aui:input name="dueDate"/>
			</aui:fieldset>
		</aui:fieldset-group>
		
		<%-- Action Button --%>
		<aui:button-row>
			<aui:button cssClass="btn btn-primary" type="submit"/>
			<aui:button cssClass="btn btn-secondary" type="cancel" onClick="${param.redirect}"/>
		</aui:button-row>
	</aui:form>
</div>