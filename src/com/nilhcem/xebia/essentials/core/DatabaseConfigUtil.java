package com.nilhcem.xebia.essentials.core;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public final class DatabaseConfigUtil extends OrmLiteConfigUtil {
	public static void main(String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt");
	}
}
