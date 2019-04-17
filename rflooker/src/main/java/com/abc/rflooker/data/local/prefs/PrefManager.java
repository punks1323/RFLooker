package com.abc.rflooker.data.local.prefs;

public interface PrefManager {

    Boolean getIsUserLoggedIn();

    void setIsUserLoggedIn(boolean status);

    String getUserEmailId();

    void setUserEmailId(String userEmailId);

    String getUserPassword();

    void setUserPassword(String userPassword);
}
