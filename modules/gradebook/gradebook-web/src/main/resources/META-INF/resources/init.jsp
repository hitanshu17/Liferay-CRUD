<%-- JSTL or FMT Core Library --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Liferay Portlet Tag Library --%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%-- Liferay Standard Tag Library --%>
<%@ taglib prefix="aui" uri="http://liferay.com/tld/aui" %>
<%@ taglib prefix="clay" uri="http://liferay.com/tld/clay" %>
<%@ taglib prefix="liferay-item-selector" uri="http://liferay.com/tld/item-selector" %>
<%@ taglib prefix="liferay-frontend" uri="http://liferay.com/tld/frontend" %>
<%@ taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet" %>
<%@ taglib prefix="liferay-theme" uri="http://liferay.com/tld/theme" %>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>

<%-- Security Tag Library --%>
<%@ taglib prefix="liferay-security" uri="http://liferay.com/tld/security" %>

<%-- Specific Set Of Class Which We Need In Our Gradebook Web Portlet Demo --%>
<%@ page import="java.util.Date"%>
<%@ page import="javax.portlet.WindowState"%>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil"%>

<%-- If Showing Error, It Means Gradebook Is Not Connect With Gradebook API --%>
<%@ page import="com.liferay.training.gradebook.model.Assignment"%>

<%-- If Showing Error, Below Said Class Is Not Yet Created In Gradebook Web Portlet --%>
<%@ page import="com.liferay.training.gradebook.web.constants.MVCCommandNames"%>

<%-- For Showing Error Message In User Interface --%>
<%@ page import="com.liferay.portal.kernel.servlet.SessionErrors"%>

<%-- Asset Related Imports --%>
<%@page import="com.liferay.asset.kernel.model.AssetRenderer" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>

<%-- Liferay Standard Implicit Object Addition To The User Interface --%>
<liferay-frontend:defineObjects/>

<portlet:defineObjects/>

<liferay-theme:defineObjects/>