package com.liferay.training.gradebook.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.exception.NoSuchAssignmentException;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author hgrahul
 * MVC Render Command For Adding or Editing An Assignment
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=" + MVCCommandNames.EDIT_ASSIGNMENT
	},
	service = MVCRenderCommand.class
)
public class EditAssignmentMVCRenderCommand implements MVCRenderCommand{
	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		// Considering This Is The Process For Adding A New Assignment, Assignment Is Empty Or Null
		Assignment assignment = null;
		
		// Get The Assignment Id If The Purpose If For Editing Purpose
		long assignmentId = ParamUtil.getLong(renderRequest, "assignmentId", 0);
		
		// If Assignment Id Is Greater Then Zero - Editing Purpose
		if(assignmentId > 0) {
			try {
				assignment = assignmentService.getAssignment(assignmentId);
			}
			catch (NoSuchAssignmentException nsae) {
				nsae.printStackTrace();
			}
			catch (PortalException pe) {
				pe.printStackTrace();
			}
		}
		
		// Want To Also Setup A Back Button To Return From Appropriate Individual Assignment View
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
									
		String redirect = renderRequest.getParameter("redirect");
									
		portletDisplay.setShowBackIcon(true);
		portletDisplay.setURLBack(redirect);
		
		// Setting Up Assignment Details To The Request For The Appropriate Add or Edit View.
		renderRequest.setAttribute("assignment", assignment);
		renderRequest.setAttribute("assignmentClass", Assignment.class);
		
		// Returning To The Appropriate Views
		return "/assignment/edit_assignment.jsp";
	}
	
	@Reference
	private AssignmentService assignmentService;
}
