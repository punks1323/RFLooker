package com.abc.rflooker.data;

import com.abc.rflooker.data.local.db.DBManager;
import com.abc.rflooker.data.local.prefs.PrefManager;
import com.abc.rflooker.data.remote.ApiHelper;

public interface DataManager extends DBManager, PrefManager, ApiHelper {
}
