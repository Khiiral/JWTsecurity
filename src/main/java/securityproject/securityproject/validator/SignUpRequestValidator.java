package securityproject.securityproject.validator;



import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.SignUpRequest;
import securityproject.securityproject.services.UserService;

@Component
@RequiredArgsConstructor
public class SignUpRequestValidator implements Validator {

    private final UserService userService;    

    @Override
    public boolean supports(Class<?> clazz) {
       return SignUpRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //To perform validation, first you have to downcast target to SignUpRequest
        SignUpRequest signUpRequest = (SignUpRequest)target;

        String firstName = signUpRequest.getFirstName().trim();
        String lastName = signUpRequest.getLastName().trim();
        String email = signUpRequest.getEmail().trim();
        String password = signUpRequest.getPassword().trim();

        if(StringUtils.isBlank(firstName)) {
            errors.rejectValue("firstName", "NotBlank", "Firstname is a mandatory field");
        } else if(firstName.length() < 2) {
            errors.rejectValue("firstName", "Length", "First name must contain atleast 2 characters");
        } else if(containsWhitespace(firstName)) {
            errors.rejectValue("firstName", "Whitespace", "Whitespaces are not allowed");
        } else if(!isValidName(firstName)) {
            errors.rejectValue("firstName", "NotValid", "Numbers or special characters are not allowed");
        }

        if(StringUtils.isBlank(lastName)) {
            errors.rejectValue("lastName", "NotBlank", "Lastname is a mandatory field");
        } else if(lastName.length() < 2) {
            errors.rejectValue("lastName", "Length", "Lastname must contain atleast 2 characters"); 
        } else if(containsWhitespace(lastName)) {
            errors.rejectValue("lastName", "Whitespace", "Whitespaces are not allowed");
        } else if(!isValidLastName(lastName)) {
            errors.rejectValue("lastName", "NotValid", "Numbers or special character are not allowed");
        } 

        if(StringUtils.isBlank(email)) {
            errors.rejectValue("email", "NotEmpty", "Email is required");
        } else if(!isValidEmail(email)) {
            errors.rejectValue("email", "Format", "Email format not valid");
        } else if(userService.existsByEmail(email)) {
            errors.rejectValue("email", "Exists", "Email is already taken");
        }

        if(StringUtils.isBlank(password)) {
            errors.rejectValue("password", "NotEmpty", "Password is required");
        } else if(password.length() < 6) {
            errors.rejectValue("password", "Length", "Password has to contain atleast 6 characters");
        }
    }



    public boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    public boolean isValidLastName(String lastName) {
        return lastName.matches("[a-zA-Z]+");
    }
    
    public boolean containsWhitespace(String str) {
        return str != null && str.contains(" ");
    }
}
