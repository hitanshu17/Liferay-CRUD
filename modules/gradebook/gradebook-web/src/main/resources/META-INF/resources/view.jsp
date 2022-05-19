<%@ include file="init.jsp" %>

<%-- Details About Error And Success Message --%>
<liferay-ui:error key="serviceErrorDetails">
	<liferay-ui:message arguments='<%= SessionErrors.get(liferayPortletRequest, "serviceErrorDetails") %>' key="error.assignment-service-error" />
</liferay-ui:error>

<liferay-ui:success key="assignmentAdded" message="assignment-added-successfully" />
<liferay-ui:success key="assignmentUpdated" message="assignment-updated-successfully" />
<liferay-ui:success key="assignmentDeleted" message="assignment-deleted-successfully" />

<div class="container-fluid-1280">
	<h1>
		<liferay-ui:message key="assignments"/>
	</h1>
	
	<%-- Clay Management Toolbar For My Assignment --%>
	<clay:management-toolbar disabled="${assignmentCount eq 0}" displayContext="${assignmentsManagementToolbarDisplayContext}" itemsTotal="${asssignmentCount}" searchContainerId="assignmentEntries" selectable="false"/>
	
	<%-- Search Container - For The Assignment Clay Management Toolbar --%>
	<liferay-ui:search-container id="assignmentEntries" emptyResultsMessage="no-assignments" total="${assignmentCount}" iteratorURL="${portletURL}">
		
		<%-- Need To Get Result For The Search Container From The Assignment Rendered --%>
		<liferay-ui:search-container-results results="${assignments}"/>
		
		<%-- To Show It Up In A Appropriate Model View And Different View Forms --%>
		<liferay-ui:search-container-row className="com.liferay.training.gradebook.model.Assignment" modelVar="entry">
			
			<%-- To Handle Different Display View(Card|Table|List) We Are Doing It In A Separate JSP --%>
			<%@include file="/assignment/entry_search_columns.jspf" %>
			
		</liferay-ui:search-container-row>
		
		<%-- For Interation And Paging --%>
		<liferay-ui:search-iterator displayStyle="${assignmentsManagementToolbarDisplayContext.getDisplayStyle()}" markupView="lexicon"/>
	</liferay-ui:search-container>
</div>