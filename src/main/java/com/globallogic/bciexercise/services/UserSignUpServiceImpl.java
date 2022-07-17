package com.globallogic.bciexercise.services;

import com.globallogic.bciexercise.errors.ApiError;
import com.globallogic.bciexercise.errors.ApiErrorCodes;
import com.globallogic.bciexercise.dtos.UserResponse;
import com.globallogic.bciexercise.domain.User;
import com.globallogic.bciexercise.errors.CypherException;
import com.globallogic.bciexercise.errors.DuplicatedUserException;
import com.globallogic.bciexercise.errors.ValidationException;
import com.globallogic.bciexercise.mappers.UserMapper;
import com.globallogic.bciexercise.repositories.UserRepository;
import com.globallogic.bciexercise.utils.Cipher;
import com.globallogic.bciexercise.utils.MailFormatValidator;
import com.globallogic.bciexercise.utils.PasswordValidator;
import com.globallogic.bciexercise.utils.TokenProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserSignUpServiceImpl implements UserSignUpService {

    private final UserRepository userRepository;
    private final TokenProvider jwtProvider;
    private final Cipher cipher;

    public UserSignUpServiceImpl(UserRepository userRepository, TokenProvider jwtProvider, Cipher cipher) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.cipher = cipher;
    }

    public UserResponse signUp(User user) {

        validateInput(user);
        checkUserExistence(user);
        setUserFields(user);
        discardPhonesWithAllEmptyValues(user);

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    private void setUserFields(User user) {

        user.setId(UUID.randomUUID().toString());
        user.setToken(jwtProvider.generateToken(user.getEmail()));
        Date now = new Date();
        user.setLastLogin(now);
        user.setCreated(now);
        user.setActive(true);

        String encryptedPassword;
        try {
            encryptedPassword = cipher.encrypt(user.getPassword());
        } catch (Exception e) {
            ApiError error = new ApiError(ApiErrorCodes.ENCRYPTION_ERROR.getCode(), "error while encrypting: " + e.getMessage());
            throw new CypherException(error);
        }
        user.setPassword(encryptedPassword);
    }

    private void discardPhonesWithAllEmptyValues(User user) {
        if (user.getPhones() == null) {
            return;
        }
        user.setPhones(user.getPhones()
                .stream()
                .filter(
                        phone -> (phone.getNumber() != null
                                || phone.getCityCode() != null
                                || phone.getCountryCode() != null || !phone.getCountryCode().isEmpty())
                ).collect(Collectors.toList()));
    }

    private void checkUserExistence(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            ApiError error = new ApiError(ApiErrorCodes.ALREADY_EXISTING_RESOURCE.getCode(), "user already exist");
            throw new DuplicatedUserException(error);
        }
    }

    private void validateInput(User user) {
        List<ApiError> emailValidations = MailFormatValidator.isValidMail(user.getEmail());
        List<ApiError> errors = new ArrayList<>(emailValidations);

        List<ApiError> passwordValidationErrors = PasswordValidator.isValidPassword(user.getPassword());
        errors.addAll(passwordValidationErrors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
