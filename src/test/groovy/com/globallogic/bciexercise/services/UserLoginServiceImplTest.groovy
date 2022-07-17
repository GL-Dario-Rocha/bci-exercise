package com.globallogic.bciexercise.services

import com.globallogic.bciexercise.domain.User
import com.globallogic.bciexercise.errors.ApiErrorCodes
import com.globallogic.bciexercise.errors.TokenException
import com.globallogic.bciexercise.errors.UserNotFoundException
import com.globallogic.bciexercise.repositories.UserRepository
import com.globallogic.bciexercise.utils.Cipher
import com.globallogic.bciexercise.utils.JwtProvider
import spock.lang.Specification

class UserLoginServiceImplTest extends Specification {

    UserRepository userRepository
    JwtProvider jwtProvider
    UserLoginServiceImpl loginService
    Cipher cypher

    def setup() {
        userRepository = Mock(UserRepository)
        jwtProvider = Mock(JwtProvider)
        cypher = Mock(Cipher)
        loginService = new UserLoginServiceImpl(jwtProvider, userRepository, cypher)
    }


    def "should return the user if it exists" () {
        given:
            def token = "token"
            def newToken = "newToken"
            def userEmail = "valid@mail.com"
            def encryptedPassword = "vu3Ln2Dnr/c6Jr1Cr0YAtHhCTPqIHKzrehsreyIIwsI="
            def user = new User()
            def now = new Date()
            user.setEmail(userEmail)
            user.setToken(token)
            user.setCreated(now)
            user.setLastLogin(now)
            user.setPassword(encryptedPassword)
            jwtProvider.validateToken(token) >> _
            jwtProvider.getContentFromToken(token) >> userEmail
            userRepository.findByEmail(userEmail) >> Optional.of(user)
            jwtProvider.generateToken(user.getEmail()) >> newToken
            userRepository.save(user) >> user
            cypher.decrypt(encryptedPassword) >> "decryptedPassword"
        when:
            def response = loginService.loginUser(token)
        then:
            response.getToken() == newToken
            response.getCreated() == now
            response.getLastLogin() > now
            1 * userRepository.save(user)
    }

    def "should throw a exception when the token is empty" () {
        given:
            def token = ""
        when:
            loginService.loginUser(token)
        then:
            def exception = thrown(TokenException)
            exception.getError().getCode() == ApiErrorCodes.TOKEN_ERROR.getCode()
            exception.getError().getDetail() == "token can not be empty"
            exception.getError().getTimestamp() != null
    }

    def "should throw a exception when the user not found" () {
        given:
            def token = "token"
            def userEmail = "valid@mail.com"
            jwtProvider.validateToken(token) >> _
            jwtProvider.getContentFromToken(token) >> userEmail
            userRepository.findByEmail(userEmail) >> Optional.empty()
        when:
            loginService.loginUser(token)
        then:
            def exception = thrown(UserNotFoundException)
            exception.getError().getCode() == ApiErrorCodes.USER_NOT_FOUND.getCode()
            exception.getError().getDetail() == String.format("user with email %s not found", userEmail)
            exception.getError().getTimestamp() != null
    }
}
