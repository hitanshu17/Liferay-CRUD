package com.liferay.training.gradebook.web.asset.model;

import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentLocalService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author hgrahul
 * This Class Represent or Takes Care of Asset Rendering and Handling The Renderer Itself
 */
@Component(immediate = true, property = "javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME, service = AssetRendererFactory.class)
public class AssignmentAssetRendererFactory extends BaseAssetRendererFactory<Assignment>{
	public static final String CLASS_NAME = Assignment.class.getName();
	public static final String TYPE = "assignment";
	
	public AssignmentAssetRendererFactory() {
		setClassName(CLASS_NAME);
		setLinkable(true);
		setPortletId(GradebookPortletKeys.PORTLET_NAME);
		setSearchable(true);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AssetRenderer<Assignment> getAssetRenderer(long assignmentId, int type) throws PortalException {
		// We Need Assignment Related Information For Asset Rendering Operation
		Assignment assignment = assignmentLocalService.getAssignment(assignmentId);
		
		// Pass This Assignment Related Details To The Asset Render For Being Rendered
		AssignmentAssetRenderer assignmentAssetRenderer = new AssignmentAssetRenderer(assignment);
		
		assignmentAssetRenderer.setAssetDisplayPageFriendlyURLProvider(assetDisplayPageFriendlyURLProvider);
		assignmentAssetRenderer.setAssetRendererType(type);
		assignmentAssetRenderer.setServletContext(servletContext);
		
		return assignmentAssetRenderer;
	}
	
	/**
	 * URL Viewing and Adding Related
	 */
	@Override
	public PortletURL getURLView(LiferayPortletResponse liferayPortletResponse, WindowState windowState) throws PortalException {
		LiferayPortletURL liferayPortletURL = liferayPortletResponse.createLiferayPortletURL(GradebookPortletKeys.PORTLET_NAME, PortletRequest.RENDER_PHASE);
		
		try {
			liferayPortletURL.setWindowState(windowState);
		}
		catch (WindowStateException e) {
			// TODO: handle exception
		}
		
		return liferayPortletURL;
	}
	
	@Override
	public PortletURL getURLAdd(LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse, long classTypeId) throws PortalException {
		PortletURL portletURL = portal.getControlPanelPortletURL(liferayPortletRequest, GradebookPortletKeys.PORTLET_NAME,PortletRequest.RENDER_PHASE);
		portletURL.setParameter("mvcRenderCommandName", MVCCommandNames.EDIT_ASSIGNMENT);
		
		return portletURL;
	}
	
	/**
	 * Define All The Permission Checker Details
	 */
	@Override
	public boolean hasPermission(PermissionChecker permissionChecker, long assignmentId, String actionId) throws Exception {
		return assignmentModelResourcePermission.contains(permissionChecker, assignmentId, actionId);
	}
	
	@Override
	public boolean hasAddPermission(PermissionChecker permissionChecker, long groupId, long classTypeId) throws Exception {
		return portletResourcePermission.contains(permissionChecker, groupId, ActionKeys.ADD_ENTRY);
	}

	/**
	 * Getting All The References Which We Will Need Here
	 */
	@Reference
	private AssignmentLocalService assignmentLocalService;
	
	@Reference
	private AssetDisplayPageFriendlyURLProvider assetDisplayPageFriendlyURLProvider;
	
	@Reference(target = "(model.class.name=com.liferay.training.gradebook.model.Assignment)")
	private ModelResourcePermission<Assignment> assignmentModelResourcePermission;
	
	@Reference(target = "(resource.name=com.liferay.training.gradebook.model)")
	private PortletResourcePermission portletResourcePermission;
	
	@Reference
	private Portal portal;
	
	@Reference
	private PortletURLFactory portletURLFactory;
	
	@Reference(target = "(osgi.web.symbolicname=com.liferay.training.gradebook.web)")
	private ServletContext servletContext;
}
