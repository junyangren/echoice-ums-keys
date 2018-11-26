package org.echoice.ums.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.domain.UserCakey;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperForUserKey implements RowMapper<UserCakey>{

	@Override
	public UserCakey mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		UserCakey userCakey=new UserCakey();
		
		userCakey.setId(rs.getLong("id"));
		userCakey.setIdcard(rs.getString("idcard"));
		userCakey.setHardwareSn(rs.getString("hardware_sn"));
		userCakey.setStatus(rs.getString("status"));
		userCakey.setCreateTime(rs.getTimestamp("create_time"));
		userCakey.setCreateUser(rs.getString("create_user"));
		userCakey.setOpTime(rs.getTimestamp("op_time"));
		userCakey.setOpUser(rs.getString("op_user"));
		EcGroup ecGroup=new EcGroup();
		ecGroup.setGroupId(rs.getLong("group_id"));
		ecGroup.setName(rs.getString("groupName"));
		EcUser ecUser=new EcUser();
		ecUser.setName(rs.getString("userName"));
		ecUser.setJobNumber(rs.getString("jobNumber"));
		
		userCakey.setEcGroup(ecGroup);
		userCakey.setEcUser(ecUser);
		return userCakey;
	}

}
