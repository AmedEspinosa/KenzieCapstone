import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import eventClient from "../api/eventClient";

class SearchPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onGet'], this);
        this.dataStore = new DataStore();
    }

    async onGet(event) {
        event.preventDefault();

        let id = document.getElementById("id2").value;
        this.dataStore.set("event", null);

        let result = await this.client.getEventById(id, this.errorHandler);
        this.dataStore.set("event", result);

        if (result) {
            this.showMessage(`Found event ${result.name}!`)
        } else {
            this.errorHandler("No event found with given ID!");
        }
    }

    async mount() {
        document.getElementById("get_event_form").addEventListener('submit', this.onGet);
        this.event = new eventClient();
    }
}

const main = async () => {
    const searchPage = new SearchPage();
    searchPage.mount();
};


window.addEventListener('DOMContentLoaded', main);

