package com.liferay.training.gradebook.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.workflow.kaleo.definition.Assignment;
import com.liferay.training.gradebook.exception.AssignmentValidationException;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author IP
 * This Class Represent Action Command For Editing An Existing Assignment
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=" + MVCCommandNames.EDIT_ASSIGNMENT
	},
	service = MVCActionCommand.class
)
public class EditAssignmentMVCActionCommand extends BaseMVCActionCommand{
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		// As It For Editing Purpose We Need The Assignment Id
		long assignmentId = ParamUtil.getLong(actionRequest, "assignmentId");
		
		// We Also Need Service Context For Our Add Operation
		ServiceContext serviceContext = ServiceContextFactory.getInstance(Assignment.class.getName(), actionRequest);
		
		// Now Getting Information From The Form Elements
		// Use LocalizationUtil to get a localized parameter.
		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(actionRequest, "title");
		Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(actionRequest, "description");
		
		Date dueDate = ParamUtil.getDate(actionRequest, "dueDate", DateFormatFactoryUtil.getDate(actionRequest.getLocale()));
		
		try {
			// Call Up Assignment Service To Update Existing Assignment
			assignmentService.updateAssignment(assignmentId, titleMap, descriptionMap, dueDate, serviceContext);
			
			// Add A New Success Message For : Assignment Updation
			SessionMessages.add(actionRequest, "assignmentUpdated");
			
			// Move Back To Main View
			sendRedirect(actionRequest, actionResponse);
		}
		catch(AssignmentValidationException ave) {
			// ave.printStackTrace();
			
			ave.getErrors().forEach(key -> SessionErrors.add(actionRequest, key));
			actionResponse.setRenderParameter("mvcRenderCommandName", MVCCommandNames.EDIT_ASSIGNMENT);
		}
		catch(PortalException pxe) {
			// pxe.printStackTrace();
			
			SessionErrors.add(actionRequest, "serviceErrorDetails", pxe);
			actionResponse.setRenderParameter("mvcRenderCommandName", MVCCommandNames.EDIT_ASSIGNMENT);
		}
	}
	
	@Reference
	private AssignmentService assignmentService;
}
