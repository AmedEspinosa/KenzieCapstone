import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import eventClient from "../api/eventClient";

class LandingPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreateEvent', 'onUpdateEvent', 'onDeleteEvent'], this);
        this.dataStore = new DataStore();
    }

    async onCreateEvent(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
    }

    async onUpdateEvent(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
    }

    async onDeleteEvent(event) {
        event.preventDefault();
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


