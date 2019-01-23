declare var Tobago4;
declare var moment;
declare var jsf;

interface JQuery {
  overlay(data?: any, options?: any): JQuery; // XXX is "data" correct?
  datetimepicker(data?: any, options?: any): JQuery;
  popover(data?: any, options?: any): JQuery;
  modal(data?: any, options?: any): JQuery;
  typeahead(data?: any, options?: any): JQuery;
}

/*
todos: remove jqueryui (also from package.json)
*/