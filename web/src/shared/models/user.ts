import {InsuranceCompany} from "./insuranceCompany";

export class User {
  id: number;
  username: string;
  password: string;
  type: string;
  company: InsuranceCompany;


  constructor(id: number, username: string, password: string, type: string, company: InsuranceCompany) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.type = type;
    this.company = company;
  }

  static fromJsonObject(jsonObject: any): User {
    return new User(jsonObject.id, jsonObject.username, jsonObject.password, jsonObject.type, jsonObject.insuranceCompany);
  }
}
