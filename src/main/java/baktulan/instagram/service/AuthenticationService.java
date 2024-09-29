package baktulan.instagram.service;

import baktulan.instagram.dto.authenticationDTO.AuthenticationResponse;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.authenticationDTO.SignInRequest;
import baktulan.instagram.dto.authenticationDTO.SignUpRequest;
import baktulan.instagram.exception.AlreadyExistException;
import baktulan.instagram.exception.BadCredentialException;
import baktulan.instagram.exception.NotFoundException;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest signUpRequest) throws AlreadyExistException;
    AuthenticationResponse signIn(SignInRequest signInRequest) throws NotFoundException, BadCredentialException;
    ProfileUser getProfile() throws NotFoundException;
}
