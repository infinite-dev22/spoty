package org.infinite.spoty.views.login;

import javafx.concurrent.Task;
import org.infinite.spoty.data_source.models.APIResponseModel;
import org.infinite.spoty.viewModels.LoginViewModel;

import java.io.IOException;

public class LoginTask extends Task<APIResponseModel> {
    @Override
    protected APIResponseModel call() throws Exception {
        return login();
    }

    public APIResponseModel login() throws IOException, InterruptedException {
        return LoginViewModel.login();
    }
}
