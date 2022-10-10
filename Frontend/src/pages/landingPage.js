import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import eventClient from "../api/eventClient";

class LandingPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreateEvent', 'onUpdateEvent', 'onDeleteEvent', 'onGet'], this);
        // this.dataStore = new DataStore();
    }

    async mount() {
        console.log("we have mounted")
        document.getElementById("create_event_form").addEventListener('submit', this.onCreateEvent);
        document.getElementById("get_event_form").addEventListener('submit', this.onGet);
        document.getElementById("update_event_form").addEventListener('submit', this.onUpdateEvent);
        document.getElementById("delete_event").addEventListener('submit', this.onDeleteEvent);

        // document.getElementById().addEventListener()
        this.client = new eventClient();
    }

    async onCreateEvent(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let name = document.getElementById("name").value;
        let date = document.getElementById("date").value;
        let user = {
            id: "1",
            name: "Bobby",
            email: "emailOfUser@email.com"
        }
        // let listOfAttending = document.getElementById("list_Attending").value;
        // let listOfAttending = document.getElementById("list_Attending").value;
        let listOfAttending =
            [
                {
                    id: "1",
                    name: "Bobby",
                    email: "email"
                }
            ];
        let address = document.getElementById("address").value;
        let description = document.getElementById("description").value;

        console.log(name);
        console.log(date);
        console.log(user);
        console.log(listOfAttending);
        console.log(address);
        console.log(description);

        const createdEvent = await this.client.addEvent(name,date,user,listOfAttending,address,description);

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

        let original = await this.client.getEventById(id,this.errorHandler);
        this.dataStore.set("event",original);
        if (original) {
            let update = await this.client.updateEvent(id,name,date,user,listOfAttending,address,description);
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
        let result = await this.client.getEventById(id, this.errorHandler);
        this.dataStore.set("event", result);
        if (result) {
            this.showMessage(`Found event ${result.name}!`);
            await this.client.deleteEventById(id, this.errorHandler);
            this.showMessage(`Deleted event ${result.name}`);
        } else {
            this.errorHandler("No event found with given ID!");
        }
    }

    async onGet(event) {
        event.preventDefault();

        let id = document.getElementById("id2").value;
        // this.dataStore.set("event", null);

        let result = await this.client.getEventById(id, this.errorHandler);
        //this.dataStore.set("event", result);
        console.log(`Result found ${result.name}`);
        if (result) {
            this.showMessage(`Found event ${result.name}!`)
        } else {
            this.errorHandler("No event found with given ID!");
        }
    }
}

const main = async () => {
    console.log("We hit main")
    const landingPage = new LandingPage();
    console.log("landing page is instantiated")
    await landingPage.mount();
};


window.addEventListener('DOMContentLoaded', main);


