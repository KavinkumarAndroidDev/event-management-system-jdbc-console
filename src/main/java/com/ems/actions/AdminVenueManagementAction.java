package com.ems.actions;

import java.util.List;

import com.ems.exception.DataAccessException;
import com.ems.model.Event;
import com.ems.model.Venue;
import com.ems.service.AdminService;
import com.ems.service.EventService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

public class AdminVenueManagementAction {
	private final AdminService adminService;
	private final EventService eventService;

	public AdminVenueManagementAction() {
		this.adminService = ApplicationUtil.adminService();
		this.eventService = ApplicationUtil.eventService();
	}

	public List<Venue> getAllVenues() throws DataAccessException {
		return eventService.getAllVenues();
	}

	public void addVenue() {
		Venue venue = new Venue();
		String venueName;
		while (true) {
			venueName = InputValidationUtil.readNonEmptyString(
					ScannerUtil.getScanner(), "Enter the venue name (3 - 100 characters): ");
			if (venueName.length() >= 3 && venueName.length() <= 100) {
				venue.setName(venueName);
				break;
			}
			System.out.println("Venue name must be between 3 and 100 characters.");
		}

		String street;
		while (true) {
			street = InputValidationUtil.readNonEmptyString(
					ScannerUtil.getScanner(), "Enter the street name (3 - 100 characters): ");
			if (street.length() >= 3 && street.length() <= 100) {
				venue.setStreet(street);
				break;
			}
			System.out.println("Street name must be between 3 and 100 characters.");
		}

		String city;
		while (true) {
			city = InputValidationUtil.readNonEmptyString(
					ScannerUtil.getScanner(), "Enter the city name (3 - 30 characters): ");
			if (city.length() >= 3 && city.length() <= 30) {
				venue.setCity(city);
				break;
			}
			System.out.println("City name must be between 3 and 30 characters.");
		}

		String state;
		while (true) {
			state = InputValidationUtil.readNonEmptyString(
					ScannerUtil.getScanner(), "Enter the state name (2 - 30 characters): ");
			if (state.length() >= 2 && state.length() <= 30) {
				venue.setState(state);
				break;
			}
			System.out.println("State name must be between 2 and 30 characters.");
		}

		String pincode;
		while (true) {
			pincode = InputValidationUtil.readNonEmptyString(
					ScannerUtil.getScanner(), "Enter the pincode (5 - 10 characters): ");
			if (pincode.length() >= 5 && pincode.length() <= 10) {
				venue.setPincode(pincode);
				break;
			}
			System.out.println("Pincode must be between 5 and 10 characters.");
		}

		int maxCapacity;
		while (true) {
			maxCapacity = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter the maximum capacity: ");
			if (maxCapacity > 0) {
				venue.setMaxCapacity(maxCapacity);
				break;
			}
			System.out.println("Venue capacity must be greater than 0");
		}
		try {
			adminService.addVenue(venue);
			System.out.println("Venue added successfully.");
		} catch (DataAccessException e) {
			System.out.println("Error adding venue: " + e.getMessage());
		}
	}

