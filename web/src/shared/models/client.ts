export class Client {
  id: string;
  firstname: string;
  lastname: string;
  dni: number;
  phone: number;
  email: string;

  constructor(id: string, firstname: string, email: string, lastname: string, dni: number, phone: number) {
    this.id = id;
    this.firstname = firstname;
    this.email = email;
    this.lastname = lastname;
    this.dni = dni;
    this.phone = phone;
  }

  static fromJsonObject(jsonObject: any): Client {
    return new Client(jsonObject.id, jsonObject.firstname, jsonObject.email, jsonObject.lastname, jsonObject.dni, jsonObject.phone);
  }
}
