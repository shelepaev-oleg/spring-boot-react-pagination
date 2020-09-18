
/**
 * Изменение вложенного свойства
 */
export function setProperty<
    T,
    P1 extends keyof NonNullable<T>
    >(obj: T, value: any, prop1: P1): void;

export function setProperty<
    T,
    P1 extends keyof NonNullable<T>,
    P2 extends keyof NonNullable<NonNullable<T>[P1]>
    >(obj: T, value: any, prop1: P1, prop2: P2): void;

export function setProperty<
    T,
    P1 extends keyof NonNullable<T>,
    P2 extends keyof NonNullable<NonNullable<T>[P1]>,
    P3 extends keyof NonNullable<NonNullable<NonNullable<T>[P1]>[P2]>
    >(obj: T, value: any, prop1: P1, prop2: P2, prop3: P3): void;

export function setProperty(obj: any, value: any, ...props: string[]): any {
    let count = 0;
    obj && props.reduce((result, prop) => {
        (count === props.length - 1) ? result[prop] = value : count++;
        return result[prop];
    }, obj);
}

/**
 * Получение вложенного свойства объекта
 */
export function getProperty<
    T,
    P1 extends keyof NonNullable<T>
    >(obj: T, prop1: P1): NonNullable<T>[P1] | undefined;

export function getProperty<
    T,
    P1 extends keyof NonNullable<T>,
    P2 extends keyof NonNullable<NonNullable<T>[P1]>
    >(obj: T, prop1: P1, prop2: P2): NonNullable<NonNullable<T>[P1]>[P2] | undefined;

export function getProperty<
    T,
    P1 extends keyof NonNullable<T>,
    P2 extends keyof NonNullable<NonNullable<T>[P1]>,
    P3 extends keyof NonNullable<NonNullable<NonNullable<T>[P1]>[P2]>
    >(obj: T, prop1: P1, prop2: P2, prop3: P3): NonNullable<NonNullable<NonNullable<T>[P1]>[P2]>[P3] | undefined;


export function getProperty(obj: any, ...props: string[]): any {
    return obj && props.reduce(
        (result, prop) => result == null ? undefined : result[prop],
        obj
    );
}
