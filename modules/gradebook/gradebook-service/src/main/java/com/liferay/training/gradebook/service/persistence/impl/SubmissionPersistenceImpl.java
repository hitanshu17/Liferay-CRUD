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

package com.liferay.training.gradebook.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.training.gradebook.exception.NoSuchSubmissionException;
import com.liferay.training.gradebook.model.Submission;
import com.liferay.training.gradebook.model.impl.SubmissionImpl;
import com.liferay.training.gradebook.model.impl.SubmissionModelImpl;
import com.liferay.training.gradebook.service.persistence.SubmissionPersistence;
import com.liferay.training.gradebook.service.persistence.impl.constants.GradebookPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the submission service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = SubmissionPersistence.class)
public class SubmissionPersistenceImpl
	extends BasePersistenceImpl<Submission> implements SubmissionPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>SubmissionUtil</code> to access the submission persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		SubmissionImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByGroupId;
	private FinderPath _finderPathWithoutPaginationFindByGroupId;
	private FinderPath _finderPathCountByGroupId;

	/**
	 * Returns all the submissions where groupId = &#63;.
	 *s
	 * @param groupId the group ID
	 * @return the matching submissions
	 */
	@Override
	public List<Submission> findByGroupId(long groupId) {
		return findByGroupId(
			groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the submissions where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @return the range of matching submissions
	 */
	@Override
	public List<Submission> findByGroupId(long groupId, int start, int end) {
		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the submissions where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<Submission> orderByComparator) {

		return findByGroupId(groupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the submissions where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<Submission> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroupId;
				finderArgs = new Object[] {groupId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroupId;
			finderArgs = new Object[] {groupId, start, end, orderByComparator};
		}

		List<Submission> list = null;

		if (useFinderCache) {
			list = (List<Submission>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Submission submission : list) {
					if (groupId != submission.getGroupId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				list = (List<Submission>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first submission in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByGroupId_First(
			long groupId, OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByGroupId_First(
			groupId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the first submission in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByGroupId_First(
		long groupId, OrderByComparator<Submission> orderByComparator) {

		List<Submission> list = findByGroupId(groupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last submission in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByGroupId_Last(
			long groupId, OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByGroupId_Last(groupId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the last submission in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByGroupId_Last(
		long groupId, OrderByComparator<Submission> orderByComparator) {

		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<Submission> list = findByGroupId(
			groupId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the submissions before and after the current submission in the ordered set where groupId = &#63;.
	 *
	 * @param submissionId the primary key of the current submission
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next submission
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission[] findByGroupId_PrevAndNext(
			long submissionId, long groupId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = findByPrimaryKey(submissionId);

		Session session = null;

		try {
			session = openSession();

			Submission[] array = new SubmissionImpl[3];

			array[0] = getByGroupId_PrevAndNext(
				session, submission, groupId, orderByComparator, true);

			array[1] = submission;

			array[2] = getByGroupId_PrevAndNext(
				session, submission, groupId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Submission getByGroupId_PrevAndNext(
		Session session, Submission submission, long groupId,
		OrderByComparator<Submission> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_SUBMISSION_WHERE);

		sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(submission)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Submission> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the submissions where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	@Override
	public void removeByGroupId(long groupId) {
		for (Submission submission :
				findByGroupId(
					groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(submission);
		}
	}

	/**
	 * Returns the number of submissions where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching submissions
	 */
	@Override
	public int countByGroupId(long groupId) {
		FinderPath finderPath = _finderPathCountByGroupId;

		Object[] finderArgs = new Object[] {groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 =
		"submission.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByG_A;
	private FinderPath _finderPathWithoutPaginationFindByG_A;
	private FinderPath _finderPathCountByG_A;

	/**
	 * Returns all the submissions where groupId = &#63; and assignmentId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @return the matching submissions
	 */
	@Override
	public List<Submission> findByG_A(long groupId, long assignmentId) {
		return findByG_A(
			groupId, assignmentId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the submissions where groupId = &#63; and assignmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @return the range of matching submissions
	 */
	@Override
	public List<Submission> findByG_A(
		long groupId, long assignmentId, int start, int end) {

		return findByG_A(groupId, assignmentId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the submissions where groupId = &#63; and assignmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByG_A(
		long groupId, long assignmentId, int start, int end,
		OrderByComparator<Submission> orderByComparator) {

		return findByG_A(
			groupId, assignmentId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the submissions where groupId = &#63; and assignmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByG_A(
		long groupId, long assignmentId, int start, int end,
		OrderByComparator<Submission> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByG_A;
				finderArgs = new Object[] {groupId, assignmentId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByG_A;
			finderArgs = new Object[] {
				groupId, assignmentId, start, end, orderByComparator
			};
		}

		List<Submission> list = null;

		if (useFinderCache) {
			list = (List<Submission>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Submission submission : list) {
					if ((groupId != submission.getGroupId()) ||
						(assignmentId != submission.getAssignmentId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_G_A_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_A_ASSIGNMENTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(assignmentId);

				list = (List<Submission>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first submission in the ordered set where groupId = &#63; and assignmentId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByG_A_First(
			long groupId, long assignmentId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByG_A_First(
			groupId, assignmentId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", assignmentId=");
		sb.append(assignmentId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the first submission in the ordered set where groupId = &#63; and assignmentId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByG_A_First(
		long groupId, long assignmentId,
		OrderByComparator<Submission> orderByComparator) {

		List<Submission> list = findByG_A(
			groupId, assignmentId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last submission in the ordered set where groupId = &#63; and assignmentId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByG_A_Last(
			long groupId, long assignmentId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByG_A_Last(
			groupId, assignmentId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", assignmentId=");
		sb.append(assignmentId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the last submission in the ordered set where groupId = &#63; and assignmentId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByG_A_Last(
		long groupId, long assignmentId,
		OrderByComparator<Submission> orderByComparator) {

		int count = countByG_A(groupId, assignmentId);

		if (count == 0) {
			return null;
		}

		List<Submission> list = findByG_A(
			groupId, assignmentId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the submissions before and after the current submission in the ordered set where groupId = &#63; and assignmentId = &#63;.
	 *
	 * @param submissionId the primary key of the current submission
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next submission
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission[] findByG_A_PrevAndNext(
			long submissionId, long groupId, long assignmentId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = findByPrimaryKey(submissionId);

		Session session = null;

		try {
			session = openSession();

			Submission[] array = new SubmissionImpl[3];

			array[0] = getByG_A_PrevAndNext(
				session, submission, groupId, assignmentId, orderByComparator,
				true);

			array[1] = submission;

			array[2] = getByG_A_PrevAndNext(
				session, submission, groupId, assignmentId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Submission getByG_A_PrevAndNext(
		Session session, Submission submission, long groupId, long assignmentId,
		OrderByComparator<Submission> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_SUBMISSION_WHERE);

		sb.append(_FINDER_COLUMN_G_A_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_A_ASSIGNMENTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(assignmentId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(submission)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Submission> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the submissions where groupId = &#63; and assignmentId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 */
	@Override
	public void removeByG_A(long groupId, long assignmentId) {
		for (Submission submission :
				findByG_A(
					groupId, assignmentId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(submission);
		}
	}

	/**
	 * Returns the number of submissions where groupId = &#63; and assignmentId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param assignmentId the assignment ID
	 * @return the number of matching submissions
	 */
	@Override
	public int countByG_A(long groupId, long assignmentId) {
		FinderPath finderPath = _finderPathCountByG_A;

		Object[] finderArgs = new Object[] {groupId, assignmentId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_G_A_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_A_ASSIGNMENTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(assignmentId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_A_GROUPID_2 =
		"submission.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_A_ASSIGNMENTID_2 =
		"submission.assignmentId = ?";

	private FinderPath _finderPathWithPaginationFindByStudentId;
	private FinderPath _finderPathWithoutPaginationFindByStudentId;
	private FinderPath _finderPathCountByStudentId;

	/**
	 * Returns all the submissions where studentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @return the matching submissions
	 */
	@Override
	public List<Submission> findByStudentId(long studentId) {
		return findByStudentId(
			studentId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the submissions where studentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param studentId the student ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @return the range of matching submissions
	 */
	@Override
	public List<Submission> findByStudentId(
		long studentId, int start, int end) {

		return findByStudentId(studentId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the submissions where studentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param studentId the student ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByStudentId(
		long studentId, int start, int end,
		OrderByComparator<Submission> orderByComparator) {

		return findByStudentId(studentId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the submissions where studentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param studentId the student ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByStudentId(
		long studentId, int start, int end,
		OrderByComparator<Submission> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByStudentId;
				finderArgs = new Object[] {studentId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByStudentId;
			finderArgs = new Object[] {
				studentId, start, end, orderByComparator
			};
		}

		List<Submission> list = null;

		if (useFinderCache) {
			list = (List<Submission>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Submission submission : list) {
					if (studentId != submission.getStudentId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_STUDENTID_STUDENTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(studentId);

				list = (List<Submission>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first submission in the ordered set where studentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByStudentId_First(
			long studentId, OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByStudentId_First(
			studentId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("studentId=");
		sb.append(studentId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the first submission in the ordered set where studentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByStudentId_First(
		long studentId, OrderByComparator<Submission> orderByComparator) {

		List<Submission> list = findByStudentId(
			studentId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last submission in the ordered set where studentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByStudentId_Last(
			long studentId, OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByStudentId_Last(
			studentId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("studentId=");
		sb.append(studentId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the last submission in the ordered set where studentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByStudentId_Last(
		long studentId, OrderByComparator<Submission> orderByComparator) {

		int count = countByStudentId(studentId);

		if (count == 0) {
			return null;
		}

		List<Submission> list = findByStudentId(
			studentId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the submissions before and after the current submission in the ordered set where studentId = &#63;.
	 *
	 * @param submissionId the primary key of the current submission
	 * @param studentId the student ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next submission
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission[] findByStudentId_PrevAndNext(
			long submissionId, long studentId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = findByPrimaryKey(submissionId);

		Session session = null;

		try {
			session = openSession();

			Submission[] array = new SubmissionImpl[3];

			array[0] = getByStudentId_PrevAndNext(
				session, submission, studentId, orderByComparator, true);

			array[1] = submission;

			array[2] = getByStudentId_PrevAndNext(
				session, submission, studentId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Submission getByStudentId_PrevAndNext(
		Session session, Submission submission, long studentId,
		OrderByComparator<Submission> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_SUBMISSION_WHERE);

		sb.append(_FINDER_COLUMN_STUDENTID_STUDENTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(studentId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(submission)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Submission> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the submissions where studentId = &#63; from the database.
	 *
	 * @param studentId the student ID
	 */
	@Override
	public void removeByStudentId(long studentId) {
		for (Submission submission :
				findByStudentId(
					studentId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(submission);
		}
	}

	/**
	 * Returns the number of submissions where studentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @return the number of matching submissions
	 */
	@Override
	public int countByStudentId(long studentId) {
		FinderPath finderPath = _finderPathCountByStudentId;

		Object[] finderArgs = new Object[] {studentId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_STUDENTID_STUDENTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(studentId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_STUDENTID_STUDENTID_2 =
		"submission.studentId = ?";

	private FinderPath _finderPathWithPaginationFindByStudentIdAssignmentId;
	private FinderPath _finderPathWithoutPaginationFindByStudentIdAssignmentId;
	private FinderPath _finderPathCountByStudentIdAssignmentId;

	/**
	 * Returns all the submissions where studentId = &#63; and assignmentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @return the matching submissions
	 */
	@Override
	public List<Submission> findByStudentIdAssignmentId(
		long studentId, long assignmentId) {

		return findByStudentIdAssignmentId(
			studentId, assignmentId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the submissions where studentId = &#63; and assignmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @return the range of matching submissions
	 */
	@Override
	public List<Submission> findByStudentIdAssignmentId(
		long studentId, long assignmentId, int start, int end) {

		return findByStudentIdAssignmentId(
			studentId, assignmentId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the submissions where studentId = &#63; and assignmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByStudentIdAssignmentId(
		long studentId, long assignmentId, int start, int end,
		OrderByComparator<Submission> orderByComparator) {

		return findByStudentIdAssignmentId(
			studentId, assignmentId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the submissions where studentId = &#63; and assignmentId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching submissions
	 */
	@Override
	public List<Submission> findByStudentIdAssignmentId(
		long studentId, long assignmentId, int start, int end,
		OrderByComparator<Submission> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByStudentIdAssignmentId;
				finderArgs = new Object[] {studentId, assignmentId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByStudentIdAssignmentId;
			finderArgs = new Object[] {
				studentId, assignmentId, start, end, orderByComparator
			};
		}

		List<Submission> list = null;

		if (useFinderCache) {
			list = (List<Submission>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (Submission submission : list) {
					if ((studentId != submission.getStudentId()) ||
						(assignmentId != submission.getAssignmentId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_STUDENTIDASSIGNMENTID_STUDENTID_2);

			sb.append(_FINDER_COLUMN_STUDENTIDASSIGNMENTID_ASSIGNMENTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(studentId);

				queryPos.add(assignmentId);

				list = (List<Submission>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first submission in the ordered set where studentId = &#63; and assignmentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByStudentIdAssignmentId_First(
			long studentId, long assignmentId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByStudentIdAssignmentId_First(
			studentId, assignmentId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("studentId=");
		sb.append(studentId);

		sb.append(", assignmentId=");
		sb.append(assignmentId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the first submission in the ordered set where studentId = &#63; and assignmentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByStudentIdAssignmentId_First(
		long studentId, long assignmentId,
		OrderByComparator<Submission> orderByComparator) {

		List<Submission> list = findByStudentIdAssignmentId(
			studentId, assignmentId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last submission in the ordered set where studentId = &#63; and assignmentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission
	 * @throws NoSuchSubmissionException if a matching submission could not be found
	 */
	@Override
	public Submission findByStudentIdAssignmentId_Last(
			long studentId, long assignmentId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = fetchByStudentIdAssignmentId_Last(
			studentId, assignmentId, orderByComparator);

		if (submission != null) {
			return submission;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("studentId=");
		sb.append(studentId);

		sb.append(", assignmentId=");
		sb.append(assignmentId);

		sb.append("}");

		throw new NoSuchSubmissionException(sb.toString());
	}

	/**
	 * Returns the last submission in the ordered set where studentId = &#63; and assignmentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching submission, or <code>null</code> if a matching submission could not be found
	 */
	@Override
	public Submission fetchByStudentIdAssignmentId_Last(
		long studentId, long assignmentId,
		OrderByComparator<Submission> orderByComparator) {

		int count = countByStudentIdAssignmentId(studentId, assignmentId);

		if (count == 0) {
			return null;
		}

		List<Submission> list = findByStudentIdAssignmentId(
			studentId, assignmentId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the submissions before and after the current submission in the ordered set where studentId = &#63; and assignmentId = &#63;.
	 *
	 * @param submissionId the primary key of the current submission
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next submission
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission[] findByStudentIdAssignmentId_PrevAndNext(
			long submissionId, long studentId, long assignmentId,
			OrderByComparator<Submission> orderByComparator)
		throws NoSuchSubmissionException {

		Submission submission = findByPrimaryKey(submissionId);

		Session session = null;

		try {
			session = openSession();

			Submission[] array = new SubmissionImpl[3];

			array[0] = getByStudentIdAssignmentId_PrevAndNext(
				session, submission, studentId, assignmentId, orderByComparator,
				true);

			array[1] = submission;

			array[2] = getByStudentIdAssignmentId_PrevAndNext(
				session, submission, studentId, assignmentId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected Submission getByStudentIdAssignmentId_PrevAndNext(
		Session session, Submission submission, long studentId,
		long assignmentId, OrderByComparator<Submission> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_SUBMISSION_WHERE);

		sb.append(_FINDER_COLUMN_STUDENTIDASSIGNMENTID_STUDENTID_2);

		sb.append(_FINDER_COLUMN_STUDENTIDASSIGNMENTID_ASSIGNMENTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SubmissionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(studentId);

		queryPos.add(assignmentId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(submission)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<Submission> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the submissions where studentId = &#63; and assignmentId = &#63; from the database.
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 */
	@Override
	public void removeByStudentIdAssignmentId(
		long studentId, long assignmentId) {

		for (Submission submission :
				findByStudentIdAssignmentId(
					studentId, assignmentId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(submission);
		}
	}

	/**
	 * Returns the number of submissions where studentId = &#63; and assignmentId = &#63;.
	 *
	 * @param studentId the student ID
	 * @param assignmentId the assignment ID
	 * @return the number of matching submissions
	 */
	@Override
	public int countByStudentIdAssignmentId(long studentId, long assignmentId) {
		FinderPath finderPath = _finderPathCountByStudentIdAssignmentId;

		Object[] finderArgs = new Object[] {studentId, assignmentId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SUBMISSION_WHERE);

			sb.append(_FINDER_COLUMN_STUDENTIDASSIGNMENTID_STUDENTID_2);

			sb.append(_FINDER_COLUMN_STUDENTIDASSIGNMENTID_ASSIGNMENTID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(studentId);

				queryPos.add(assignmentId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_STUDENTIDASSIGNMENTID_STUDENTID_2 =
			"submission.studentId = ? AND ";

	private static final String
		_FINDER_COLUMN_STUDENTIDASSIGNMENTID_ASSIGNMENTID_2 =
			"submission.assignmentId = ?";

	public SubmissionPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("comment", "comment_");

		setDBColumnNames(dbColumnNames);

		setModelClass(Submission.class);

		setModelImplClass(SubmissionImpl.class);
		setModelPKClass(long.class);
	}

	/**
	 * Caches the submission in the entity cache if it is enabled.
	 *
	 * @param submission the submission
	 */
	@Override
	public void cacheResult(Submission submission) {
		entityCache.putResult(
			entityCacheEnabled, SubmissionImpl.class,
			submission.getPrimaryKey(), submission);

		submission.resetOriginalValues();
	}

	/**
	 * Caches the submissions in the entity cache if it is enabled.
	 *
	 * @param submissions the submissions
	 */
	@Override
	public void cacheResult(List<Submission> submissions) {
		for (Submission submission : submissions) {
			if (entityCache.getResult(
					entityCacheEnabled, SubmissionImpl.class,
					submission.getPrimaryKey()) == null) {

				cacheResult(submission);
			}
			else {
				submission.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all submissions.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(SubmissionImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the submission.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Submission submission) {
		entityCache.removeResult(
			entityCacheEnabled, SubmissionImpl.class,
			submission.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Submission> submissions) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Submission submission : submissions) {
			entityCache.removeResult(
				entityCacheEnabled, SubmissionImpl.class,
				submission.getPrimaryKey());
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, SubmissionImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new submission with the primary key. Does not add the submission to the database.
	 *
	 * @param submissionId the primary key for the new submission
	 * @return the new submission
	 */
	@Override
	public Submission create(long submissionId) {
		Submission submission = new SubmissionImpl();

		submission.setNew(true);
		submission.setPrimaryKey(submissionId);

		submission.setCompanyId(CompanyThreadLocal.getCompanyId());

		return submission;
	}

	/**
	 * Removes the submission with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param submissionId the primary key of the submission
	 * @return the submission that was removed
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission remove(long submissionId)
		throws NoSuchSubmissionException {

		return remove((Serializable)submissionId);
	}

	/**
	 * Removes the submission with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the submission
	 * @return the submission that was removed
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission remove(Serializable primaryKey)
		throws NoSuchSubmissionException {

		Session session = null;

		try {
			session = openSession();

			Submission submission = (Submission)session.get(
				SubmissionImpl.class, primaryKey);

			if (submission == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchSubmissionException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(submission);
		}
		catch (NoSuchSubmissionException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected Submission removeImpl(Submission submission) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(submission)) {
				submission = (Submission)session.get(
					SubmissionImpl.class, submission.getPrimaryKeyObj());
			}

			if (submission != null) {
				session.delete(submission);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (submission != null) {
			clearCache(submission);
		}

		return submission;
	}

	@Override
	public Submission updateImpl(Submission submission) {
		boolean isNew = submission.isNew();

		if (!(submission instanceof SubmissionModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(submission.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(submission);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in submission proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom Submission implementation " +
					submission.getClass());
		}

		SubmissionModelImpl submissionModelImpl =
			(SubmissionModelImpl)submission;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (submission.getCreateDate() == null)) {
			if (serviceContext == null) {
				submission.setCreateDate(date);
			}
			else {
				submission.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!submissionModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				submission.setModifiedDate(date);
			}
			else {
				submission.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(submission);

				submission.setNew(false);
			}
			else {
				submission = (Submission)session.merge(submission);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!_columnBitmaskEnabled) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {submissionModelImpl.getGroupId()};

			finderCache.removeResult(_finderPathCountByGroupId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByGroupId, args);

			args = new Object[] {
				submissionModelImpl.getGroupId(),
				submissionModelImpl.getAssignmentId()
			};

			finderCache.removeResult(_finderPathCountByG_A, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByG_A, args);

			args = new Object[] {submissionModelImpl.getStudentId()};

			finderCache.removeResult(_finderPathCountByStudentId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByStudentId, args);

			args = new Object[] {
				submissionModelImpl.getStudentId(),
				submissionModelImpl.getAssignmentId()
			};

			finderCache.removeResult(
				_finderPathCountByStudentIdAssignmentId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByStudentIdAssignmentId, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((submissionModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByGroupId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					submissionModelImpl.getOriginalGroupId()
				};

				finderCache.removeResult(_finderPathCountByGroupId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByGroupId, args);

				args = new Object[] {submissionModelImpl.getGroupId()};

				finderCache.removeResult(_finderPathCountByGroupId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByGroupId, args);
			}

			if ((submissionModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByG_A.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					submissionModelImpl.getOriginalGroupId(),
					submissionModelImpl.getOriginalAssignmentId()
				};

				finderCache.removeResult(_finderPathCountByG_A, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_A, args);

				args = new Object[] {
					submissionModelImpl.getGroupId(),
					submissionModelImpl.getAssignmentId()
				};

				finderCache.removeResult(_finderPathCountByG_A, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_A, args);
			}

			if ((submissionModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByStudentId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					submissionModelImpl.getOriginalStudentId()
				};

				finderCache.removeResult(_finderPathCountByStudentId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByStudentId, args);

				args = new Object[] {submissionModelImpl.getStudentId()};

				finderCache.removeResult(_finderPathCountByStudentId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByStudentId, args);
			}

			if ((submissionModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByStudentIdAssignmentId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					submissionModelImpl.getOriginalStudentId(),
					submissionModelImpl.getOriginalAssignmentId()
				};

				finderCache.removeResult(
					_finderPathCountByStudentIdAssignmentId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByStudentIdAssignmentId,
					args);

				args = new Object[] {
					submissionModelImpl.getStudentId(),
					submissionModelImpl.getAssignmentId()
				};

				finderCache.removeResult(
					_finderPathCountByStudentIdAssignmentId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByStudentIdAssignmentId,
					args);
			}
		}

		entityCache.putResult(
			entityCacheEnabled, SubmissionImpl.class,
			submission.getPrimaryKey(), submission, false);

		submission.resetOriginalValues();

		return submission;
	}

	/**
	 * Returns the submission with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the submission
	 * @return the submission
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission findByPrimaryKey(Serializable primaryKey)
		throws NoSuchSubmissionException {

		Submission submission = fetchByPrimaryKey(primaryKey);

		if (submission == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchSubmissionException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return submission;
	}

	/**
	 * Returns the submission with the primary key or throws a <code>NoSuchSubmissionException</code> if it could not be found.
	 *
	 * @param submissionId the primary key of the submission
	 * @return the submission
	 * @throws NoSuchSubmissionException if a submission with the primary key could not be found
	 */
	@Override
	public Submission findByPrimaryKey(long submissionId)
		throws NoSuchSubmissionException {

		return findByPrimaryKey((Serializable)submissionId);
	}

	/**
	 * Returns the submission with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param submissionId the primary key of the submission
	 * @return the submission, or <code>null</code> if a submission with the primary key could not be found
	 */
	@Override
	public Submission fetchByPrimaryKey(long submissionId) {
		return fetchByPrimaryKey((Serializable)submissionId);
	}

	/**
	 * Returns all the submissions.
	 *
	 * @return the submissions
	 */
	@Override
	public List<Submission> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the submissions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @return the range of submissions
	 */
	@Override
	public List<Submission> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the submissions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of submissions
	 */
	@Override
	public List<Submission> findAll(
		int start, int end, OrderByComparator<Submission> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the submissions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SubmissionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of submissions
	 * @param end the upper bound of the range of submissions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of submissions
	 */
	@Override
	public List<Submission> findAll(
		int start, int end, OrderByComparator<Submission> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<Submission> list = null;

		if (useFinderCache) {
			list = (List<Submission>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_SUBMISSION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_SUBMISSION;

				sql = sql.concat(SubmissionModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<Submission>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the submissions from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Submission submission : findAll()) {
			remove(submission);
		}
	}

	/**
	 * Returns the number of submissions.
	 *
	 * @return the number of submissions
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_SUBMISSION);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "submissionId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_SUBMISSION;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return SubmissionModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the submission persistence.
	 */
	@Activate
	public void activate() {
		SubmissionModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		SubmissionModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByGroupId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByGroupId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] {Long.class.getName()},
			SubmissionModelImpl.GROUPID_COLUMN_BITMASK);

		_finderPathCountByGroupId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] {Long.class.getName()});

		_finderPathWithPaginationFindByG_A = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_A",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByG_A = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_A",
			new String[] {Long.class.getName(), Long.class.getName()},
			SubmissionModelImpl.GROUPID_COLUMN_BITMASK |
			SubmissionModelImpl.ASSIGNMENTID_COLUMN_BITMASK);

		_finderPathCountByG_A = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_A",
			new String[] {Long.class.getName(), Long.class.getName()});

		_finderPathWithPaginationFindByStudentId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByStudentId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByStudentId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByStudentId",
			new String[] {Long.class.getName()},
			SubmissionModelImpl.STUDENTID_COLUMN_BITMASK);

		_finderPathCountByStudentId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByStudentId",
			new String[] {Long.class.getName()});

		_finderPathWithPaginationFindByStudentIdAssignmentId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByStudentIdAssignmentId",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByStudentIdAssignmentId =
			new FinderPath(
				entityCacheEnabled, finderCacheEnabled, SubmissionImpl.class,
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByStudentIdAssignmentId",
				new String[] {Long.class.getName(), Long.class.getName()},
				SubmissionModelImpl.STUDENTID_COLUMN_BITMASK |
				SubmissionModelImpl.ASSIGNMENTID_COLUMN_BITMASK);

		_finderPathCountByStudentIdAssignmentId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByStudentIdAssignmentId",
			new String[] {Long.class.getName(), Long.class.getName()});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(SubmissionImpl.class.getName());

		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = GradebookPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.training.gradebook.model.Submission"),
			true);
	}

	@Override
	@Reference(
		target = GradebookPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = GradebookPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_SUBMISSION =
		"SELECT submission FROM Submission submission";

	private static final String _SQL_SELECT_SUBMISSION_WHERE =
		"SELECT submission FROM Submission submission WHERE ";

	private static final String _SQL_COUNT_SUBMISSION =
		"SELECT COUNT(submission) FROM Submission submission";

	private static final String _SQL_COUNT_SUBMISSION_WHERE =
		"SELECT COUNT(submission) FROM Submission submission WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "submission.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No Submission exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No Submission exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		SubmissionPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"comment"});

}