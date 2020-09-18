/**
 * Получаение элемента массива, в котором параметр соответствует значению
 */
export function getOneByValue<T>(source: T[], propertie: keyof T, desiredValue: T[keyof T]): T | null {
    const filteredArray: Array<T> = source.filter(item => item[propertie] === desiredValue);

    if (filteredArray.length === 0) {
        // Нужных значений нет. Возвращаем undefined
        return null;
    }
    // Возвращаем первый попавшийся элемент, соответствующий условию
    return filteredArray[0];

}