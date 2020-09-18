import axios from 'axios';
import qs from 'qs';
import {IRequests} from "../interface/IRequests";

/**
 * запросы для api backend
 */
const backendApiRequests: IRequests = {
    uri(uri: string) {
        return `/api/rest/${uri}`;
    },
    delete(path: string, data: any, config: any) {
        const configWithData = {
            ...config,
            data,
        };
        return axios.delete(this.uri(path), configWithData);
    },
    get(path: string, data: any, config: any) {
        return axios.get(this.uri(path), {
            ...config,
            params: data,
            paramsSerializer: (params: any) => qs.stringify(params),
        });
    },
    post(path: string, data: any, config: any) {
        return axios.post(this.uri(path), data, config);
    },
    put(path: string, data: any, config: any) {
        return axios.put(this.uri(path), data, config);
    },
};

/**
 * все api
 */
const requests = {
    backendApi: backendApiRequests
};

export default requests;
