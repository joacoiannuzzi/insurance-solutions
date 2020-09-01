export class Client {
  id: string;
  firstName: string;
  lastName: string;
  dni: number;
  phoneNumber: number;
  mail: string;

  constructor(id: string, firstName: string, mail: string, lastName: string, dni: number, phoneNumber: number) {
    this.id = id;
    this.firstName = firstName;
    this.mail = mail;
    this.lastName = lastName;
    this.dni = dni;
    this.phoneNumber = phoneNumber;
  }

  static fromJsonObject(jsonObject: any): Client {
    return new Client(jsonObject.id, jsonObject.firstName, jsonObject.mail, jsonObject.lastName, jsonObject.dni, jsonObject.phoneNumber);
  }
}
