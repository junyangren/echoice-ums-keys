package org.echoice.ums.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.echoice.ums.domain.AppInfo;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperForAppInfo implements RowMapper<AppInfo>{

	@Override
	public AppInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		AppInfo obj=new AppInfo();
		obj.setId(rs.getLong("id"));
		obj.setAppId(rs.getString("app_id"));
		obj.setAppName(rs.getString("app_name"));
		obj.setNote(rs.getString("note"));
		obj.setIconPath(rs.getString("icon_path"));
		obj.setBrowserTypes(rs.getString("browser_types"));
		obj.setAppPaths(rs.getString("app_paths"));
		obj.setAppUrl(rs.getString("app_url"));
		obj.setAppType(rs.getString("app_type"));
		obj.setTaxis(rs.getLong("taxis"));
		return obj;
	}

}
