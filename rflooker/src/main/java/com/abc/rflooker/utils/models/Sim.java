package com.abc.rflooker.utils.models;

import com.google.gson.annotations.SerializedName;

import lombok.Data;


@Data
public class Sim{

	@SerializedName("simCountryIso")
	private String simCountryIso;

	@SerializedName("simCarrierIdName")
	private String simCarrierIdName;

	@SerializedName("simSerialNumber")
	private String simSerialNumber;

	@SerializedName("simOperatorName")
	private String simOperatorName;
}