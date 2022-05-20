package com.liferay.training.gradebook.internal.search;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.registrar.ModelSearchRegistrarHelper;
import com.liferay.portal.search.spi.model.result.contributor.ModelSummaryContributor;
import com.liferay.training.gradebook.model.Assignment;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author IP
 * This Class Register's Assignment Entity And Search Framework
 */
@Component(immediate = true)
public class AssignmentSearchRegistrar {
	@Activate
	protected void activate(BundleContext bundleContext) {
		serviceRegistration = modelSearchRegistrarHelper.register(Assignment.class, bundleContext, 
			modelSearchDefination -> {
				modelSearchDefination.setDefaultSelectedFieldNames(Field.ASSET_TAG_NAMES, Field.COMPANY_ID, Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK, Field.GROUP_ID, Field.MODIFIED_DATE, Field.SCOPE_GROUP_ID, Field.UID);
				modelSearchDefination.setDefaultSelectedLocalizedFieldNames(Field.DESCRIPTION, Field.TITLE);
				modelSearchDefination.setModelIndexWriteContributor(modelIndexerWriterContributor);
				modelSearchDefination.setModelSummaryContributor(modelSummaryContributor);
			}
		);
	}
	
	@Deactivate
	protected void deactivate() {
		serviceRegistration.unregister();
	}
	
	
	/**
	 * Reference Which I Need For Search and Indexing Related Operation
	 */
	@Reference(target = "(indexer.class.name=com.liferay.training.gradebook.model.Assignment)")
	protected ModelIndexerWriterContributor<Assignment> modelIndexerWriterContributor;
	
	@Reference(target = "(indexer.class.name=com.liferay.training.gradebook.model.Assignment)")
	private ModelSummaryContributor modelSummaryContributor;
	
	@Reference
	private ModelSearchRegistrarHelper modelSearchRegistrarHelper;
	
	private ServiceRegistration<?> serviceRegistration;
}
