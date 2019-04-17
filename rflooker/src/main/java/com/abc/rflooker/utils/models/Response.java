package com.abc.rflooker.utils.models;

import com.google.gson.annotations.SerializedName;

import lombok.Data;


@Data
public class Response{

	@SerializedName("mobile")
	private Mobile mobile;
}