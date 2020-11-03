import {InsuranceCompany} from "./insuranceCompany";
import {Type} from "./type";

export class User {
  email: string
  id: number;
  username: string;
  password: string;
  type: Type | string;
  insuranceCompany: InsuranceCompany;


  constructor(email: string, id: number, username: string, password?: string, type?: string, company?: InsuranceCompany) {
    this.email = email;
    this.id = id;
    this.username = username;
    this.password = password;
    this.type = type;
    this.insuranceCompany = company;
  }

  static fromJsonObject(jsonObject: any): User {
    return new User(jsonObject.email, jsonObject.id, jsonObject.username, jsonObject.password, jsonObject.type, jsonObject.insuranceCompany);
  }

  static typeToString(typ: string | Type): string {
    switch (typ) {
      case "BASE":
        return 'Base';
      case "ADMIN":
        return 'Admin';
      default:
        return 'Invalid option in typeToString';
    }
  }

  static typeToInt(ty: Type | string): number {
    const t = typeof ty == 'string' ? ty : this.fromTypeToString(ty)
    switch (t) {
      case "BASE":
        return 0;
      case "ADMIN":
        return 1;
      default:
        return -1;
    }
  }

  static fromTypeToString(t: Type): string {
    switch (t) {
      case Type.BASE:
        return "BASE";
      case Type.ADMIN:
        return "ADMIN";
      default:
        return "Invalid";
    }
  }
}
