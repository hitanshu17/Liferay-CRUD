package com.liferay.training.gradebook.web.portlet.action;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;
import com.liferay.training.gradebook.web.display.context.AssignmentsManagementToolbarDisplayContext;
import com.liferay.training.gradebook.web.internal.security.permission.resource.AssignmentPermission;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author IP
 * This Class Represent View For Showing The Complete List Of Assignments
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=/",
		"mvc.command.name=" + MVCCommandNames.VIEW_ASSIGNMENTS
	},
	service = MVCRenderCommand.class
)
public class ViewAssignmentsMVCRenderCommand implements MVCRenderCommand{
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		// Add Assignment Related Details Or List Information To The Request So That We Can View Appropriately
		addAssignmentListAttributes(renderRequest);
		
		// Add Clay Management Toolbar For Search, Sorting and Filtering Related Purpose
		addManagementToolbarAttributes(renderRequest, renderResponse);
		
		//Adding Permission Related Details To The User Request or RenderRequest
		renderRequest.setAttribute("assignmentPermission", assignmentPermission);
		
		// Return To The JSP View
		return "/view.jsp";
	}
	
	/**
	 * Adds Clay management toolbar context object to the request.
	 * 
	 * @param renderRequest
	 * @param renderResponse
	 */
	private void addManagementToolbarAttributes(RenderRequest renderRequest, RenderResponse renderResponse) {
		LiferayPortletRequest liferayPortletRequest = portal.getLiferayPortletRequest(renderRequest);
		LiferayPortletResponse liferayPortletResponse = portal.getLiferayPortletResponse(renderResponse);
		HttpServletRequest httpServletRequest = portal.getHttpServletRequest(renderRequest);
		
		// Now Here We Will Require To Create And Then Call Constructor For Assignment Clay Management Toolbar
		AssignmentsManagementToolbarDisplayContext assignmentsManagementToolbarDisplayContext = new AssignmentsManagementToolbarDisplayContext(liferayPortletRequest, liferayPortletResponse, httpServletRequest);
		
		// We Have To Set Up The Constructor/Instance To Request So That It Can Be Used By Appropriate Views
		renderRequest.setAttribute("assignmentsManagementToolbarDisplayContext", assignmentsManagementToolbarDisplayContext);
	}
	
	/**
	 * Adds Assignment List Related Attributes To The Request
	 */
	private void addAssignmentListAttributes(RenderRequest renderRequest) {
		// Resolve Start and End Of The Search Container Which Will Be Used As View Or Table To Display Assignment Details
		int currentPage = ParamUtil.getInteger(renderRequest, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_CUR);
		int delta = ParamUtil.getInteger(renderRequest, SearchContainer.DEFAULT_DELTA_PARAM, SearchContainer.DEFAULT_DELTA);
		
		int start = ((currentPage > 0) ? (currentPage - 1) : 0) * delta;
		int end = start + delta;
		
		// Get Sorting Details For Assignment From Search Container
		String orderByCol = ParamUtil.getString(renderRequest, "orderByCol", "title");
		String orderByType = ParamUtil.getString(renderRequest, "orderByType", "asc");
		
		// Create A OrderByComparator Which Will Also Used For Get List Of Assignment
		OrderByComparator<Assignment> comparator = OrderByComparatorFactoryUtil.create("Assignment", orderByCol, !("asc").equals(orderByType));
		
		// Get Keywords If Available
		String keywords = ParamUtil.getString(renderRequest, "keywords");
		
		// Need Group Id For This Process
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long groupId = themeDisplay.getScopeGroupId();

		// Call For AssignmentService To Retrieve List Of Assignments And Count Also
		List<Assignment> assignments = assignmentService.getAssignmentsByKeywords(groupId, keywords, start, end, comparator);
		long assignmentCount = assignmentService.getAssignmentsCountByKeywords(groupId, keywords);
		
		// Assign The Retrived Values To Request So As To Be Used By Your Appropriate View
		renderRequest.setAttribute("assignments", assignments);
		renderRequest.setAttribute("assignmentCount", assignmentCount);
	}
	
	@Reference
	private Portal portal;
	
	@Reference
	private AssignmentService assignmentService;
	
	@Reference
	private AssignmentPermission assignmentPermission;
}
