package com.liferay.training.gradebook.web.internal.security.permission.resource;

import com.liferay.headless.commerce.bom.dto.v1_0.Model;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.training.gradebook.model.Assignment;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author hgrahul
 * This Class Represents Class For Checking Entity Permissions
 */
@Component(immediate = true, service = AssignmentPermission.class)
public class AssignmentPermission {
	public static boolean contains(PermissionChecker permissionChecker, long assignmentId, String actionId) throws PortalException {
		return assignmentModelResourcePermission.contains(permissionChecker, assignmentId, actionId);
	}
	
	public static boolean contains(PermissionChecker permissionChecker, Assignment assignment, String actionId) throws PortalException {
		return assignmentModelResourcePermission.contains(permissionChecker, assignment, actionId);
	}
	
	@Reference(target = "(model.class.name=com.liferay.training.gradebook.model.Assignment)", unbind = "-")
	protected void setModelResourcePermission(ModelResourcePermission<Assignment> modelResourcePermission) {
		assignmentModelResourcePermission = modelResourcePermission;
	}
	
	private static ModelResourcePermission<Assignment> assignmentModelResourcePermission;
}