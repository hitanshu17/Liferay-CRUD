package com.liferay.training.gradebook.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author IP
 * The Class Represent Process Action For Deleting An Assignment
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=" + MVCCommandNames.DELETE_ASSIGNMENT
	},
	service = MVCActionCommand.class
)
public class DeleteAssignmentMVCActionCommand extends BaseMVCActionCommand{
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		// Get The Assignnent Id Which Needs To Be Deleted
		long assignmentId = ParamUtil.getLong(actionRequest, "assignmentId");
		
		try {
			// Call For AssignmentService To Delete The Assignment
			assignmentService.deleteAssignment(assignmentId);
			
			// Add A New Success Message For : Assignment Deletion
			SessionMessages.add(actionRequest, "assignmentDeleted");
		}
		catch (PortalException pe) {
			pe.printStackTrace();
		}
	}
	
	@Reference
	private AssignmentService assignmentService;
}
