package com.abc.rflooker.utils.models;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Mobile{

	@SerializedName("telephonyDetails")
	private TelephonyDetails telephonyDetails;

	@SerializedName("bluetoothMacAddress")
	private String bluetoothMacAddress;

	@SerializedName("androidSecureId")
	private String androidSecureId;

	@SerializedName("wifiMacAddress")
	private String wifiMacAddress;
}