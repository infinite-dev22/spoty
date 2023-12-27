package org.infinite.spoty.data_source.repositories.interfaces;

import org.infinite.spoty.data_source.models.LoginModel;
import org.infinite.spoty.data_source.models.SignUpModel;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface AuthRepository {

    HttpResponse<String> login(LoginModel loginModel) throws IOException, InterruptedException;

    HttpResponse<String> signup(SignUpModel signUpModel) throws IOException, InterruptedException;
}
