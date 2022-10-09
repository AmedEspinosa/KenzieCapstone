import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import eventClient from "../api/eventClient";

class LandingPage extends BaseClass {
    constructor() {
        super();

        this.bindClassMethods(['onCreateEvent', 'onUpdateEvent', 'onDeleteEvent', 'onGet'], this);

        this.bindClassMethods(['onCreateEvent', 'onUpdateEvent', 'onDeleteEvent'], this);

        this.dataStore = new DataStore();
    }

    async onCreateEvent(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();


        let name = document.getElementById("name").value;
        let date = document.getElementById("date").value;
        let user = document.getElementById("user").value;
        let listOfAttending = document.getElementById("list_Attending").value;
        let address = document.getElementById("address").value;
        let description = document.getElementById("description").value;

        const createdEvent = await this.event.addEvent(name,date,user,listOfAttending,address,description);
        this.dataStore.set("createdEvent",createdEvent);

        if (createdEvent) {
            this.showMessage(`Created ${createdEvent.name}!`)
        } else {
            this.errorHandler("Error creating! Try again...")
        }



    }

    async onUpdateEvent(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();


        let id = document.getElementById("id3").value;
        let name = document.getElementById("name3").value;
        let date = document.getElementById("date3").value;
        let user = document.getElementById("user3").value;
        let listOfAttending = document.getElementById("list_Attending3").value;
        let address = document.getElementById("address3").value;
        let description = document.getElementById("description3").value;

        let original = await this.event.getEventById(id,this.errorHandler);
        this.dataStore.set("event",original);
        if (original) {
            let update = await this.event.updateEvent(id,name,date,user,listOfAttending,address,description);
            this.dataStore.set("event",update);
            this.showMessage(`Updated ${update.name}!`)
        } else{
            this.errorHandler("No event found with given ID!");
        }

    }

    async onDeleteEvent(event) {
        event.preventDefault();


        let id = document.getElementById("id4").value;
        this.dataStore.set("event", null);
        let result = await this.event.getEventById(id, this.errorHandler);
        this.dataStore.set("event", result);
        if (result) {
            this.showMessage(`Found event ${result.name}!`);
            await this.event.deleteEventById(id, this.errorHandler);
            this.showMessage(`Deleted event ${result.name}`);
        } else {
            this.errorHandler("No event found with given ID!");
        }
    }

    async onGet(event) {
        event.preventDefault();

        let id = document.getElementById("id2").value;
        this.dataStore.set("event", null);

        let result = await this.event.getEventById(id, this.errorHandler);
        this.dataStore.set("event", result);

        if (result) {
            this.showMessage(`Found event ${result.name}!`)
        } else {
            this.errorHandler("No event found with given ID!");
        }
    }



    async mount() {
        document.getElementById("create_event_form").addEventListener('submit', this.onCreate);

    }

    async mount() {

        this.event = new eventClient();
    }
}

const main = async () => {
    const landingPage = new LandingPage();
    landingPage.mount();
};


window.addEventListener('DOMContentLoaded', main);


