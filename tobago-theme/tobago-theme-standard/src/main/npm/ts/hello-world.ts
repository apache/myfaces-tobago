export const hello = () => 'Hello world!';

export function es6test(): any {
  return [0,1, 2, 3, 4,5,6,7,8,9,10,11,12,13,14,15,16].map((n) => n * n);
}

export namespace T {

  export class HelloWorld {

    private _value: string;

    public constructor(value: string) {
      this._value = value;
    }

    get value(): string {
      return this._value;
    }
  }
}
