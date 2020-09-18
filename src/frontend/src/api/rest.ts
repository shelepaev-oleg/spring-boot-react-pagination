import requests from "../utils/requests";
import {ISortFilterPageableRequest} from "../utils/sortFilter/ISortFilterPageableRequest";

const config = { withCredentials: true };

/**
 * Возвращает страницу Стран
 * @param {ISortFilterPageableRequest} sortFilterPageableRequest
 */
export const getCountryPage = (sortFilterPageableRequest: ISortFilterPageableRequest) =>
    requests.backendApi.post('country/page', sortFilterPageableRequest, config);
