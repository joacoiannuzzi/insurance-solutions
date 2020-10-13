import {Vehicle} from "./vehicle";
import {InsuranceCompany} from "./insuranceCompany";

export class Client {
  id: number;
  firstName: string;
  lastName: string;
  dni: string;
  phoneNumber: string;
  mail: string;
  insuranceCompany?: InsuranceCompany;
  vehicles?: Vehicle[];

  constructor(id: number, firstName: string, mail: string, lastName: string, dni: string, phoneNumber: string, vehicles?: Vehicle[], insuranceComp?: InsuranceCompany) {
    this.id = id;
    this.firstName = firstName;
    this.mail = mail;
    this.lastName = lastName;
    this.dni = dni;
    this.phoneNumber = phoneNumber;
    // TODO: Eliminar
    this.insuranceCompany = insuranceComp;
    this.vehicles = vehicles ? vehicles : [];
  }

  static fromJsonObject(jsonObject: any): Client {
    return new Client(jsonObject.id, jsonObject.firstName, jsonObject.mail, jsonObject.lastName, jsonObject.dni, jsonObject.phoneNumber,
      jsonObject.vehicles.map((vehicle) => Vehicle.fromJsonObject(vehicle)), jsonObject.insuranceCompany);
  }
}
