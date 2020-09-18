import { AxiosPromise, AxiosRequestConfig } from 'axios';

/**
 * Интерфейс для запросов
 */
export interface IRequests {

    /**
     * Куки, иной раз надо
     */
    cookies?: Array<[string, string]>

    /**
     * путь до api
     */
    uri(uri: string): string

    /**
     * Запрос типа get
     */
    get(path: string, data: any, config?: AxiosRequestConfig): AxiosPromise

    /**
     * Запрос типа post
     */
    post(path: string, data: any, config?: AxiosRequestConfig): AxiosPromise

    /**
     * Запрос типа put
     */
    put(path: string, data: any, config?: AxiosRequestConfig): AxiosPromise

    /**
     * Запрос типа delete
     */
    delete(path: string, data?: any, config?: AxiosRequestConfig): AxiosPromise
}
