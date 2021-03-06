package com.commercebank.mapper;

import com.commercebank.model.Manager;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ManagerMapper implements RowMapper<Manager> {
    @Override
    public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Create a new manager object that is mapped to the SQL Result rs
        return new Manager(
                rs.getInt("id"),
                rs.getString("f_name"),
                rs.getString("l_name"),
                rs.getString("phone_num"),
                rs.getString("email"),
                rs.getInt("branch_id"));
    }
}
