import {Client} from "./client";

export class InsuranceCompany {
  id: number;
  name: string;
  clients: Client[];

  constructor(id: number, name: string, clients: Client[]) {
    this.id = id;
    this.name = name;
    this.clients = clients;
  }

  static fromJsonObject(jsonObject: any): InsuranceCompany {
    return new InsuranceCompany(jsonObject.id, jsonObject.name, jsonObject.clients);
  }
}
