package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/
    private EventDao eventDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.eventDao = mock(EventDao.class);
        this.lambdaService = new LambdaService(eventDao);
    }

    @Test
    void getEventById_Successful() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "Event Id";
        String name = "Event Name";
        Date today = new Date();
        User user = new User("User Id", "User Name", "User Email");
        List<Customer> listOfAttending = new ArrayList<Customer>();
        Customer customer = new Customer("Customer Id", "Customer Name", "Customer Email");
        listOfAttending.add(customer);

        EventRecord record = new EventRecord();
        record.setId(id);
        record.setName(name);
        record.setDate(today.toString());
        record.setUser(user);
        record.setListOfAttending(listOfAttending);
        record.setAddress("Fake Address");
        record.setDescription("Fake Event Description");

        when(eventDao.getEventById(id)).thenReturn(Arrays.asList(record));

        // WHEN
        EventResponseData response = this.lambdaService.getEventById(id);

        // THEN
        verify(eventDao, times(1)).getEventById(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(name, response.getName(), "The response name should match");
        assertEquals(today.toString(), response.getDate(), "The response date should match");
        assertEquals(user, response.getUser(), "The response user should match");

    }


    // Write additional tests here

    @Test
    void addEventTest_Successful() {
        // GIVEN
        String id = "Event Id";
        String name = "Event Name";
        Date today = new Date();
        User user = new User("User Id", "User Name", "User Email");
        List<Customer> listOfAttending = new ArrayList<Customer>();
        Customer customer = new Customer("Customer Id", "Customer Name", "Customer Email");
        listOfAttending.add(customer);
        CreateEventRequestData request = new CreateEventRequestData();
        request.setId(id);
        request.setName(name);
        request.setDate(today.toString());
        request.setUser(user);
        request.setListOfAttending(listOfAttending);
        request.setAddress("Fake Address");
        request.setDescription("Add Event Description Test");

        // WHEN
        EventResponseData response = this.lambdaService.addEvent(request);

        // THEN
        ArgumentCaptor<EventRecord> eventRecordArgumentCaptor = ArgumentCaptor.forClass(EventRecord.class);
        verify(eventDao).postNewEvent(eventRecordArgumentCaptor.capture());
        EventRecord record = eventRecordArgumentCaptor.getValue();

        assertEquals(id, record.getId(), "The record id should match");
        assertEquals(name, record.getName(), "The record name should match");
        assertEquals(today.toString(), record.getDate(), "The record date should match");
        assertEquals(user, record.getUser(), "The record user should match");

        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(name, response.getName(), "The response name should match");
        assertEquals(today.toString(), response.getDate(), "The response date should match");
        assertEquals(user, response.getUser(), "The response user should match");
    }

    @Test
    void addEventTest_null_request_throws_InvalidDataException() {
        // GIVEN / WHEN / THEN
        assertThrows(InvalidDataException.class, ()->this.lambdaService.addEvent(null));
    }

    @Test
    void getAllEvents_Successful() {
        //GIVEN

        List<EventRecord> rootList = new ArrayList<>();
        String id = "Event Id";
        String name = "Event Name";
        Date today = new Date();
        User user = new User("User Id", "User Name", "User Email");
        List<Customer> listOfAttending = new ArrayList<Customer>();
        Customer customer = new Customer("Customer Id", "Customer Name", "Customer Email");
        listOfAttending.add(customer);
        String address = "Fake Address";
        String description = "Add Event Description Test";

        EventRecord record1 = new EventRecord();
        record1.setId(id);
        record1.setName(name);
        record1.setDate(today.toString());
        record1.setUser(user);
        record1.setListOfAttending(listOfAttending);
        record1.setAddress(address);
        record1.setDescription(description);
        rootList.add(record1);

        when(eventDao.getAllEvents()).thenReturn(rootList);

        //WHEN
        List<Event> allEvents = lambdaService.getAllEvents();

        List<Event> expected = new ArrayList<>();
        expected.add(new Event(id, name, today.toString(), user, listOfAttending, address, description));

        assertEquals(1, allEvents.size());
        for (int i = 0; i < 1; i++) {
            assertEquals(allEvents.get(i).getName(), expected.get(i).getName());
            assertEquals(allEvents.get(i).getId(), expected.get(i).getId());
        }
    }
}