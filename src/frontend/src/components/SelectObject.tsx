import React from 'react';
import { selectObjectOptions } from './SelectObjectOptions';
import { setTimeout as timersSetTimeout, clearTimeout as timerClearTimeout } from 'timers';
import {SelectProps, SelectValue} from "antd/es/select";
import {getOneByValue} from "../utils/ArrayFunctions";
import Select from "antd/es/select";

// Структура типового объекта для построения селекта
export interface SelectableObject {
    id?: number | string
    code?: string | number
    name?: string | null
    allowClear?: boolean | false
}

// Свойства и методы селекта
export interface SelectableObjectProps<T> {
    dataSource: T[]
    optionTextRender?: (record: T) => string

    onScroll: () => void

    searchTimeoutDelay?: number
    searchLengthDelay?: number
}

interface SelectObjectProps<T> extends SelectableObjectProps<T>, SelectProps<T> {

}

/**
 * Компонент 'Выпадающий список'
 */
export default class SelectObject<T extends SelectableObject> extends React.Component<SelectObjectProps<T>> {
    // Хранит результат запуска таймаута поиска
    searchTimeoutId?: NodeJS.Timeout;

    onChange = (value: SelectValue, option: React.ReactElement | React.ReactElement[]): void => {
        // Берем первую попавшуюся строку для определения параметров ключа
        if (this.props.dataSource.length === 0) {
            return;
        }
        const firstString: T = this.props.dataSource[0];
        let keyName: 'id' | 'code';
        if (firstString.hasOwnProperty('id')) {
            keyName = 'id';
        } else if (firstString.hasOwnProperty('code')) {
            keyName = 'code';
        } else {
            throw new Error('ОШИБКА В ПРОГРАММЕ: У объекта нет ни кода, ни id.');
        }

        // Преобразуем значение в объект и поднимаем наверх объект
        const objectValue = getOneByValue<T>(this.props.dataSource, keyName, value as unknown as T[keyof T]) as T;

        if (this.props.onChange) {
            this.props.onChange(objectValue, option);
        }
    };

    delayedSearch = (value: string): void => {
        // TS ругается. Хотя я это уже проверил в onSearch
        if (!this.props.onSearch) {
            return;
        }

        this.props.onSearch(value);
    };

    onSearch = (value: string): void => {
        // Если из родителя onSearch не передан, то ничего не делаем
        if (!this.props.onSearch) {
            return;
        }

        // Если не переданы параметры задержки, то вызываем родительскую функцию
        if (!this.props.searchTimeoutDelay && !this.props.searchLengthDelay) {
            this.props.onSearch(value);
        }

        // Отсечка по длине строки
        if (this.props.searchLengthDelay && value.length < this.props.searchLengthDelay) {
            return;
        }

        // Сбрасываем результат предыдущего запуска таймаута
        timerClearTimeout(this.searchTimeoutId as NodeJS.Timeout);
        // Устанавливанием запуск
        this.searchTimeoutId = timersSetTimeout(() => this.delayedSearch(value), this.props.searchTimeoutDelay as number);
    };

    getValue = () => {
        if (this.props.disabled || this.props.dataSource.length === 0) {
            // Поле в режиме RO. Выводим представление объекта или name
            if (this.props.optionTextRender && this.props.value) {
                return this.props.optionTextRender(this.props.value);
            }
            return this.props.value ? this.props.value.name : '';

        }
        return this.props.value
            ? this.props.value.id
                ? this.props.value.id
                : this.props.value.code
            : undefined;

    };

    onScroll = (event: any) => {
        let target = event.target;
        if (!this.props.loading && target.scrollTop + target.offsetHeight === target.scrollHeight) {
            this.props.onScroll();
        }
    };

    render(): React.ReactElement {
        // Преобразуем значение из объекта в ИД или код
        const value = this.getValue();

        // Рисуем список выбора только если форма не в режиме RO и не disabled
        let children = null;
        if (!this.props.disabled) {
            children = this.props.children
                ? this.props.children
                : selectObjectOptions({ ...this.props as SelectableObjectProps<T> });
        }

        return (
            <Select
                showSearch
                notFoundContent="Информация отсутствует"
                optionFilterProp={'children'}
                className='field-width'
                // Переданные сверху пропсы селекта
                {...this.props as SelectProps}

                // onChange и присвоение value делаем ниже переданных пропсов, чтобы перекрыть передачу
                onChange={this.onChange}
                onSearch={this.onSearch}
                onPopupScroll={this.onScroll}
                value={value as string}
            >{children}</Select>
        )
    }
}
