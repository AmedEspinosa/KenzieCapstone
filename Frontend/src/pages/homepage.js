import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ViewEventClient from "../api/viewEventClient";
/**
 * Logic needed for the view playlist page of the website.
 */
class Homepage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetAll', 'renderEvent'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('view-all-events-form').addEventListener('submit', this.onGet);
        this.client = new ViewEventClient();

        this.dataStore.addChangeListener(this.renderEvent())
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderEvent() {
        let resultArea = document.getElementById("view-all-events-form");

        const event = this.dataStore.get("event");

        if (event) {
            resultArea.innerHTML = `
                 <body>
                    <h3 style = "background: -webkit-linear-gradient(#0000FF, #008080,#0000FF)";
                                                         -webkit-background-clip: text;
                                                         -webkit-text-fill-color: transparent;
                                                         "align-text:center";>
                    <label style="text-align: center">
                    Name: ${event.getName}<br>
                    DateCreated: ${event.getDate}<br>
                    User: ${event.getUser}<br>
                    Address: ${event.getAddress}<br>
                    UsersAttending: ${event.getListOfUsersAttending}<br>
                    Description: ${event.getDescription}<br>
                    </label>
                    </h3>
            `
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetAll(event) {
        event.preventDefault();

        const events = await this.client.getEvent(this.errorHandler)

        this.dataStoreList.set("events", events);
    }

    async onGet(event) {
        event.preventDefault();

        let id = document.getElementById("view-event-id").value;
        this.dataStore.set("event", null);

        let result = await this.client.getEvent(id, this.errorHandler);
        this.dataStore.set("event", result);

        if (result) {
            this.showMessage(`Found event ${result.title}!`)
        } else {
            this.errorHandler("No event found with given ID!");
        }
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const homepage = new Homepage();
    homepage.mount();
};

window.addEventListener('DOMContentLoaded', main);
