import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import eventClient from "../api/eventClient";

class SearchPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onGet'], this);
        // this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById("get_event_form").addEventListener('submit', this.onGet);
        this.client = new eventClient();
    }

    async onGet(event) {
        event.preventDefault();
        let eventHtml = "";

        let id = document.getElementById("id2").value;
        // this.dataStore.set("event", null);

        let result = await this.client.getEventById(id, this.errorHandler);
        // this.dataStore.set("event", result);

        if (result) {
            this.showMessage(`Found event ${result.name}!`)
            eventHtml += `
                    <div class="form_1">
                        <h2 style="color: #F27A24">${result.name}</h2>
                        <div>Event Date: ${result.date}</div>
                        <div>Event Sponsor: ${result.user}</div>
                        <div>Attending: ${result.listOfAttending}</div>
                        <div>Event Address: ${result.address}</div>
                        <div>Event Description: ${result.description}</div>
                `;
            document.getElementById("event_list").innerHTML = eventHtml;
        } else {
            this.errorHandler("No event found with given ID!");
        }
    }

}

    const main = async () => {
        const searchPage = new SearchPage();
        await searchPage.mount();
    };


    window.addEventListener('DOMContentLoaded', main);

