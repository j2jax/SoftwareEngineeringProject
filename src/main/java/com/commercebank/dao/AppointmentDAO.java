package com.commercebank.dao;

import com.commercebank.mapper.AppointmentServiceMapper;
import com.commercebank.model.Appointment;
import com.commercebank.mapper.AppointmentMapper;
import com.commercebank.model.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentDAO {
    // The JdbcTemplate is the class that interfaces with the database
    private final JdbcTemplate jdbcTemplate;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentServiceMapper appointmentServiceMapper;

    @Autowired // This tells Spring to use the JdbcTemplate Bean we created in the configuration class
    AppointmentDAO(final JdbcTemplate jdbcTemplate, final AppointmentMapper appointmentMapper, final AppointmentServiceMapper appointmentServiceMapper){
        // Set up the dependencies from Spring
        this.jdbcTemplate = jdbcTemplate;
        this.appointmentMapper = appointmentMapper;
        this.appointmentServiceMapper = appointmentServiceMapper;
    }

    public List<Appointment> list(){
        // Run the SQL query on the database to select all appointments and return a List of Appointment objects
        List<Appointment> appointments = this.jdbcTemplate.query("SELECT * FROM appointment", appointmentMapper);

        List<AppointmentService> appointmentServices = listServices();

        for(Appointment appointment : appointments){
            int[] services = appointmentServices.stream()
                    .filter(a -> a.getAppointmentId() == appointment.getId())
                    .mapToInt(AppointmentService::getServiceId)
                    .toArray();

            appointment.setServiceIds(services);
        }

        return appointments;
    }

    public void insert(Appointment appointment){
        this.jdbcTemplate.update("INSERT INTO appointment (calendar_id, time, branch_id, manager_id, customer_id) VALUES (?, ?, ?, ?, ?)",
                appointment.getCalendarId(),
                appointment.getTime(),
                appointment.getBranchId(),
                appointment.getManagerId(),
                appointment.getCustomerId());


        // Get the id of the appointment just inserted
        List<Appointment> appointments = list();
        int id = appointments.get(appointments.size() - 1).getId();

        // Insert all of the appointment services
        for(int service : appointment.getServiceIds()){
            this.jdbcTemplate.update("INSERT INTO appointment_services (service_id, appointment_id) VALUES (?, ?)", service, id);
        }
    }

    public List<AppointmentService> listServices(){
        return this.jdbcTemplate.query("SELECT * FROM appointment_services", appointmentServiceMapper);
    }
}