	public void updateVenue() {
		try {
			Venue selectedVenue = selectVenue();
			if (selectedVenue == null) {
				return;
			}

			if (selectedVenue.isStatus() == false) {
				char choice = InputValidationUtil.readChar(ScannerUtil.getScanner(),
						"The selected venue is inactive\nDo you need activate the venue (Y/N): ");
				if (choice == 'Y' || choice == 'y') {
					adminService.activateVenue(selectedVenue.getVenueId());
					System.out.println("Venue activated successfully.");

					return;
				}
				System.out.println("Action cancelled by user!");
				return;
			}
			System.out.println("Press Enter to keep the current value");

			String name;
			while (true) {
				name = InputValidationUtil.readString(
						ScannerUtil.getScanner(),
						"Venue name (" + selectedVenue.getName() + ") [3 - 100]: ");

				if (name.isBlank()) {
					break;
				}

				if (name.length() >= 3 && name.length() <= 100) {
					selectedVenue.setName(name);
					break;
				}

				System.out.println("Venue name must be between 3 and 100 characters.");
			}

			String street;
			while (true) {
				street = InputValidationUtil.readString(
						ScannerUtil.getScanner(),
						"Street (" + selectedVenue.getStreet() + ") [3 - 100]: ");

				if (street.isBlank()) {
					break;
				}

				if (street.length() >= 3 && street.length() <= 100) {
					selectedVenue.setStreet(street);
					break;
				}

				System.out.println("Street name must be between 3 and 100 characters.");
			}

			String city;
			while (true) {
				city = InputValidationUtil.readString(
						ScannerUtil.getScanner(),
						"City (" + selectedVenue.getCity() + ") [3 - 30]: ");

				if (city.isBlank()) {
					break;
				}

				if (city.length() >= 3 && city.length() <= 30) {
					selectedVenue.setCity(city);
					break;
				}

				System.out.println("City name must be between 3 and 30 characters.");
			}

			String state;
			while (true) {
				state = InputValidationUtil.readString(
						ScannerUtil.getScanner(),
						"State (" + selectedVenue.getState() + ") [2 - 30]: ");

				if (state.isBlank()) {
					break;
				}

				if (state.length() >= 2 && state.length() <= 30) {
					selectedVenue.setState(state);
					break;
				}

				System.out.println("State name must be between 2 and 30 characters.");
			}

			String pincode;
			while (true) {
				pincode = InputValidationUtil.readString(
						ScannerUtil.getScanner(),
						"Pincode (" + selectedVenue.getPincode() + ") [5 - 10]: ");

				if (pincode.isBlank()) {
					break;
				}

				if (pincode.length() >= 5 && pincode.length() <= 10) {
					selectedVenue.setPincode(pincode);
					break;
				}

				System.out.println("Pincode must be between 5 and 10 characters.");
			}

			int capacity = InputValidationUtil.readInt(
					ScannerUtil.getScanner(),
					"Maximum capacity (" + selectedVenue.getMaxCapacity() + ") enter 0 to skip: ");

			if (capacity > 0) {
				selectedVenue.setMaxCapacity(capacity);
			}

			adminService.updateVenue(selectedVenue);
			System.out.println("Venue updated successfully.");
		} catch (DataAccessException e) {
			System.out.println("Error updating venue: " + e.getMessage());
		}
	}

	public void removeVenue() {
		try {
			Venue selectedVenue = selectVenue();
			if (selectedVenue == null)
				return;

			char confirm = InputValidationUtil.readChar(ScannerUtil.getScanner(),
					"Are you sure you want to remove this venue (Y/N): ");

			if (Character.toUpperCase(confirm) != 'Y') {
				System.out.println("Venue removal cancelled.");
				return;
			}

			adminService.removeVenue(selectedVenue.getVenueId());

			System.out.println("Venue removed successfully.");
		} catch (DataAccessException e) {
			System.out.println("Error removing venue: " + e.getMessage());
		}
	}

	public void listEventsByCity() {
		try {
			Venue selectedVenue = selectVenue();
			if (selectedVenue == null)
				return;

			List<Event> events = eventService.searchByCity(selectedVenue.getVenueId());

			if (events.isEmpty()) {
				System.out.println("No events for this venue");
			} else {
				PaginationUtil.paginate(events, MenuHelper::printEventSummaries);
			}
		} catch (DataAccessException e) {
			System.out.println("Error searching events by city: " + e.getMessage());
		}
	}

	public void listAllVenues() {
		try {
			List<Venue> venues = getAllVenues();

			if (venues.isEmpty()) {
				System.out.println("No venues found.");
			} else {
				PaginationUtil.paginate(venues, AdminMenuHelper::printVenues);
			}
		} catch (DataAccessException e) {
			System.out.println("Error listing venues: " + e.getMessage());
		}
	}

	private Venue selectVenue() throws DataAccessException {

		List<Venue> venues = getAllVenues();

		if (venues.isEmpty()) {
			System.out.println("No venues found.");
			return null;
		}

		PaginationUtil.paginate(venues, AdminMenuHelper::printVenues);

		int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Select a venue (1-" + venues.size() + "): ");

		while (choice < 1 || choice > venues.size()) {
			choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		return venues.get(choice - 1);
	}
}
