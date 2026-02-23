package com.ems.actions;

import java.time.Instant;

import com.ems.enums.UserRole;
import com.ems.exception.AuthenticationException;
import com.ems.exception.AuthorizationException;
import com.ems.exception.InvalidPasswordFormatException;
import com.ems.model.User;
import com.ems.service.UserService;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.PasswordUtil;
import com.ems.util.ScannerUtil;

public class UserAction {
	private final UserService userService;
	
	public UserAction() {
		this.userService = ApplicationUtil.userService();
	}
	
	public User login() throws AuthorizationException, AuthenticationException {
	    String email =
	        InputValidationUtil.readNonEmptyString(
	            ScannerUtil.getScanner(),
	            "Enter email: "
	        );
	    while (!email.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            email =
                InputValidationUtil.readNonEmptyString(
                    ScannerUtil.getScanner(),
                    "Enter valid Email Address: "
                );
        }

	    String password =
	        InputValidationUtil.readNonEmptyString(
	            ScannerUtil.getScanner(),
	            "Enter password: "
	        );

        return userService.login(email, password);
    }

    public UserRole getUserRole(User user) {
        return userService.getRole(user);
    }

	public void createAccount(UserRole role) {
	
		String fullName;
		do {
		    fullName = InputValidationUtil.readNonEmptyString(
		        ScannerUtil.getScanner(),
		        "Enter Full Name (max 30 chars): "
		    );
		    if (fullName.length()<2 ||fullName.length() >= 30) {
		        System.out.println("Error: Name must be minimum of 2 characters and a maximum of 30 characters.");
		    }
		} while (fullName.length() >= 30 || fullName.length()<2);

	
	    String email;
	    while (true) {
	        email = InputValidationUtil.readNonEmptyString(
	                ScannerUtil.getScanner(),
	                "Enter Email (max 100 characters): "
	            );

	        if (email.length() > 100) {
	        	System.out.println("Error: Email too long. Database limit is 100 characters.");
	        	continue;
	        	} 
	        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
	            System.out.println("Invalid email format.\nExample: name@company.com");
	            continue;
	        }
	        if (userExists(email)) {
	            System.out.println("This email is already registered.\nPlease try a different email.\n");
	            continue;
	        }
	
	        break;
	    }
	
	    String phone = InputValidationUtil.readString(
	    	    ScannerUtil.getScanner(),
	    	    "Enter phone number (optional): "
	    	);

	    	if (phone != null && !phone.trim().isEmpty()) {
	    	    phone = phone.replaceAll("\\D", "");

	    	    // starts with 6-9 and has exactly 10 digits
	    	    while (!phone.matches("^[6-9][0-9]{9}$")) {
	    	        phone = InputValidationUtil.readString(
	    	            ScannerUtil.getScanner(),
	    	            "Enter valid 10-digit phone number starting with 6-9: "
	    	        ).replaceAll("\\D", "");
	    	    }
	    	} else {
	    	    phone = null;
	    	}

	
	    String passwordPrompt =
	        "Create a password:\n" +
	        "Minimum 8 characters\n" +
	        "At least 1 uppercase, 1 lowercase, 1 number, 1 special character\n";
	
	    String password = InputValidationUtil.readNonEmptyString(
	        ScannerUtil.getScanner(),
	        passwordPrompt
	    );
	
	    while (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")) {
	        password = InputValidationUtil.readNonEmptyString(
	            ScannerUtil.getScanner(),
	            "Enter a valid password: "
	        );
	    }
	
	    int genderChoice;
	    do {
	        genderChoice = InputValidationUtil.readInt(
	            ScannerUtil.getScanner(),
	            "Enter your gender:\n1. Male\n2. Female\n3. Prefer not to say\n"
	        );
	    } while (genderChoice < 1 || genderChoice > 3);
	
	    String gender = (genderChoice == 1)
	        ? "Male"
	        : (genderChoice == 2)
	            ? "Female"
	            : "Opt-out";
	
