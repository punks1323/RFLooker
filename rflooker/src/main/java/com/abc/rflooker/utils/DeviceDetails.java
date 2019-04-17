package com.abc.rflooker.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.abc.rflooker.utils.models.Active;
import com.abc.rflooker.utils.models.Mobile;
import com.abc.rflooker.utils.models.Network;
import com.abc.rflooker.utils.models.Sim;
import com.abc.rflooker.utils.models.TelephonyDetails;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresPermission;

public class DeviceDetails {

    @RequiresPermission(allOf = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE})
    public static String getDeviceDetails(Context context, Gson gson) {
        String json = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            Integer phoneCount = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                phoneCount = telephonyManager.getPhoneCount();
            }
            String phoneType;
            switch (telephonyManager.getPhoneType()) {
                case TelephonyManager.PHONE_TYPE_NONE:
                    phoneType = "No phone radio";
                    break;
                case TelephonyManager.PHONE_TYPE_GSM:
                    phoneType = "Phone radio is GSM";
                    break;
                case TelephonyManager.PHONE_TYPE_CDMA:
                    phoneType = "Phone radio is CDMA";
                    break;
                case TelephonyManager.PHONE_TYPE_SIP:
                    phoneType = "Phone is via SIP";
                    break;
                default:
                    phoneType = "Unknown";
                    break;
            }

            String line1Number = telephonyManager.getLine1Number();
            String subscriberId = telephonyManager.getSubscriberId();
            String networkOperatorName = telephonyManager.getNetworkOperatorName();
            String networkCountryIso = telephonyManager.getNetworkCountryIso();
            Boolean isNetworkRoaming = telephonyManager.isNetworkRoaming();

            String simOperatorName = telephonyManager.getSimOperatorName();
            String simCountryIso = telephonyManager.getSimCountryIso();
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            String simCarrierIdName = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                simCarrierIdName = (String) telephonyManager.getSimCarrierIdName();
            }

            List<String> imeiList = new ArrayList<>();
            for (int i = 0; i < phoneCount; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imeiList.add(telephonyManager.getImei(i));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    telephonyManager.getDeviceId(i);
                }

            }
            //If lower than android M
            if (imeiList.size() == 0)
                imeiList.add(telephonyManager.getDeviceId());

            String wifiMacAddress = null;
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null)
                wifiMacAddress = connectionInfo.getMacAddress();

            String bluetoothAddress = BluetoothAdapter.getDefaultAdapter() != null ? BluetoothAdapter.getDefaultAdapter().getAddress() : null;
            String androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            Network network = new Network();
            network.setNetworkOperatorName(networkOperatorName);
            network.setNetworkCountryIso(networkCountryIso);
            network.setNetworkRoaming(isNetworkRoaming);

            Sim sim = new Sim();
            sim.setSimOperatorName(simOperatorName);
            sim.setSimCountryIso(simCountryIso);
            sim.setSimSerialNumber(simSerialNumber);
            sim.setSimCarrierIdName(simCarrierIdName);

            Active active = new Active();
            active.setLine1Number(line1Number);
            active.setSubscriberId(subscriberId);
            active.setNetwork(network);
            active.setSim(sim);

            TelephonyDetails telephoneDetails = new TelephonyDetails();
            telephoneDetails.setPhoneCount(phoneCount);
            telephoneDetails.setPhoneType(phoneType);
            telephoneDetails.setActive(active);
            telephoneDetails.setImei(imeiList);

            Mobile mobile = new Mobile();
            mobile.setTelephonyDetails(telephoneDetails);
            mobile.setWifiMacAddress(wifiMacAddress);
            mobile.setBluetoothMacAddress(bluetoothAddress);
            mobile.setAndroidSecureId(androidId);

            json = gson.toJson(mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
