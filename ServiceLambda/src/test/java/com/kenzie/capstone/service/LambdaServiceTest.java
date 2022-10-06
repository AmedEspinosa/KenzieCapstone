package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private ExampleDao exampleDao;
    private EventDao eventDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.exampleDao = mock(ExampleDao.class);
        this.eventDao = mock(EventDao.class);
        this.lambdaService = new LambdaService(eventDao);
    }

//    @Test
//    void setDataTest() {
//        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);
//
//        // GIVEN
//        String data = "somedata";
//
//        // WHEN
//        ExampleData response = this.lambdaService.setExampleData(data);
//
//        // THEN
//        verify(exampleDao, times(1)).setExampleData(idCaptor.capture(), dataCaptor.capture());
//
//        assertNotNull(idCaptor.getValue(), "An ID is generated");
//        assertEquals(data, dataCaptor.getValue(), "The data is saved");
//
//        assertNotNull(response, "A response is returned");
//        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
//        assertEquals(data, response.getData(), "The response data should match");
//    }
//
//    @Test
//    void getDataTest() {
//        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
//
//        // GIVEN
//        String id = "fakeid";
//        String data = "somedata";
//        ExampleRecord record = new ExampleRecord();
//        record.setId(id);
//        record.setData(data);
//
//
//        when(exampleDao.getExampleData(id)).thenReturn(Arrays.asList(record));
//
//        // WHEN
//        ExampleData response = this.lambdaService.getExampleData(id);
//
//        // THEN
//        verify(exampleDao, times(1)).getExampleData(idCaptor.capture());
//
//        assertEquals(id, idCaptor.getValue(), "The correct id is used");
//
//        assertNotNull(response, "A response is returned");
//        assertEquals(id, response.getId(), "The response id should match");
//        assertEquals(data, response.getData(), "The response data should match");
//    }

    @Test
    void getEventById() {
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
//        assertEquals(data, response.getData(), "The response data should match");
    }


    // Write additional tests here

}