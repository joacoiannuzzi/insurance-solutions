export class Client {
  id: number;
  firstName: string;
  lastName: string;
  dni: string;
  phoneNumber: string;
  mail: string;
  insuranceCompany?: string;
  vehicle?: string;

  constructor(id: number, firstName: string, mail: string, lastName: string, dni: string, phoneNumber: string) {
    this.id = id;
    this.firstName = firstName;
    this.mail = mail;
    this.lastName = lastName;
    this.dni = dni;
    this.phoneNumber = phoneNumber;
    // TODO: Eliminar
    this.insuranceCompany = "dsad";
    this.vehicle = "dsad";
  }

  static fromJsonObject(jsonObject: any): Client {
    return new Client(jsonObject.id, jsonObject.firstName, jsonObject.mail, jsonObject.lastName, jsonObject.dni, jsonObject.phoneNumber);
  }
}
