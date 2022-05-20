package com.liferay.training.gradebook.web.asset.model;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.asset.util.AssetHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;
import com.liferay.training.gradebook.web.internal.security.permission.resource.AssignmentPermission;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author IP
 * This Class Represents Asset Rendering Related To Assignment Entity
 */
public class AssignmentAssetRenderer extends BaseJSPAssetRenderer<Assignment>{
	/**
	 * Constructor : About What Need To Be Rendered
	 */
	public AssignmentAssetRenderer(Assignment assignment) {
		this.assignment = assignment;
	}
	

	/**
	 * Provide All The Metadata Information For Asset Rendering Purpose
	 */
	@Override
	public Assignment getAssetObject() {
		return assignment;
	}
	
	@Override
	public String getClassName() {
		return Assignment.class.getName();
	}
	
	@Override
	public long getClassPK() {
		return assignment.getAssignmentId();
	}
	
	@Override
	public long getGroupId() {
		return assignment.getGroupId();
	}
	
	@Override
	public int getStatus() {
		return assignment.getStatus();
	}
	
	@Override
	public long getUserId() {
		return assignment.getUserId();
	}
	
	@Override
	public String getUserName() {
		return assignment.getUserName();
	}
	
	@Override
	public String getUuid() {
		return assignment.getUuid();
	}
	
	@Override
	public String getTitle(Locale locale) {
		return assignment.getTitle(locale);
	}
	
	/**
	 * Providing Permission Level Details For This Assignment Related Work
	 */
	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker) throws PortalException {
		return AssignmentPermission.contains(permissionChecker, assignment, ActionKeys.UPDATE);
	}
	
	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker) throws PortalException {
		return AssignmentPermission.contains(permissionChecker, assignment, ActionKeys.VIEW);
	}
	
	/**
	 * For Including The Custom Entity For The Asset Hierarchy Purpose
	 */
	@Override
	public boolean include(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String template) throws Exception {
		httpServletRequest.setAttribute("assignment", assignment);
		return super.include(httpServletRequest, httpServletResponse, template);
	}
	
	// Setting Up The Asset Page Friendly URL
	public void setAssetDisplayPageFriendlyURLProvider(AssetDisplayPageFriendlyURLProvider assetDisplayPageFriendlyURLProvider) {
		this.assetDisplayPageFriendlyURLProvider = assetDisplayPageFriendlyURLProvider;
	}
	
	/**
	 *  Association With View's Appropriately
	 */
	@Override
	public String getURLView(LiferayPortletResponse liferayPortletResponse, WindowState windowState) throws Exception {
		System.out.println("Normal View");
		return super.getURLView(liferayPortletResponse, windowState);
	}
	
	@Override
	public String getURLViewInContext(LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse, String noSuchEntryRedirect) throws Exception {
		if(assetDisplayPageFriendlyURLProvider != null) {
			ThemeDisplay themeDisplay = (ThemeDisplay) liferayPortletRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String friendlyURL = assetDisplayPageFriendlyURLProvider.getFriendlyURL(getClassName(), getClassPK(), themeDisplay);
			
			if(Validator.isNotNull(friendlyURL)) {
				return friendlyURL;
			}
		}
		
		// As We Now Here We Do Not Have Friendly Or Any URL
		try {
			long plid = PortalUtil.getPlidFromPortletId(assignment.getGroupId(), GradebookPortletKeys.PORTLET_NAME);
			PortletURL portletURL;
			
			if(plid == LayoutConstants.DEFAULT_PLID) {
				portletURL = liferayPortletResponse.createLiferayPortletURL(getControlPanelPlid(liferayPortletRequest), GradebookPortletKeys.PORTLET_NAME, PortletRequest.RENDER_PHASE);
			}
			else {
				portletURL = PortletURLFactoryUtil.getPortletURLFactory().create(liferayPortletRequest, GradebookPortletKeys.PORTLET_NAME, PortletRequest.RENDER_PHASE);
			}
			
			// Setup Parameter Details For Viewing Purpose
			portletURL.setParameter("mvcRenderCommandName", MVCCommandNames.VIEW_ASSIGNMENT);
			portletURL.setParameter("assignmentId", String.valueOf(assignment.getAssignmentId()));
						
			String currentURL = PortalUtil.getCurrentURL(liferayPortletRequest);
			portletURL.setParameter("redirect", currentURL);
			
			return portletURL.toString();
		}
		catch (PortalException pe) {
		}
		catch (SystemException se) {
		}
		return null;
	}
	
	/**
	 * Edting Purpose	
	 */
	@Override
	public PortletURL getURLEdit(LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse) throws Exception {
		// Need Site Or Group Details
		Group group = GroupLocalServiceUtil.fetchCompanyGroup(assignment.getGroupId());
 
		// Check Whether The Group or Site Is The Main Portal
		if(group.isCompany()) {
			ThemeDisplay themeDisplay = (ThemeDisplay) liferayPortletRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long groupId = themeDisplay.getScopeGroupId();
		}
		
		PortletURL portletURL = PortalUtil.getControlPanelPortletURL(liferayPortletRequest, group, GradebookPortletKeys.PORTLET_NAME, 0, 0, PortletRequest.RENDER_PHASE);
		
		// Setup Parameter Details For Editing Purpose
		portletURL.setParameter("mvcRenderCommandName", MVCCommandNames.EDIT_ASSIGNMENT);
		portletURL.setParameter("assignmentId", String.valueOf(assignment.getAssignmentId()));
				
		portletURL.setParameter("showback", Boolean.FALSE.toString());
		
		return portletURL;
	}
	
	/**
	 * Asset Summary - Targetting Show The Other Important Details About The Custom Entity (Description)
	 */
	@Override
	public String getSummary(PortletRequest portletRequest, PortletResponse portletResponse) {
		ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		int abstractLength = AssetHelper.ASSET_ENTRY_ABSTRACT_LENGTH;
		
		String summary = HtmlUtil.stripHtml(
			StringUtil.shorten(assignment.getDescription(themeDisplay.getLocale()), abstractLength)
		);
		
		return summary;
	}
	
	@Override
	public String getJspPath(HttpServletRequest httpServletRequest, String template) {
		if(template.contentEquals(TEMPLATE_ABSTRACT) || template.contentEquals(TEMPLATE_FULL_CONTENT)) {
			return "/asset/" + template + ".jsp";
		}
		
		return null;
	}
	
	
	private Assignment assignment;
	private AssetDisplayPageFriendlyURLProvider assetDisplayPageFriendlyURLProvider;
}