	    boolean isUserCreated = userService.createAccount(fullName, email, phone, password, gender, role);
	    if(isUserCreated) {
	    	System.out.println("\nAccount has been created successfully!\n");
	    }else {
	    	System.out.println("\nAccount creation failed!, Retry");
	    }
	}
	
	public boolean updateProfile(User loggedInUser){
	
	    System.out.println("\nUpdate Profile");
	    System.out.println("Press Enter to keep current value.\n");
	
	    String fullName = loggedInUser.getFullName();
	    String phone = loggedInUser.getPhone();
	    String passwordHash = loggedInUser.getPasswordHash();
	
	    /* ================= FULL NAME ================= */
	
	    String newName = InputValidationUtil.readString(
	            ScannerUtil.getScanner(),
	            "Full Name (" + fullName + "): "
	    );
	    
	    if (newName != null && !newName.trim().isEmpty()) {
	
	        while (newName.length() >= 30 || newName.length()<2) {
	            newName = InputValidationUtil.readString(
	                    ScannerUtil.getScanner(),
	                    "Name must be minimum of 2 characters and a maximum of 30 characters: "
	            );
	        }
	
	        fullName = newName;
	    }
	
	    /* ================= PHONE ================= */
	
	    String currentPhone = phone == null ? "Not Set" : phone;
	
	    String newPhone = InputValidationUtil.readString(
	            ScannerUtil.getScanner(),
	            "Phone (" + currentPhone + "): "
	    );
	
	    if (newPhone != null && !newPhone.trim().isEmpty()) {

	        while (true) {

	            newPhone = newPhone.trim();

	            if (newPhone.isEmpty()) {
	                break; // user changed mind, keep old phone
	            }

	            String sanitized = newPhone.replaceAll("\\D", "");

	            if (sanitized.matches("^[6-9][0-9]{9}$")) {
	                phone = sanitized;
	                break;
	            }

	            newPhone = InputValidationUtil.readString(
	                    ScannerUtil.getScanner(),
	                    "Enter valid 10-digit phone starting with 6-9 (or press Enter to cancel): "
	            );
	        }
	    }

	    /* ================= PASSWORD ================= */

	    char changePassword = InputValidationUtil.readChar(
	            ScannerUtil.getScanner(),
	            "Do you want to change password? (Y/N): "
	    );

	    if (Character.toUpperCase(changePassword) == 'Y') {

	        String passwordPrompt =
	                "New password:\n" +
	                "Minimum 8 characters\n" +
	                "At least 1 uppercase, 1 lowercase, 1 number, 1 special character\n";

	        while (true) {

	            String newPassword = InputValidationUtil.readNonEmptyString(
	                    ScannerUtil.getScanner(),
	                    passwordPrompt
	            );

	            if (!newPassword.matches(
	                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")) {

	                System.out.println("Password does not meet required format.");
	                continue;
	            }

	            try {
	                passwordHash = PasswordUtil.hashPassword(newPassword);
	                break;  // success, exit loop

	            } catch (InvalidPasswordFormatException e) {
	                System.out.println("Password format invalid. Please try again.");
	            }
	        }
	    }	
	    boolean isChanged = 
	            !fullName.equals(loggedInUser.getFullName()) ||
	            !java.util.Objects.equals(phone, loggedInUser.getPhone()) ||
	            !passwordHash.equals(loggedInUser.getPasswordHash());

	    if (!isChanged) {
	        System.out.println("\nNo changes detected. Profile not updated.\n");
	        return false;
	    }
	    /* ================= CREATE NEW USER OBJECT ================= */
	
	    User updatedUser = new User(
	            loggedInUser.getUserId(),
	            fullName,
	            loggedInUser.getEmail(),
	            phone,
	            passwordHash,
	            loggedInUser.getRoleId(),
	            loggedInUser.getStatus(),
	            loggedInUser.getCreatedAt(),
	            Instant.now(),
	            loggedInUser.getGender(),
	            loggedInUser.getFailedAttempts(),
	            loggedInUser.getLastLogin()
	    );
	
	    /* ================= SAVE ================= */
	
	    boolean updated = userService.updateUserProfile(updatedUser);
	
	    if (updated) {
	        System.out.println("\nProfile updated successfully. \n");
	        System.out.println("For security reasons, please log in again.\n");
	    } else {
	        System.out.println("\nProfile update failed.\n");
	    }
		return updated;
	}
	
    public boolean userExists(String email) {
        return userService.checkUserExists(email);
    }
}
