package com.globallogic.bciexercise.services;

import com.globallogic.bciexercise.domain.User;
import com.globallogic.bciexercise.dtos.UserResponse;
import com.globallogic.bciexercise.errors.ApiError;
import com.globallogic.bciexercise.errors.ApiErrorCodes;
import com.globallogic.bciexercise.errors.CypherException;
import com.globallogic.bciexercise.errors.TokenException;
import com.globallogic.bciexercise.errors.UserNotFoundException;
import com.globallogic.bciexercise.mappers.UserMapper;
import com.globallogic.bciexercise.repositories.UserRepository;
import com.globallogic.bciexercise.utils.Cipher;
import com.globallogic.bciexercise.utils.TokenProvider;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserLoginServiceImpl implements UserLoginService{
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final Cipher aesCipher;

    public UserLoginServiceImpl(TokenProvider tokenProvider, UserRepository userRepository, Cipher aesCipher) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.aesCipher = aesCipher;
    }

    public UserResponse loginUser(String token) {
        if(token == null || token.trim().isEmpty()) {
            ApiError error = new ApiError(ApiErrorCodes.TOKEN_ERROR.getCode(),
                    "token can not be empty");
            throw new TokenException(error);
        }
        tokenProvider.validateToken(token);
        String userEmail = tokenProvider.getContentFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String newToken = tokenProvider.generateToken(user.getEmail());
            user.setToken(newToken);
            user.setLastLogin(new Date());
            userRepository.save(user);
            UserResponse userResponse = UserMapper.toResponse(user);
            try {
                userResponse.setPassword(aesCipher.decrypt(user.getPassword()));
            } catch (Exception e) {
                ApiError error = new ApiError(ApiErrorCodes.DECRYPTION_ERROR.getCode(), "error while decrypting: " + e.getMessage());
                throw new CypherException(error);
            }
            return userResponse;
        }
        ApiError error = new ApiError(ApiErrorCodes.USER_NOT_FOUND.getCode(),
                String.format("user with email %s not found", userEmail));
        throw new UserNotFoundException(error);
    }
}
