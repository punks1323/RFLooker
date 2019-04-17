package com.abc.rflooker.utils.models;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Network{

	@SerializedName("networkCountryIso")
	private String networkCountryIso;

	@SerializedName("isNetworkRoaming")
	private boolean isNetworkRoaming;

	@SerializedName("networkOperatorName")
	private String networkOperatorName;
}