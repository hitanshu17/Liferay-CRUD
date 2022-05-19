package com.liferay.training.gradebook.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import java.text.DateFormat;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author hgrahul
 * MVC Render Command For Viewing A Single/Individual Assignment
 * 
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=" + MVCCommandNames.VIEW_ASSIGNMENT
	},
	service = MVCRenderCommand.class
)
public class ViewSingleAssignmentMVCRenderCommand implements MVCRenderCommand{
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		// Need To Get The Assignment Id For Which We Need To Show The Details
		long assignmentId = ParamUtil.getLong(renderRequest, "assignmentId", 0);
		
		try {
			// Get The Detailed Assignment Information Using AssignemntId and AssignmentService
			Assignment assignment = assignmentService.getAssignment(assignmentId);
			
			// Getting A Simple Date Formator For Specific View Experience
			DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat("EEEEE, MMMMM dd, yyyy", renderRequest.getLocale());
			
			// Setting Up The Request With Information Which We Want To Pass To The Associated View
			renderRequest.setAttribute("assignment", assignment);
			renderRequest.setAttribute("dueDate", dateFormat.format(assignment.getDueDate()));
			renderRequest.setAttribute("createDate", dateFormat.format(assignment.getCreateDate()));
			
			// Want To Also Setup A Back Button To Return From Appropriate Individual Assignment View
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
			
			String redirect = renderRequest.getParameter("redirect");
			
			portletDisplay.setShowBackIcon(true);
			portletDisplay.setURLBack(redirect);
			
			// Finally Return The View
			return "/assignment/view_assignment.jsp";
		}
		catch (PortalException e) {
			throw new PortletException(e);
		}
	}
	
	@Reference
	private AssignmentService assignmentService;
}
