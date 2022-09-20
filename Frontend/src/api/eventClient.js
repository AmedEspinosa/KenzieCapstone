import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class EventClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getEventById', 'getAllEvents', 'updateEvent',
            'deleteEventById', 'addEvent'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async getEventById(id, errorCallback) {
        try {
            const response = await this.client.get(`/events/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventById", error, errorCallback)
        }
    }

    async getAllEvents(attending, errorCallback) {
        try {
            const response = await this.client.get(`/events/all`);
            return response.data;
        } catch (error) {
            this.handleError("getAllEvents", error, errorCallback)
        }
    }

    async addEvent(id, name, date, user, listOfUsers, address, description, errorCallback) {
        try {
            const response = await this.client.post(`events`, {
                id: id,
                name: name,
                date: date,
                user: user,
                listOfUsers: listOfUsers,
                address: address,
                description: description
            });
            return response.data;
        } catch (error) {
            this.handleError("addEvent", error, errorCallback)
        }
    }

    async updateEvent(id, name, date, user, listOfUsers, address, description, errorCallback) {
        try {
            const response = await this.client.put(`events`, {
                id: id,
                name: name,
                date: date,
                user: user,
                listOfUsers: listOfUsers,
                address: address,
                description: description
            });
            return response.data;
        } catch (error) {
            this.handleError("updateEvent", error, errorCallback);
        }
    }

    async deleteEventById(eventId, errorCallback) {
        try {
            const response = await this.client.delete(`events/${eventId}`, {
                eventId: eventId
            });
            return response.data;
        } catch (error) {
            this.handleError("deleteEventById", error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param method
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
