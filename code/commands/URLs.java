package com.socialvagrancy.vail.commands;

public class URLs
{
	public static String accountsURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/accounts";
	}

	public static String bucketsURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/buckets";
	}

	public static String clearCacheURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/clear_cache";
	}

	public static String keysURL(String ipaddress, String account, String user)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account + "/" + user + "/keys";
	}

	public static String keysDeleteURL(String ipaddress, String account, String user, String key)
	{
		return "https://" + ipaddress + "/sl/api/iam/users/" + account + "/" + user + "/keys/" + key;
	}

	public static String lifecycleURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/lifecycles";
	}

	public static String loginURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/tokens";
	}

	public static String storageURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/storage";
	}

	public static String usersURL(String ipaddress)
	{
		return "https://" + ipaddress + "/sl/api/iam/users";
	}

}
