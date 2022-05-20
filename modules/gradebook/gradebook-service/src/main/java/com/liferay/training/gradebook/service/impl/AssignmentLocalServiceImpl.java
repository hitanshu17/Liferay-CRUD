/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.training.gradebook.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.base.AssignmentLocalServiceBaseImpl;
import com.liferay.training.gradebook.validator.AssignmentValidator;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author IP
 */
@Component(
	property = "model.class.name=com.liferay.training.gradebook.model.Assignment",
	service = AopService.class
)
public class AssignmentLocalServiceImpl extends AssignmentLocalServiceBaseImpl {
	/**
	 * Adding A New Assignment To The System
	 */
	public Assignment addAssignment(long groupId, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap, Date dueDate, ServiceContext serviceContext) throws PortalException {
		// Perform Assignment Validation Check
		assignmentValidator.validate(titleMap, descriptionMap, dueDate);
		
		// Need To Get Information With Respect User And Site Details
		Group group = groupLocalService.getGroup(groupId);
		long userId = serviceContext.getUserId();
		User user = userLocalService.getUser(userId);
		
		// Generate A New Primary Key For This Assignment
		long assignmentId = counterLocalService.increment(Assignment.class.getName());
		
		// Use The Generated Primary For Create A New Assignment Instance Which Further Be Updated With Appropriate Values
		Assignment assignment = createAssignment(assignmentId);
		
		// Populate The Field Values To The Assignment Instance
		// First Populating Actual Assignment Information
		assignment.setTitleMap(titleMap);
		assignment.setDescriptionMap(descriptionMap);
		assignment.setDueDate(dueDate);
		
		// Now Updating Other Common Field Values
		assignment.setGroupId(groupId);
		assignment.setCompanyId(group.getCompanyId());
		assignment.setCreateDate(serviceContext.getCreateDate(new Date()));
		assignment.setModifiedDate(serviceContext.getModifiedDate(new Date()));
		assignment.setUserId(userId);
		assignment.setUserName(user.getScreenName());
		
		// Persist Assignment Instance To Database And Return It
		assignment = super.addAssignment(assignment);
		
		// Adding A Permissioned Resource Details
		boolean portletActions = false;
		boolean addGroupPermissions = true;
		boolean addGuestPermissions = true;
		
		// The Actual Call For Making A Permissioned Resource With Group, Guest And Portlet Details
		resourceLocalService.addResources(group.getCompanyId(), groupId, userId, Assignment.class.getName(), assignment.getAssignmentId(), portletActions, addGroupPermissions, addGuestPermissions);
		
		// Update Asset Entry
		updateAsset(assignment, serviceContext);
		
		// Finally Return Assignment Instance As Usual
		return assignment;
	}
	
	/**
	 * Introducing A Custom Delete As Due To Asset And Resource Requirement
	 */
	public Assignment deleteAssignment(Assignment assignment) throws PortalException {
		// Delete The Permissioned Resource Before Actual Deletion
		resourceLocalService.deleteResource(assignment, ResourceConstants.SCOPE_INDIVIDUAL);
		
		// For Deleting Asset Entry Information
		assetEntryLocalService.deleteEntry(Assignment.class.getName(), assignment.getAssignmentId());
		
		// Deleting The Actual Assignment
		return super.deleteAssignment(assignment);
	}
	
	/**
	 * For Updating Existing Assignment
	 */
	public Assignment updateAssignment(long assignmentId, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap, Date dueDate, ServiceContext serviceContext) throws PortalException {
		// Perform Assignment Validation Check
		assignmentValidator.validate(titleMap, descriptionMap, dueDate);
		
		// Need To Get The Assignment Details From The Recieved AssignmentId
		Assignment assignment = getAssignment(assignmentId);
		
		// Set The New Details To The Assignment Instance
		assignment.setTitleMap(titleMap);
		assignment.setDescriptionMap(descriptionMap);
		assignment.setDueDate(dueDate);
		
		// Also Update The New Modification Date
		assignment.setModifiedDate(new Date());
		
		// Finally Persist And Return It
		assignment = super.updateAssignment(assignment);
		
		// Update Asset Entry
		updateAsset(assignment, serviceContext);
		
		return assignment;
	}
	
	public List<Assignment> getAssignmentsByGroupId(long groupId) {
		return assignmentPersistence.findByGroupId(groupId);
	}
	
	public List<Assignment> getAssignmentsByGroupId(long groupId, int start, int end) {
		return assignmentPersistence.findByGroupId(groupId, start, end);
	}
	
	public List<Assignment> getAssignmentsByGroupId(long groupId, int start, int end, OrderByComparator<Assignment> orderByComparator) {
		return assignmentPersistence.findByGroupId(groupId, start, end, orderByComparator);
	}
	
	private DynamicQuery getKeywordSearchDynamicQuery(long groupId, String keywords) {
		// Create A DynamicQuery For The Assiociated Group or Website
		DynamicQuery dynamicQuery = dynamicQuery().add(RestrictionsFactoryUtil.eq("groupId", groupId));
		
		if(Validator.isNotNull(keywords)) {
			// If Keywords Are Available For Search Operations. Create A Empty Disjunction Query Which Further Store Query Clauses
			Disjunction disjunctionQuery = RestrictionsFactoryUtil.disjunction();
			
			disjunctionQuery.add(RestrictionsFactoryUtil.like("title", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("description", "%" + keywords + "%"));
			
			// Update Disjunction Query To The Actual DynamicQuery
			dynamicQuery.add(disjunctionQuery);
		}
		
		return dynamicQuery;
	}
	
	public List<Assignment> getAssignmentsByKeywords(long groupId, String keywords, int start, int end, OrderByComparator<Assignment> orderByComparator) {
		return assignmentLocalService.dynamicQuery(getKeywordSearchDynamicQuery(groupId, keywords), start, end, orderByComparator);
	}
	
	public long getAssignmentsCountByKeywords(long groupId, String keywords) {
		return assignmentLocalService.dynamicQueryCount(getKeywordSearchDynamicQuery(groupId, keywords));
	}
	
	@Override
	public Assignment addAssignment(Assignment assignment) {
		throw new UnsupportedOperationException("Not Supported Anymore....");
	}
	
	@Override
	public Assignment updateAssignment(Assignment assignment) {
		throw new UnsupportedOperationException("Not Supported Anymore....");
	}
	
	/**
	 * Updating Assignment Information To The Asset Hierarchy
	 */
	private void updateAsset(Assignment assignment, ServiceContext serviceContext) throws PortalException{
		assetEntryLocalService.updateEntry(serviceContext.getUserId(), serviceContext.getScopeGroupId(), assignment.getCreateDate(), assignment.getModifiedDate(), Assignment.class.getName(), assignment.getAssignmentId(), assignment.getUuid(), 0, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(), true, true, assignment.getCreateDate(), null, null, null, ContentTypes.TEXT_HTML, assignment.getTitle(serviceContext.getLocale()), assignment.getDescription(serviceContext.getLocale()), null, null, null, 0, 0, serviceContext.getAssetPriority());
	}
	
	@Reference
	private AssignmentValidator assignmentValidator;
}