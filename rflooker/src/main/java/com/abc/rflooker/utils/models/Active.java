package com.abc.rflooker.utils.models;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Active{

	@SerializedName("sim")
	private Sim sim;

	@SerializedName("subscriberId")
	private String subscriberId;

	@SerializedName("line1Number")
	private String line1Number;

	@SerializedName("network")
	private Network network;
}