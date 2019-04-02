package com.commercebank.dao;

import com.commercebank.model.Manager;
import com.commercebank.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ManagerDAO {
    // The JdbcTemplate is the class that interfaces with the database
    private final JdbcTemplate jdbcTemplate;
    private final ManagerMapper managerMapper;

    @Autowired // This tells Spring to use the JdbcTemplate Bean we created in the configuration class
    ManagerDAO(final JdbcTemplate jdbcTemplate, final ManagerMapper managerMapper){
        // Set up the dependencies from Spring
        this.jdbcTemplate = jdbcTemplate;
        this.managerMapper = managerMapper;
    }

    public List<Manager> list(){
        // Run the SQL query on the database to select all managers and return a List of Manager objects
        return this.jdbcTemplate.query("SELECT * FROM manager", managerMapper);
    }

    public void insert(Manager manager){
        this.jdbcTemplate.update("INSERT INTO manager (f_name, l_name, phone_num, email, branch_id) VALUES (?, ?, ?, ?, ?)",
                manager.getFirstName(),
                manager.getLastName(),
                manager.getPhoneNumber(),
                manager.getEmail(),
                manager.getBranchId());
    }

    public void update(int id, Manager manager){
        String sql = "UPDATE manager SET f_name=?, l_name=?, phone_num=?, email = ?, branch_id=? WHERE id = ?";
        this.jdbcTemplate.update(sql, manager.getFirstName(), manager.getLastName(),
                manager.getPhoneNumber(), manager.getEmail(), manager.getBranchId(), id);
    }

    public void delete(int id){this.jdbcTemplate.update("DELETE FROM manager WHERE id = ?", id); }



    // TODO: Make work for multiple servicecs
//    public List<Manager> list(int branchID, int[] serviceIDs, int calendarID, LocalTime time){
//        String sql = "SELECT * FROM manager WHERE (branch_id=?) AND id IN (SELECT id FROM manager JOIN skills ON(id = manager_id) WHERE (service_id=?)) AND id NOT IN (SELECT manager_id FROM manager_unavailable JOIN unavailable ON(unavailable_id = id) WHERE (calendar_id=?) AND (time=?));";
//        return this.jdbcTemplate.query(sql, managerMapper, branchID, serviceIDs[0], calendarID, time);
//    }
}
