package com.dflex.emkopo.app.fsp.services;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderCategory;
import com.dflex.emkopo.app.fsp.models.forms.FspCategoryForm;

import java.util.List;

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
	 * @param id - An id used to find FSP Category by id
	 * @return FinancialServiceProviderCategory
	 */
	FinancialServiceProviderCategory getById(Long id);

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
	FinancialServiceProviderCategory softDelete(Long fspId);

	/**
	 * Deletes category and nothing is left.
	 * @param fspId ID of the category to be permanently deleted.
	 */
	void hardDelete(Long fspId);
}