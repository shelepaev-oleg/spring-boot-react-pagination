import React from 'react';
import { SelectableObject, SelectableObjectProps } from './SelectObject';
import {Select} from 'antd';

type SelectObjectOptionProps<T> = SelectableObjectProps<T>

export function selectObjectOptions<T extends SelectableObject>(props: SelectObjectOptionProps<T>): JSX.Element[] {
    // Ключ строим как значение, преобразованное к строке
    const keyRender = (item: T): string => item.id ? item.id.toString() : item.code ? item.code.toString() : '';
    // Значение строим на основании ИД или кода
    const valueRender = (item: T): string | number | undefined => item.id ? item.id : item.code;
    // В тексте выводим наименование или значение из внешнего рендера
    const childRender = props.optionTextRender || ((item: T) => item.name ? item.name : '');

    return props.dataSource.map((item: T) => <Select.Option
        key={keyRender(item)}
        value={valueRender(item)}
        title={item.name || childRender(item)}
    >{childRender(item)}</Select.Option>)
}
