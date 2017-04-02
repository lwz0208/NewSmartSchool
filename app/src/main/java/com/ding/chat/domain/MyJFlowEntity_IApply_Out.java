package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

public class MyJFlowEntity_IApply_Out implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int code;
	List<MyJFlowEntity_IApply> data;
	String msg;

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public List<MyJFlowEntity_IApply> getData()
	{
		return data;
	}

	public void setData(List<MyJFlowEntity_IApply> data)
	{
		this.data = data;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

}
