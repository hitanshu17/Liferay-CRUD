package com.liferay.training.gradebook.web.internal.security.permission.resource;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author IP
 * This Class Represent As A Helper Class In Portlet Module For Checking Top-Level Permission
 */
@Component(immediate = true)
public class AssignmentTopLevelPermission {
	public static boolean contains(PermissionChecker permissionChecker, long groupId, String actionId) {
		return assignmentPortletResourcePermission.contains(permissionChecker, groupId, actionId);
	}
	
	@Reference(target = "(resource.name=com.liferay.training.gradebook.model)", unbind = "-")
	protected void setPortletResourcePermission(PortletResourcePermission portletResourcePermission) {
		assignmentPortletResourcePermission = portletResourcePermission;
	}
	
	private static PortletResourcePermission assignmentPortletResourcePermission;
}
