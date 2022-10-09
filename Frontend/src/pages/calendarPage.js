import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import eventClient from "../api/eventClient";

class CalendarPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onGetTable', 'loadIntoTable'], this);
        this.dataStore = new DataStore();
    }

    async onGetTable() {
        let result = await this.client.getAllEvents(this.errorHandler);
        this.dataStore.set(result);
    }

    async loadIntoTable() {
        const eventInfo = this.dataStore.get("attending");

        let eventTable = document.getElementById("upcoming_events");

        if (eventInfo) {
            let myHtml="";
            myHtml += `<tr>
               <th>Event Id</th>
               <th>Name of Event</th>
               <th>Event Date</th>
               <th>User</th>
               <th>Guests Attending</th>
               <th>Description</th>
                </tr>
                `
            for(let event of eventInfo) {
                myHtml += `<tr>
                <td>${event.id}</td>
                <td>${event.name}</td>
                <td>${event.date}</td>
                <td>${event.user}</td>
                <td>${event.listOfAttending}</td>
                <td>${event.address}</td>
                <td>${event.description}</td>
                </tr>
                `
            }
        }
        else {
            eventTable.innerHTML = "<tr><td> no one attending.. </td></tr>"
        }
    }

    async mount() {
        this.event = new eventClient();
        this.dataStore.addChangeListener(this.loadIntoTable);
        this.onGetTable();
    }
}

const main = async () => {
    const calendarPage = new CalendarPage();
    calendarPage.mount();
};


window.addEventListener('DOMContentLoaded', main);
