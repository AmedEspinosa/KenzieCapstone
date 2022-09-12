package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.repositories.model.EventRepository;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import net.andreinc.mockneat.MockNeat;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class EventServiceTest {
    private EventRepository eventRepository;
    private EventService eventService;
    private LambdaServiceClient lambdaServiceClient;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @BeforeEach
    void setup() {
        eventRepository = mock(EventRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        eventService = new EventService(eventRepository, lambdaServiceClient);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void getEventById() {
        // GIVEN
        String id = randomUUID().toString();
        List<String> usersAttending = mock(List.class);

        EventRecord record = new EventRecord();
        record.setId(id);
        record.setName(mockNeat.strings().get());
        record.setOrganizer(mockNeat.strings().get());
        record.setListOfUsersAttending(usersAttending);
        record.setAddress(mockNeat.strings().get());
        record.setDescription(mockNeat.strings().get());

        // WHEN
        when(eventRepository.findById(id)).thenReturn(Optional.of(record));
        Optional<EventRecord> eventResponse = eventRepository.findById(id);

        // THEN
        Assertions.assertNotNull(eventResponse, "The object is returned");
        Assertions.assertEquals(record.getId(), eventResponse.get().getId(), "The id matches");
        Assertions.assertEquals(record.getName(), eventResponse.get().getName(), "The name matches");
    }

    @Test
    void updateEventById() {

        String eventId = randomUUID().toString();

        EventRecord oldEventRecord = new EventRecord();
        oldEventRecord.setId(eventId);
        oldEventRecord.setName(mockNeat.strings().get());
        oldEventRecord.setDate(LocalDate.now().minusDays(2).toString());
        oldEventRecord.setOrganizer(mockNeat.strings().get());
        oldEventRecord.setListOfUsersAttending(mock(List.class));
        oldEventRecord.setAddress(mockNeat.strings().get());
        oldEventRecord.setDescription(mockNeat.strings().get());

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(oldEventRecord));

        ArgumentCaptor<EventRecord> eventRecordCaptor = ArgumentCaptor.forClass(EventRecord.class);

        String newName = mockNeat.strings().get();
        String newDate = mockNeat.strings().get();
        String newOrganizer = mockNeat.strings().get();
        List<String> newListOfUsersAttending = mock(List.class);
        String newAddress = mockNeat.strings().get();
        String newDescription = mockNeat.strings().get();

        // WHEN
        eventService.updateEventById(eventId, newName, newDate, newOrganizer, newListOfUsersAttending, newAddress, newDescription);

        // THEN
        verify(eventRepository).save(eventRecordCaptor.capture());

        EventRecord record = eventRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The event record has been returned");
        Assertions.assertEquals(record.getId(), eventId, "The event id matches");
        Assertions.assertEquals(record.getName(), newName, "The event name matches");
        Assertions.assertEquals(record.getDate(), newDate, "The event date has been changed");
        Assertions.assertEquals(record.getOrganizer(), newOrganizer, "Events organizer matches");
        Assertions.assertEquals(record.getListOfUsersAttending(), newListOfUsersAttending, "Lists match");
        Assertions.assertEquals(record.getAddress(), newAddress, "Both of the address matches");
        Assertions.assertEquals(record.getDescription(), newDescription, "The descriptions match");




    }

}
