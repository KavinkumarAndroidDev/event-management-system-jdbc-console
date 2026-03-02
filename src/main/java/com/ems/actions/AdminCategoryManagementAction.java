package com.ems.actions;

import java.util.List;

import com.ems.exception.DataAccessException;
import com.ems.model.Category;
import com.ems.service.AdminService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

/**
 * 
 */
public class AdminCategoryManagementAction {
	private final AdminService adminService;

	public AdminCategoryManagementAction() {
		this.adminService = ApplicationUtil.adminService();
	}

	/**
	 * prints all the categories
	 */
	public void listAllCategories() {
		try {
			List<Category> categories = adminService.getAllCategories();
			if (categories.isEmpty()) {
				System.out.println("No categories found.");
				return;
			}
			PaginationUtil.paginate(categories, AdminMenuHelper::printCategories);
		} catch (DataAccessException e) {
			System.out.println("Error retrieving categories: " + e.getMessage());
		}
	}

	/**
	 * Gets user input and add a new category
	 * Category name must be between 3 and 30 characters
	 */
	public void addCategory() {
		String name = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter category name: ");
		while (name.length() < 3 || name.length() > 30) {
			name = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(),
					"Enter category name (3 - 30 characters): ");
		}
		try {
			adminService.addCategory(name.trim());
			System.out.println("Category added successfully.");
		} catch (DataAccessException e) {
			System.out.println("Error adding category: " + e.getMessage());
		}
	}

	/**
	 * It prints the existing categories
	 * user select a category to update
	 */
	public void updateCategory() {
		try {
			Category selectedCategory = selectCategory();
			if (selectedCategory == null)
				return;

			if (selectedCategory.getIsActive() == 1) {
				String newName = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(),
						"Enter new category name: ");
				adminService.updateCategory(selectedCategory.getCategoryId(), newName);

			} else {
				char confirm = InputValidationUtil.readChar(ScannerUtil.getScanner(),
						"Are you sure you want to activate this category (Y/N): ");

				if (Character.toUpperCase(confirm) != 'Y') {
					System.out.println("Category updation cancelled.");
					return;

				}
				adminService.updateCategory(selectedCategory.getCategoryId());
			}
			System.out.println("Category updated successfully");
		} catch (DataAccessException e) {
			System.out.println("Error updating category: " + e.getMessage());
		}
	}

	/**
	 * Prints all active category and admin select any
	 * and the category is deactivated, not completely delted
	 */
	public void deleteCategory() {
		try {
			Category selectedCategory = selectCategory();
			if (selectedCategory == null)
				return;

			if (selectedCategory.getIsActive() == 1) {
				char confirm = InputValidationUtil.readChar(ScannerUtil.getScanner(),
						"Are you sure you want to delete this category (Y/N): ");

				if (Character.toUpperCase(confirm) != 'Y') {
					System.out.println("Category deletion cancelled.");
					return;
				}
				adminService.deleteCategory(selectedCategory.getCategoryId());
				System.out.println("Category deleted successfully");
			} else {
				System.out.println("The category is already deleted");
			}
		} catch (DataAccessException e) {
			System.out.println("Error deleting category: " + e.getMessage());
		}
	}

	/**
	 * It returns the list of all available categories
	 */
	public List<Category> getAllCategories() throws DataAccessException {
		List<Category> categories = adminService.getAllCategories();
		return categories;
	}

	/*
	 * Helper function to display the categories
	 */
	private Category selectCategory() throws DataAccessException {

		List<Category> categories = getAllCategories();

		if (categories.isEmpty()) {
			System.out.println("No categories found.");
			return null;
		}

		PaginationUtil.paginate(categories, AdminMenuHelper::printCategories);

		int choice = InputValidationUtil.readInt(
				ScannerUtil.getScanner(),
				"Select a category (1-" + categories.size() + "): ");

		while (choice < 1 || choice > categories.size()) {
			choice = InputValidationUtil.readInt(
					ScannerUtil.getScanner(),
					"Enter a valid choice: ");
		}

		return categories.get(choice - 1);
	}

}
