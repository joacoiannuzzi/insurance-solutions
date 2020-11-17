import {InsuranceCompany} from "./insuranceCompany";
import {Role} from "./role";

export class User {
  email: string
  id: number;
  username: string;
  password: string;
  role: Role | string;
  insuranceCompany: InsuranceCompany;


  constructor(email: string, id: number, username: string, password?: string, role?: string, company?: InsuranceCompany) {
    this.email = email;
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
    this.insuranceCompany = company;
  }

  static fromJsonObject(jsonObject: any): User {
    return new User(jsonObject.email, jsonObject.id, jsonObject.name, jsonObject.password, jsonObject.rol, jsonObject.insuranceCompany);
  }

  static roleToString(typ: string | Role): string {
    switch (typ) {
      case "BASE":
        return 'Base';
      case "ADMIN":
        return 'Admin';
      default:
        return 'Invalid option in typeToString';
    }
  }

  static roleToInt(ty: Role | string): number {
    const t = typeof ty == 'string' ? ty : this.fromRoleToString(ty)
    switch (t) {
      case "BASE":
        return 0;
      case "ADMIN":
        return 1;
      default:
        return -1;
    }
  }

  static fromRoleToString(t: Role): string {
    switch (t) {
      case Role.BASE:
        return "BASE";
      case Role.ADMIN:
        return "ADMIN";
      default:
        return "Invalid";
    }
  }
}
