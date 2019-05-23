import {es6test, hello, T} from "./hello-world";
import HelloWorld = T.HelloWorld;

test('Hello function', () => {
  const result = hello();
  expect(result).toBe('Hello world!');
});

test('constructor', () => {
  const result = new HelloWorld("Hello world!");
  expect(result.value).toBe('Hello world!');
});

test('Adding', () => {
  expect(es6test()).toStrictEqual([0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256]);
});
