import {DateUtils} from "./tobago-date-utils";

test('pattern format: yyyy', () => {
  const result = DateUtils.convertPattern("yyyy");
  expect(result).toBe("yyyy");
});

test('pattern format: yy', () => {
  const result = DateUtils.convertPattern("yy");
  expect(result).toBe("yy");
});

test('pattern format: dd', () => {
  const result = DateUtils.convertPattern("dd");
  expect(result).toBe("dd");
});

test('pattern format: d', () => {
  const result = DateUtils.convertPattern("d");
  expect(result).toBe("d");
});

test('pattern format: MM', () => {
  const result = DateUtils.convertPattern("MM");
  expect(result).toBe("mm");
});

test('pattern format: M', () => {
  const result = DateUtils.convertPattern("M");
  expect(result).toBe("m");
});
