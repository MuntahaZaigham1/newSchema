export enum ListColumnType {
  String = 'string',
    Number = 'number',
    Date = 'date',
    DateTime = 'dateTime',
    Time = 'time',
    Boolean = 'boolean',
    NumericArray = 'numericArray',
    Array = 'array',
    File = 'file',
}

export interface IListColumn {
  column: string;
  searchColumn?: string;
  label: string;
  sort: boolean;
  filter: boolean;
  type: ListColumnType;
  options?: Array<any>;
}

export interface Field {
  name: string,
  label: string,
  isRequired: boolean,
  isAutoGenerated: boolean,
  type: FieldType,
}

export enum FieldType {
  String = 'string',
  Number = 'number',
  Date = 'date',
  DateTime = 'dateTime',
  Time = 'time',
  Boolean = 'boolean',
  NumericArray = 'numericArray',
  Array = 'array',
  File = 'file',
}