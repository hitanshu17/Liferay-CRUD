package com.liferay.training.gradebook.web.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.BaseManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;
import com.liferay.training.gradebook.web.internal.security.permission.resource.AssignmentTopLevelPermission;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

/**
 * @author IP
 * This Class Passes Contextual Information About The Assignment To The User Interface For Clay Management Toolbar
 * 1. Adding A New Assignment
 * 2. Search Operation
 * 3. Ordering And Filtering
 * 4. Display Style With View Mapping
 */

public class AssignmentsManagementToolbarDisplayContext extends BaseManagementToolbarDisplayContext{
	public AssignmentsManagementToolbarDisplayContext(LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse, HttpServletRequest httpServletRequest) {
		// For Share Assignment or This Assignment Management Toolbar TO Base Implmentation
		super(liferayPortletRequest, liferayPortletResponse, httpServletRequest);
		
		// To Help Us Building Assignment Management Toolbar
		portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(liferayPortletRequest);
		themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
	}
	
	/**
	 * Returns the creation menu for the toolbar
	 * (plus sign on the management toolbar).
	 *
	 * @return creation menu
	 */
	@Override
	public CreationMenu getCreationMenu() {
		if(!AssignmentTopLevelPermission.contains(themeDisplay.getPermissionChecker(), themeDisplay.getScopeGroupId(), "ADD_ENTRY")) {
			return null;
		}
		
		// Creating A New Creation Menu For Adding A New Assignment
		return new CreationMenu() {
			{
				addDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref(liferayPortletResponse.createRenderURL(), "mvcRenderCommandName", MVCCommandNames.EDIT_ASSIGNMENT, "redirect", currentURLObj.toString());
						dropdownItem.setLabel(LanguageUtil.get(request, "add-assignment"));
					}
				);
			}
		};
	}
	
	@Override
	public String getClearResultsURL() {
		return getSearchActionURL();
	}
	
	/**
	 * Returns the assignment list display style. 
	 * 
	 * Current selection is stored in portal preferences.
	 * 
	 * @return current display style
	 */
	@Override
	public String getDisplayStyle() {
		// Getting Style Selection From Clay Management Toolbar Itself
		String displayStyle = ParamUtil.getString(request, "displayStyle");
		
		if(Validator.isNull(displayStyle)) {
			// We Need To Setup A Default Display Style As Its Null
			displayStyle = portalPreferences.getValue(GradebookPortletKeys.PORTLET_NAME, "assignment-display-style", "descriptive");
		}
		else {
			portalPreferences.setValue(GradebookPortletKeys.PORTLET_NAME, "assignment-display-style", displayStyle);
			
			// Need To Ensure The Cache Is Resetted
			request.setAttribute(WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
		}
		
		return displayStyle;
	}
	
	/**
	 * Return Order By Columns
	 */
	@Override
	protected String getOrderByCol() {
		return ParamUtil.getString(request, "orderByCol", "title");
	}
	
	/**
	 * Return Order By Ordering Type
	 */
	@Override
	protected String getOrderByType() {
		return ParamUtil.getString(request, "orderByType", "asc");
	}


	/**
	 * Returns the action URL for the search.
	 *
	 * @return search action URL
	 */
	@Override
	public String getSearchActionURL() {
		// First I Need Is The URL For Search
		PortletURL searchURL = liferayPortletResponse.createRenderURL();
		
		// Setting Up Parameters For Rendering URL
		searchURL.setProperty("mvcRenderCommandName", MVCCommandNames.VIEW_ASSIGNMENTS);
		
		String navigation = ParamUtil.getString(request, "navigation", "entries");
		searchURL.setParameter("navigation", navigation);
		searchURL.setParameter("orderByCol", getOrderByCol());
		searchURL.setParameter("orderByType", getOrderByType());
		
		return searchURL.toString();
	}
	
	/**
	 * Returns the view type options (card, list, table).
	 *
	 * @return list of view types
	 */
	@Override
	public List<ViewTypeItem> getViewTypeItems() {
		// First I Need A URL For View References
		PortletURL portletURL = liferayPortletResponse.createRenderURL();
		portletURL.setProperty("mvcRenderCommandName", MVCCommandNames.VIEW_ASSIGNMENTS);
		
		int delta = ParamUtil.getInteger(request, SearchContainer.DEFAULT_DELTA_PARAM);
		if (delta > 0) {
			portletURL.setParameter("delta", String.valueOf(delta));
		}
		
		String orderByCol = ParamUtil.getString(request, "orderByCol", "title");
		String orderByType = ParamUtil.getString(request, "orderByType", "asc");
		
		portletURL.setParameter("orderByCol", orderByCol);
		portletURL.setParameter("orderByType", orderByType);
		
		int cur = ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM);
		if (cur > 0) {
			portletURL.setParameter("cur", String.valueOf(cur));
		}
		
		return new ViewTypeItemList(portletURL, getDisplayStyle()) {
			{
				addCardViewTypeItem();
				addListViewTypeItem();
				addTableViewTypeItem();
			}
		};
	}
	
	/**
	 * Return the option items for the sort column menu.
	 *
	 * @return options list for the sort column menu
	 */
	@Override
	protected List<DropdownItem> getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive("title".equals(getOrderByCol()));
						dropdownItem.setHref(getCurrentSortingURL(), "orderByCol", "title");
						dropdownItem.setLabel(LanguageUtil.get(request, "title"));
					}
				);
				
				add(
					dropdownItem -> {
						dropdownItem.setActive("createDate".equals(getOrderByCol()));
						dropdownItem.setHref(getCurrentSortingURL(), "orderByCol", "createDate");
						dropdownItem.setLabel(LanguageUtil.get(request, "create-date"));
					}
				);
			}
		};
	}
	
	/**
	 * Returns the current sorting URL.
	 *
	 * @return current sorting portlet URL
	 *
	 * @throws PortletException
	 */
	private PortletURL getCurrentSortingURL() throws PortletException {
		PortletURL sortingURL = PortletURLUtil.clone(currentURLObj, liferayPortletResponse);
		sortingURL.setParameter("mvcRenderCommandName", MVCCommandNames.VIEW_ASSIGNMENTS);
		
		// Reset My View For Current Sorted View
		sortingURL.setParameter(SearchContainer.DEFAULT_CUR_PARAM, "0");
		
		String keywords = ParamUtil.getString(request, "keywords");
		
		if (Validator.isNotNull(keywords)) {
			sortingURL.setParameter("keywords", keywords);
		}
		
		return sortingURL;
	}
	
	private final PortalPreferences portalPreferences;
	private final ThemeDisplay themeDisplay;
}
