import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import EditEventClient from "../api/editEventClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class EditPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onEdit', 'renderEvent'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('update-event-form').addEventListener('submit', this.onUpdate);
        this.client = new EditEventClient();
        this.dataStore.addChangeListener(this.renderExample);
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderEvent() {
        let resultArea = document.getElementById('update-event-form');

        const event = this.dataStore.get("event");

        if (event) {
            resultArea.innerHTML = `
            <label style="text-align: center">
                <h3 style = "background: -webkit-linear-gradient(#0000FF, #008080,#0000FF)";
                                                     -webkit-background-clip: text;
                                                     -webkit-text-fill-color: transparent;>
                Name: ${event.name}<br>
                User: ${event.user}<br>
                Date: ${event.date}<br>
                UsersAttending: ${event.listOfUsersAttending}<br>
                Address: ${event.address}<br>
                Description: ${event.description}<br>
                ID: ${event.id}<br>
               </label><br>
                                Your update was successful!

                </body>
            `
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------
    async onUpdate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("update-id-field").value;
        let datePosted = document.getElementById("update-datePosted-field").value;
        let artistName = document.getElementById("update-artistName-field").value;
        let title = document.getElementById("update-title-field").value;
        let dateCreated = document.getElementById("update-dateCreated-field").value;
        let height = document.getElementById("update-height-field").value;
        let width = document.getElementById("update-width-field").value;
        let sold = document.getElementById("update-artwork-sold").value;
        let forSale = document.getElementById("update-artwork-forSale").value;
        let price = document.getElementById("update-artwork-price").value;

        let original = await this.client.getArtwork(id, this.errorHandler);
        this.dataStore.set("artwork", original);
        if (original) {
            let update = await this.client.updateArtwork(
                id, datePosted, artistName, title, dateCreated, height, width, sold, forSale, price);
            this.dataStore.set("artwork", update);
            this.showMessage(`Updated ${update.title}!`);
        } else {
            this.errorHandler("No artwork found with given ID!");
        }
    }

}
/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updatePage = new UpdatePage();
    updatePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
