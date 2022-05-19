package com.liferay.training.gradebook.util.validator;

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.training.gradebook.exception.AssignmentValidationException;
import com.liferay.training.gradebook.validator.AssignmentValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * 
 * @author hgrahul
 * Implementation Logic For Assignment Validation Purpose
 * 
 */
@Component(immediate = true, service = AssignmentValidator.class)
public class AssignmentValidatorImpl implements AssignmentValidator {
	/**
	 * Return <code>true</code> If Title Is Valid For Assignments.
	 * If Not Valid, An Error Message Is Added To The Error List
	 * 
	 * @param titleMap
	 * @param errors
	 * 
	 * @return boolean <code>true</code> If Title Is Valid For Assignment, Otherwise <code>false</code>
	 */
	private boolean isTitleValid(final Map<Locale, String> titleMap, final List<String> errors) {
		boolean result = true;
		
		// Verify Map Data Has Something
		if(MapUtil.isEmpty(titleMap)) {
			errors.add("assignmentTitleEmpty");
			result = false;
		}
		else {
			// Get The Default Locale
			Locale defaultLocale = LocaleUtil.getSiteDefault();
			
			if(Validator.isBlank(titleMap.get(defaultLocale))) {
				errors.add("assignmentTitleEmpty");
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Return <code>true</code> If Description Is Valid For Assignment.
	 * If Not Valid, An Error Message Is Added To The Error List
	 * 
	 * @param descriptionMap
	 * @param errors
	 * 
	 * @return boolean <code>true</code> If Description Is Valid For Assignment, Otherwise <code>false</code>
	 */
	private boolean isDescriptionValid(final Map<Locale, String> descriptionMap, final List<String> errors) {
		boolean result = true;
		
		// Verify That Map Has Something
		if(MapUtil.isEmpty(descriptionMap)) {
			errors.add("assignmentDescriptionEmpty");
			result = false;
		}
		else { 
			// Get The Default Locale
			Locale defaultLocale = LocaleUtil.getSiteDefault();
			
			if(Validator.isBlank(descriptionMap.get(defaultLocale))) {
				errors.add("assignmentDescriptionEmpty");
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Return <code>true</code> If Due Date Is Valid For An Assignment, If Not Valid, An Error Message Is Added To The Error List
	 * Note That This Error Can't Ever Happen In The User Interface Because We Are Always Getting A Default Value On The Controller Layer.
	 * However, This Service Could Be Access Through THE WS API, Which Is Why We Need It.
	 * 
	 * @param dueDate
	 * @param errors
	 * 
	 * @return boolean <code>true</code> If DueDate Is Valid For Assignment, Otherwise <code>false</code>
	 */
	private boolean isDueDateValid(final Date dueDate, final List<String> errors) {
		boolean result = true;

		if (Validator.isNull(dueDate)) {
			errors.add("assignmentDateEmpty");
			result = false;
		}

		return result;
	}	
	
	/**
	 * Returns <code>true</code> If The Given Field Are Valid For An Assignment
	 * 
	 * @param titleMap
	 * @param descriptionMap
	 * @param dueDate
	 * @param errors
	 * @return
	 */
	private boolean isAssignmentValid(final Map<Locale, String> titleMap, final Map<Locale, String> descriptionMap, final Date dueDate, final List<String> errors) {
		boolean result = true;
		
		result &= isTitleValid(titleMap, errors);
		result &= isDescriptionValid(descriptionMap, errors);
		result &= isDueDateValid(dueDate, errors);
		
		return result;
	}
	
	/**
	 * Validates Assignment Values and Throws Relavent Exception (AssignmentValidationException) Is Assignment Values Are Not Valid.
	 * 
	 * @param titleMap
	 * @param descriptionMap
	 * @param dueDate
	 * @throws AssignmentValidationException
	 */
	@Override
	public void validate(Map<Locale, String> titleMap, Map<Locale, String> descriptionMap, Date dueDate) throws AssignmentValidationException {
		List<String> errors = new ArrayList<String>();
		
		if(!isAssignmentValid(titleMap, descriptionMap, dueDate, errors)) {
			throw new AssignmentValidationException(errors);
		}
	}
	
}
