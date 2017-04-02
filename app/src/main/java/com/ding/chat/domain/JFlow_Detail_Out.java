package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

public class JFlow_Detail_Out implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int code;
	String msg;
	List<JFlow_Detail> data;

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public List<JFlow_Detail> getData()
	{
		return data;
	}

	public void setData(List<JFlow_Detail> data)
	{
		this.data = data;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

}
