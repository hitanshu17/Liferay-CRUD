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

package com.liferay.training.gradebook.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link SubmissionLocalService}.
 *
 * @author hgrahul
 * @see SubmissionLocalService
 * @generated
 */
public class SubmissionLocalServiceWrapper
	implements ServiceWrapper<SubmissionLocalService>, SubmissionLocalService {

	public SubmissionLocalServiceWrapper(
		SubmissionLocalService submissionLocalService) {

		_submissionLocalService = submissionLocalService;
	}

	/**
	 * Adds the submission to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubmissionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param submission the submission
	 * @return the submission that was added
	 */
	@Override
	public com.liferay.training.gradebook.model.Submission addSubmission(
		com.liferay.training.gradebook.model.Submission submission) {

		return _submissionLocalService.addSubmission(submission);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _submissionLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new submission with the primary key. Does not add the submission to the database.
	 *
	 * @param submissionId the primary key for the new submission
	 * @return the new submission
	 */
	@Override
	public com.liferay.training.gradebook.model.Submission createSubmission(
		long submissionId) {

		return _submissionLocalService.createSubmission(submissionId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _submissionLocalService.deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the submission with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubmissionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param submissionId the primary key of the submission
	 * @return the submission that was removed
	 * @throws PortalException if a submission with the primary key could not be found
	 */
	@Override
	public com.liferay.training.gradebook.model.Submission deleteSubmission(
			long submissionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _submissionLocalService.deleteSubmission(submissionId);
	}

	/**
	 * Deletes the submission from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubmissionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param submission the submission
	 * @return the submission that was removed
	 */
	@Override
	public com.liferay.training.gradebook.model.Submission deleteSubmission(
		com.liferay.training.gradebook.model.Submission submission) {

		return _submissionLocalService.deleteSubmission(submission);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _submissionLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _submissionLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.training.gradebook.model.impl.SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _submissionLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.training.gradebook.model.impl.SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _submissionLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _submissionLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _submissionLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.training.gradebook.model.Submission fetchSubmission(
		long submissionId) {

		return _submissionLocalService.fetchSubmission(submissionId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _submissionLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _submissionLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _submissionLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _submissionLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the submission with the primary key.
	 *
	 * @param submissionId the primary key of the submission
	 * @return the submission
	 * @throws PortalException if a submission with the primary key could not be found
	 */
	@Override
	public com.liferay.training.gradebook.model.Submission getSubmission(
			long submissionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _submissionLocalService.getSubmission(submissionId);
	}

	/**
	 * Returns a range of all the submissions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.training.gradebook.model.impl.SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @return the range of submissions
	 */
	@Override
	public java.util.List<com.liferay.training.gradebook.model.Submission>
		getSubmissions(int start, int end) {

		return _submissionLocalService.getSubmissions(start, end);
	}

	/**
	 * Returns the number of submissions.
	 *
	 * @return the number of submissions
	 */
	@Override
	public int getSubmissionsCount() {
		return _submissionLocalService.getSubmissionsCount();
	}

	/**
	 * Updates the submission in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SubmissionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param submission the submission
	 * @return the submission that was updated
	 */
	@Override
	public com.liferay.training.gradebook.model.Submission updateSubmission(
		com.liferay.training.gradebook.model.Submission submission) {

		return _submissionLocalService.updateSubmission(submission);
	}

	@Override
	public SubmissionLocalService getWrappedService() {
		return _submissionLocalService;
	}

	@Override
	public void setWrappedService(
		SubmissionLocalService submissionLocalService) {

		_submissionLocalService = submissionLocalService;
	}

	private SubmissionLocalService _submissionLocalService;

}