package com.example.Project.Management.bayport.fsp.services;



import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProviderCategory;
import com.example.Project.Management.bayport.fsp.models.forms.FspCategoryForm;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 * @author yogona
 * @apiNote FSP is Financial Service Provider
 */

public interface FSPCategoryService {
	/**
	 * Registers FSP Categories
	 * @param fspCategoryForm Carries important fsp category data.
	 * @return FinancialServiceProviderCategory
	 */
	FinancialServiceProviderCategory create(FspCategoryForm fspCategoryForm);

	/**
	 * Retrieves a list of FSP Categories
	 * @return List<FinancialServiceProviderCategory>
	 */
	List<FinancialServiceProviderCategory> getAll();

	/**
     * Retrieves a FSP Category
     *
     * @param id - An id used to find FSP Category by id
     * @return FinancialServiceProviderCategory
     */
	Optional<FinancialServiceProviderCategory> getById(Long id);

	/**
	 * Updates FSP Category
	 * @param fspId An id used to get an FSP
	 * @param fspCategoryForm A form carrying FSP Category Data
	 * @return FinancialServiceProviderCategory
	 */
	FinancialServiceProviderCategory update(Long fspId, FspCategoryForm fspCategoryForm);

	/**
	 * Deletes category but it is kept in the table.
	 * @param fspId ID of the category to be temporarily deleted.
	 * @return FinancialServiceProviderCategory
	 */

	/**
	 * Deletes category and nothing is left.
	 * @param fspId ID of the category to be permanently deleted.
	 */
	void hardDelete(Long fspId);
}