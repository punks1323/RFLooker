package com.abc.rflooker.utils.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import lombok.Data;


@Data
public class TelephonyDetails{

	@SerializedName("phoneType")
	private String phoneType;

	@SerializedName("active")
	private Active active;

	@SerializedName("imei")
	private List<String> imei;

	@SerializedName("phoneCount")
	private int phoneCount;
}